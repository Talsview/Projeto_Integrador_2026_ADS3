package br.com.avcar.oficina.business.pagamento.service;

import br.com.avcar.oficina.business.ordemservico.dto.AlterarStatusOrdemServicoDTO;
import br.com.avcar.oficina.business.ordemservico.enums.StatusFluxoOrdemServico;
import br.com.avcar.oficina.business.ordemservico.model.HistoricoStatusOrdemModel;
import br.com.avcar.oficina.business.ordemservico.model.OrdemServicoModel;
import br.com.avcar.oficina.business.ordemservico.repository.IHistoricoStatusOrdemRepository;
import br.com.avcar.oficina.business.ordemservico.service.OrdemServicoService;
import br.com.avcar.oficina.business.pagamento.dto.PagamentoDTO;
import br.com.avcar.oficina.business.pagamento.dto.ResumoPagamentoOrdemServicoDTO;
import br.com.avcar.oficina.business.pagamento.enums.StatusPagamento;
import br.com.avcar.oficina.business.pagamento.mapper.PagamentoMapper;
import br.com.avcar.oficina.business.pagamento.model.PagamentoModel;
import br.com.avcar.oficina.business.pagamento.repository.IPagamentoRepository;
import br.com.avcar.oficina.business.pagamento.validation.PagamentoValidation;
import br.com.avcar.oficina.core.exception.BusinessException;
import br.com.avcar.oficina.core.exception.RuleValidationException;
import br.com.avcar.oficina.core.service.GenericService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsável pelo controle financeiro das Ordens de Serviço.
 *
 * Regras atendidas:
 * - OrdemServico pode gerar nenhum, um ou vários pagamentos.
 * - Apenas pagamentos com status PAGO abatem o saldo financeiro da OS.
 * - O pagamento somente pode ser registrado quando a OS já chegou à etapa PAGAMENTO,
 *   respeitando o fluxo Orçamento -> Execução -> Pagamento -> Finalizado.
 * - Quando o valor pago quita a OS, o sistema finaliza automaticamente a OS
 *   e inicia as garantias de peças e serviços.
 * - OS finalizada não permite alteração de pagamentos.
 */
@Service
public class PagamentoService extends GenericService<PagamentoModel> {

    private final IPagamentoRepository pagamentoRepository;
    private final IHistoricoStatusOrdemRepository historicoStatusRepository;
    private final OrdemServicoService ordemServicoService;
    private final PagamentoValidation validation;
    private final PagamentoMapper mapper;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public PagamentoService(IPagamentoRepository pagamentoRepository,
                            IHistoricoStatusOrdemRepository historicoStatusRepository,
                            OrdemServicoService ordemServicoService,
                            PagamentoValidation validation,
                            PagamentoMapper mapper) {
        super(pagamentoRepository, null);
        this.pagamentoRepository = pagamentoRepository;
        this.historicoStatusRepository = historicoStatusRepository;
        this.ordemServicoService = ordemServicoService;
        this.validation = validation;
        this.mapper = mapper;
    }

    @Transactional
    /**
     * Função: Valida os dados recebidos, monta as entidades necessárias e grava o cadastro de
     * pagamento.
     * Uso no sistema: centraliza a regra de cadastro na camada Service, mantendo Controller e tela
     * mais simples.
     */
    public PagamentoDTO cadastrar(PagamentoDTO dto) {
        validation.validateInsert(dto);

        OrdemServicoModel ordemServico = ordemServicoService.buscarModelAtivo(dto.getIdOrdemServico());
        validarOrdemPermitePagamento(ordemServico);

        prepararDataPagamento(dto);
        validarPagamentoContraOrdem(dto, ordemServico, null);
        PagamentoModel saved = pagamentoRepository.save(mapper.toModel(dto, ordemServico));

        finalizarOrdemAutomaticamenteSeQuitada(ordemServico.getId());

        return mapper.toDto(saved);
    }

