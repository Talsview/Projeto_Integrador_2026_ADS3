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
 * - Ao salvar um pagamento, a OS é conduzida automaticamente até PAGAMENTO,
 *   respeitando o fluxo Orçamento -> Execução -> Pagamento.
 * - Quando o valor pago quita a OS, o sistema finaliza automaticamente a OS
 *   e inicia as garantias de peças e serviços.
 * - OS finalizada não permite alteração de pagamentos.
 */
@Service
public class PagamentoService {

    private final IPagamentoRepository pagamentoRepository;
    private final IHistoricoStatusOrdemRepository historicoStatusRepository;
    private final OrdemServicoService ordemServicoService;
    private final PagamentoValidation validation;
    private final PagamentoMapper mapper;

    public PagamentoService(IPagamentoRepository pagamentoRepository,
                            IHistoricoStatusOrdemRepository historicoStatusRepository,
                            OrdemServicoService ordemServicoService,
                            PagamentoValidation validation,
                            PagamentoMapper mapper) {
        this.pagamentoRepository = pagamentoRepository;
        this.historicoStatusRepository = historicoStatusRepository;
        this.ordemServicoService = ordemServicoService;
        this.validation = validation;
        this.mapper = mapper;
    }

    @Transactional
    public PagamentoDTO cadastrar(PagamentoDTO dto) {
        validation.validateInsert(dto);

        OrdemServicoModel ordemServico = ordemServicoService.buscarModelAtivo(dto.getIdOrdemServico());
        ordemServico = prepararOrdemParaReceberPagamento(ordemServico);

        prepararDataPagamento(dto);
        PagamentoModel saved = pagamentoRepository.save(mapper.toModel(dto, ordemServico));

        finalizarOrdemAutomaticamenteSeQuitada(ordemServico.getId());

        return mapper.toDto(saved);
    }