    @Transactional
    /**
     * Função: Busca o registro ativo, aplica as alterações permitidas e salva a atualização de
     * pagamento.
     * Uso no sistema: garante que alterações passem por validação e não quebrem vínculos já existentes
     * no sistema.
     */
    public PagamentoDTO atualizar(Long id, PagamentoDTO dto) {
        validation.validateUpdate(id, dto);
        PagamentoModel pagamento = buscarModelAtivo(id);
        OrdemServicoModel ordemServico = ordemServicoService.buscarModelAtivo(dto.getIdOrdemServico());
        validarOrdemPermitePagamento(pagamento.getOrdemServico());
        validarOrdemPermitePagamento(ordemServico);
        prepararDataPagamento(dto);
        validarPagamentoContraOrdem(dto, ordemServico, pagamento);
        mapper.atualizarModel(pagamento, dto, ordemServico);
        PagamentoModel saved = pagamentoRepository.save(pagamento);
        finalizarOrdemAutomaticamenteSeQuitada(ordemServico.getId());
        return mapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    /**
     * Função: Localiza informações de pagamento conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    public PagamentoDTO buscar(Long id) {
        validation.validateId(id);
        return mapper.toDto(buscarModelAtivo(id));
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de pagamento aplicando filtros, paginação ou critérios de busca
     * quando informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<PagamentoDTO> listar(Pageable pageable) {
        return pagamentoRepository.findAllByAtivoTrue(pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de pagamento aplicando filtros, paginação ou critérios de busca
     * quando informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<PagamentoDTO> listarPorOrdemServico(Long idOrdemServico, Pageable pageable) {
        validation.validateIdOrdemServico(idOrdemServico);
        return pagamentoRepository.findByOrdemServicoIdAndAtivoTrue(idOrdemServico, pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de pagamento aplicando filtros, paginação ou critérios de busca
     * quando informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<PagamentoDTO> pesquisarPorOrdemServico(Long idOrdemServico, String termo, Pageable pageable) {
        validation.validateIdOrdemServico(idOrdemServico);
        if (termo == null || termo.isBlank()) {
            return listarPorOrdemServico(idOrdemServico, pageable);
        }
        return pagamentoRepository.searchByOrdemServico(idOrdemServico, termo.trim(), pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    /**
     * Função: Processa dados de serviço executado, responsável, valor e vínculo com a Ordem de
     * Serviço.
     * Uso no sistema: garante que cada serviço da OS tenha registro próprio e colaborador responsável.
     */
    public ResumoPagamentoOrdemServicoDTO resumirPorOrdemServico(Long idOrdemServico) {
        validation.validateIdOrdemServico(idOrdemServico);
        OrdemServicoModel ordemServico = ordemServicoService.buscarModelAtivo(idOrdemServico);
        BigDecimal valorPago = calcularValorPago(idOrdemServico);
        BigDecimal valorTotal = zeroIfNull(ordemServico.getValorTotal());
        BigDecimal valorPendente = valorTotal.subtract(valorPago);
        if (valorPendente.compareTo(BigDecimal.ZERO) < 0) {
            valorPendente = BigDecimal.ZERO;
        }

        ResumoPagamentoOrdemServicoDTO resumo = new ResumoPagamentoOrdemServicoDTO();
        resumo.setIdOrdemServico(ordemServico.getId());
        resumo.setNumeroOs(ordemServico.getNumeroOs());
        resumo.setValorTotalOrdemServico(valorTotal);
        resumo.setValorPago(valorPago);
        resumo.setValorPendente(valorPendente);
        resumo.setQuitada(valorPago.compareTo(valorTotal) >= 0);
        return resumo;
    }

    @Transactional
    /**
     * Função: Consulta ou altera o status operacional, registrando a evolução do processo quando
     * necessário.
     * Uso no sistema: mantém o fluxo Orçamento, Execução, Pagamento e Finalizado rastreável.
     */
    public PagamentoDTO alterarStatus(Long id, StatusPagamento statusPagamento) {
        validation.validateId(id);
        validation.validateStatus(statusPagamento);
        PagamentoModel pagamento = buscarModelAtivo(id);
        validarOrdemPermitePagamento(pagamento.getOrdemServico());
        pagamento.setStatusPagamento(statusPagamento);
        if (statusPagamento == StatusPagamento.PAGO && pagamento.getDataPagamento() == null) {
            pagamento.setDataPagamento(LocalDateTime.now());
        }
        if (statusPagamento == StatusPagamento.CANCELADO || statusPagamento == StatusPagamento.ESTORNADO) {
            pagamento.setDataPagamento(null);
        }
        PagamentoModel saved = pagamentoRepository.save(pagamento);
        if (statusPagamento == StatusPagamento.PAGO) {
            finalizarOrdemAutomaticamenteSeQuitada(saved.getOrdemServico().getId());
        }
        return mapper.toDto(saved);
    }

    @Transactional
    /**
     * Função: Localiza um registro inativado, altera seu campo ativo para verdadeiro e salva a
     * reativação.
     * Uso no sistema: permite recuperar cadastros feitos anteriormente sem duplicar clientes,
     * veículos, peças ou serviços.
     */
    public void inativar(Long id) {
        validation.validateId(id);
        PagamentoModel pagamento = buscarModelAtivo(id);
        validarOrdemPermitePagamento(pagamento.getOrdemServico());
        pagamento.setAtivo(Boolean.FALSE);
        pagamentoRepository.save(pagamento);
    }

    @Transactional(readOnly = true)
    /**
     * Função: Calcula valores agregados a partir de serviços, peças, quantidade e valor unitário.
     * Uso no sistema: mantém o orçamento e o total da OS coerentes com os itens informados pelo
     * usuário.
     */
    public BigDecimal calcularValorPago(Long idOrdemServico) {
        validation.validateIdOrdemServico(idOrdemServico);
        BigDecimal total = pagamentoRepository.somarValorPorStatus(idOrdemServico, StatusPagamento.PAGO);
        return zeroIfNull(total);
    }

    @Transactional(readOnly = true)
    /**
     * Função: Processa dados de serviço executado, responsável, valor e vínculo com a Ordem de
     * Serviço.
     * Uso no sistema: garante que cada serviço da OS tenha registro próprio e colaborador responsável.
     */
    public boolean ordemServicoQuitada(Long idOrdemServico, BigDecimal valorTotalOrdemServico) {
        BigDecimal valorTotal = zeroIfNull(valorTotalOrdemServico);
        BigDecimal valorPago = calcularValorPago(idOrdemServico);
        return valorPago.compareTo(valorTotal) >= 0;
    }

    /**
     * Função: Localiza informações de pagamento conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    public PagamentoModel buscarModelAtivo(Long id) {
        return pagamentoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Pagamento não encontrado ou inativo."));
    }


    /**
     * Pagamentos não conduzem a OS até a etapa PAGAMENTO.
     *
     * A OS deve chegar em PAGAMENTO pelo fluxo operacional da tela de Ordens
     * de Serviço: ORCAMENTO -> EXECUCAO -> PAGAMENTO. Somente depois disso a
     * tela Pagamentos pode registrar valores. Essa regra evita que o usuário
     * quite uma OS que ainda está em orçamento ou execução.
     */

    private void validarPagamentoContraOrdem(PagamentoDTO dto, OrdemServicoModel ordemServico, PagamentoModel pagamentoAtual) {
        if (dto.getDataPagamento() != null && ordemServico.getDataAbertura() != null
                && dto.getDataPagamento().isBefore(ordemServico.getDataAbertura())) {
            throw new RuleValidationException("A data de pagamento não pode ser anterior à data de abertura da OS.");
        }

        if (dto.getStatusPagamento() != StatusPagamento.PAGO) {
            return;
        }

        BigDecimal valorTotal = zeroIfNull(ordemServico.getValorTotal());
        if (valorTotal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuleValidationException("A Ordem de Serviço deve possuir valor total maior que zero para receber pagamento.");
        }

        BigDecimal valorJaPago = calcularValorPago(ordemServico.getId());
        if (pagamentoAtual != null
                && pagamentoAtual.getStatusPagamento() == StatusPagamento.PAGO
                && pagamentoAtual.getOrdemServico() != null
                && pagamentoAtual.getOrdemServico().getId().equals(ordemServico.getId())) {
            valorJaPago = valorJaPago.subtract(zeroIfNull(pagamentoAtual.getValorPago()));
        }

        BigDecimal saldoPendente = valorTotal.subtract(valorJaPago);
        if (saldoPendente.compareTo(BigDecimal.ZERO) < 0) {
            saldoPendente = BigDecimal.ZERO;
        }

        if (zeroIfNull(dto.getValorPago()).compareTo(saldoPendente) > 0) {
            throw new RuleValidationException("O valor pago não pode ser maior que o saldo pendente da OS. Saldo pendente: R$ " + saldoPendente + ".");
        }
    }

    /**
     * Função: Finaliza a OS ou operação após validar que as etapas anteriores foram cumpridas.
     * Uso no sistema: encerra o atendimento e permite iniciar garantias de peças e serviços quando
     * aplicável.
     */
    private void finalizarOrdemAutomaticamenteSeQuitada(Long idOrdemServico) {
        OrdemServicoModel ordemServico = ordemServicoService.buscarModelAtivo(idOrdemServico);
        HistoricoStatusOrdemModel statusAtual = buscarStatusAtualOuNulo(idOrdemServico);
        if (statusAtual == null) {
            return;
        }

        String nomeStatus = statusAtual.getStatusOrdemServico().getNomeStatus();
        if (!StatusFluxoOrdemServico.PAGAMENTO.name().equals(nomeStatus)) {
            return;
        }

        BigDecimal valorTotal = zeroIfNull(ordemServico.getValorTotal());
        BigDecimal valorPago = calcularValorPago(idOrdemServico);

        if (valorTotal.compareTo(BigDecimal.ZERO) > 0 && valorPago.compareTo(valorTotal) >= 0) {
            alterarStatusAutomaticamente(idOrdemServico, StatusFluxoOrdemServico.FINALIZADO,
                    "Finalização automática após quitação financeira da Ordem de Serviço.");
        }
    }

    /**
     * Função: Consulta ou altera o status operacional, registrando a evolução do processo quando
     * necessário.
     * Uso no sistema: mantém o fluxo Orçamento, Execução, Pagamento e Finalizado rastreável.
     */
    private void alterarStatusAutomaticamente(Long idOrdemServico,
                                              StatusFluxoOrdemServico novoStatus,
                                              String observacao) {
        AlterarStatusOrdemServicoDTO dto = new AlterarStatusOrdemServicoDTO();
        dto.setNovoStatus(novoStatus);
        dto.setObservacao(observacao);
        ordemServicoService.alterarStatus(idOrdemServico, dto);
    }

    /**
     * Função: Processa informações financeiras da OS, como valores, parcelas, quitação ou consulta de
     * pagamentos.
     * Uso no sistema: separa a etapa financeira da execução do serviço e mantém o histórico de
     * recebimentos.
     */
    private void validarOrdemPermitePagamento(OrdemServicoModel ordemServico) {
        HistoricoStatusOrdemModel statusAtual = buscarStatusAtualOuNulo(ordemServico.getId());
        if (statusAtual == null) {
            throw new RuleValidationException("A Ordem de Serviço não possui status para receber pagamento.");
        }
        String nomeStatus = statusAtual.getStatusOrdemServico().getNomeStatus();
        if (!StatusFluxoOrdemServico.PAGAMENTO.name().equals(nomeStatus)) {
            throw new RuleValidationException("Pagamento só pode ser registrado ou alterado quando a OS estiver na etapa PAGAMENTO.");
        }
    }


    @Transactional(readOnly = true)
    /**
     * Função: Lista cadastros inativados para que o usuário possa localizar e reativar registros sem
     * recriá-los.
     * Uso no sistema: reforça a rastreabilidade, pois registros antigos continuam no banco e podem
     * voltar a ficar ativos.
     */
    public Page<PagamentoDTO> listarInativos(Pageable pageable) {
        return pagamentoRepository.findAllByAtivoFalse(pageable).map(mapper::toDto);
    }

    @Transactional
    /**
     * Função: Localiza um registro inativado, altera seu campo ativo para verdadeiro e salva a
     * reativação.
     * Uso no sistema: permite recuperar cadastros feitos anteriormente sem duplicar clientes,
     * veículos, peças ou serviços.
     */
    public PagamentoDTO ativar(Long id) {
        validation.validateId(id);
        PagamentoModel model = pagamentoRepository.findByIdAndAtivoFalse(id)
                .orElseThrow(() -> new BusinessException("Pagamento não encontrado entre os inativos."));
        model.setAtivo(Boolean.TRUE);
        return mapper.toDto(pagamentoRepository.save(model));
    }

    /**
     * Função: Localiza informações de pagamento conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    private HistoricoStatusOrdemModel buscarStatusAtualOuNulo(Long idOrdemServico) {
        List<HistoricoStatusOrdemModel> historico = historicoStatusRepository.findHistoricoFluxoDesc(idOrdemServico);
        return historico.isEmpty() ? null : historico.get(0);
    }

    /**
     * Função: Processa informações financeiras da OS, como valores, parcelas, quitação ou consulta de
     * pagamentos.
     * Uso no sistema: separa a etapa financeira da execução do serviço e mantém o histórico de
     * recebimentos.
     */
    private void prepararDataPagamento(PagamentoDTO dto) {
        if (dto.getStatusPagamento() == null) {
            dto.setStatusPagamento(StatusPagamento.PAGO);
        }
        if (dto.getStatusPagamento() == StatusPagamento.PAGO && dto.getDataPagamento() == null) {
            dto.setDataPagamento(LocalDateTime.now());
        }
    }

    /**
     * Função: Converte valor monetário nulo para zero antes de somar ou comparar.
     * Uso no sistema: evita erro de cálculo em OS com peça, serviço ou pagamento ainda não informado.
     */
    private BigDecimal zeroIfNull(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