    @Transactional
    public PagamentoDTO atualizar(Long id, PagamentoDTO dto) {
        validation.validateUpdate(id, dto);
        PagamentoModel pagamento = buscarModelAtivo(id);
        OrdemServicoModel ordemServico = ordemServicoService.buscarModelAtivo(dto.getIdOrdemServico());
        validarOrdemPermitePagamento(pagamento.getOrdemServico());
        validarOrdemPermitePagamento(ordemServico);
        prepararDataPagamento(dto);
        mapper.atualizarModel(pagamento, dto, ordemServico);
        PagamentoModel saved = pagamentoRepository.save(pagamento);
        finalizarOrdemAutomaticamenteSeQuitada(ordemServico.getId());
        return mapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public PagamentoDTO buscar(Long id) {
        validation.validateId(id);
        return mapper.toDto(buscarModelAtivo(id));
    }

    @Transactional(readOnly = true)
    public Page<PagamentoDTO> listar(Pageable pageable) {
        return pagamentoRepository.findAllByAtivoTrue(pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<PagamentoDTO> listarPorOrdemServico(Long idOrdemServico, Pageable pageable) {
        validation.validateIdOrdemServico(idOrdemServico);
        return pagamentoRepository.findByOrdemServicoIdAndAtivoTrue(idOrdemServico, pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<PagamentoDTO> pesquisarPorOrdemServico(Long idOrdemServico, String termo, Pageable pageable) {
        validation.validateIdOrdemServico(idOrdemServico);
        if (termo == null || termo.isBlank()) {
            return listarPorOrdemServico(idOrdemServico, pageable);
        }
        return pagamentoRepository.searchByOrdemServico(idOrdemServico, termo.trim(), pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
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
    public void inativar(Long id) {
        validation.validateId(id);
        PagamentoModel pagamento = buscarModelAtivo(id);
        validarOrdemPermitePagamento(pagamento.getOrdemServico());
        pagamento.setAtivo(Boolean.FALSE);
        pagamentoRepository.save(pagamento);
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularValorPago(Long idOrdemServico) {
        validation.validateIdOrdemServico(idOrdemServico);
        BigDecimal total = pagamentoRepository.somarValorPorStatus(idOrdemServico, StatusPagamento.PAGO);
        return zeroIfNull(total);
    }

    @Transactional(readOnly = true)
    public boolean ordemServicoQuitada(Long idOrdemServico, BigDecimal valorTotalOrdemServico) {
        BigDecimal valorTotal = zeroIfNull(valorTotalOrdemServico);
        BigDecimal valorPago = calcularValorPago(idOrdemServico);
        return valorPago.compareTo(valorTotal) >= 0;
    }

    public PagamentoModel buscarModelAtivo(Long id) {
        return pagamentoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Pagamento não encontrado ou inativo."));
    }


    /**
     * Conduz automaticamente a Ordem de Serviço até a etapa PAGAMENTO quando
     * o usuário registra um pagamento pela tela. A regra de fluxo continua
     * preservada, pois o sistema registra as transições intermediárias no
     * HistoricoStatusOrdem em vez de pular etapas.
     */
    private OrdemServicoModel prepararOrdemParaReceberPagamento(OrdemServicoModel ordemServico) {
        HistoricoStatusOrdemModel statusAtual = buscarStatusAtualOuNulo(ordemServico.getId());
        if (statusAtual == null) {
            throw new RuleValidationException("A Ordem de Serviço não possui status para receber pagamento.");
        }

        String nomeStatus = statusAtual.getStatusOrdemServico().getNomeStatus();

        if (StatusFluxoOrdemServico.FINALIZADO.name().equals(nomeStatus)) {
            throw new RuleValidationException("Ordem de Serviço finalizada não permite registro de novos pagamentos.");
        }

        if (StatusFluxoOrdemServico.ORCAMENTO.name().equals(nomeStatus)) {
            alterarStatusAutomaticamente(ordemServico.getId(), StatusFluxoOrdemServico.EXECUCAO,
                    "Avanço automático para execução ao registrar pagamento.");
            ordemServico = ordemServicoService.buscarModelAtivo(ordemServico.getId());
            nomeStatus = buscarStatusAtualOuNulo(ordemServico.getId()).getStatusOrdemServico().getNomeStatus();
        }

        if (StatusFluxoOrdemServico.EXECUCAO.name().equals(nomeStatus)) {
            alterarStatusAutomaticamente(ordemServico.getId(), StatusFluxoOrdemServico.PAGAMENTO,
                    "Avanço automático para pagamento ao registrar pagamento.");
            ordemServico = ordemServicoService.buscarModelAtivo(ordemServico.getId());
            nomeStatus = buscarStatusAtualOuNulo(ordemServico.getId()).getStatusOrdemServico().getNomeStatus();
        }

        if (!StatusFluxoOrdemServico.PAGAMENTO.name().equals(nomeStatus)) {
            throw new RuleValidationException("A Ordem de Serviço não está em uma etapa válida para receber pagamento.");
        }

        return ordemServico;
    }

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

    private void alterarStatusAutomaticamente(Long idOrdemServico,
                                              StatusFluxoOrdemServico novoStatus,
                                              String observacao) {
        AlterarStatusOrdemServicoDTO dto = new AlterarStatusOrdemServicoDTO();
        dto.setNovoStatus(novoStatus);
        dto.setObservacao(observacao);
        ordemServicoService.alterarStatus(idOrdemServico, dto);
    }

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

    private HistoricoStatusOrdemModel buscarStatusAtualOuNulo(Long idOrdemServico) {
        List<HistoricoStatusOrdemModel> historico = historicoStatusRepository.findHistoricoFluxoDesc(idOrdemServico);
        return historico.isEmpty() ? null : historico.get(0);
    }

    private void prepararDataPagamento(PagamentoDTO dto) {
        if (dto.getStatusPagamento() == null) {
            dto.setStatusPagamento(StatusPagamento.PAGO);
        }
        if (dto.getStatusPagamento() == StatusPagamento.PAGO && dto.getDataPagamento() == null) {
            dto.setDataPagamento(LocalDateTime.now());
        }
    }

    private BigDecimal zeroIfNull(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
