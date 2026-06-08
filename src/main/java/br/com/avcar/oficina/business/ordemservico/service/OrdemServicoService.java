package br.com.avcar.oficina.business.ordemservico.service;

import br.com.avcar.oficina.business.garantia.service.GarantiaService;
import br.com.avcar.oficina.business.ordemservico.dto.AlterarStatusOrdemServicoDTO;
import br.com.avcar.oficina.business.ordemservico.dto.HistoricoStatusOrdemDTO;
import br.com.avcar.oficina.business.ordemservico.dto.ItemServicoDTO;
import br.com.avcar.oficina.business.ordemservico.dto.OrdemServicoDTO;
import br.com.avcar.oficina.business.ordemservico.dto.OrdemServicoResumoDTO;
import br.com.avcar.oficina.business.ordemservico.enums.StatusFluxoOrdemServico;
import br.com.avcar.oficina.business.ordemservico.mapper.HistoricoStatusOrdemMapper;
import br.com.avcar.oficina.business.ordemservico.mapper.ItemServicoMapper;
import br.com.avcar.oficina.business.ordemservico.mapper.OrdemServicoMapper;
import br.com.avcar.oficina.business.ordemservico.model.ExecucaoServicoTerceirizadoModel;
import br.com.avcar.oficina.business.ordemservico.model.HistoricoStatusOrdemModel;
import br.com.avcar.oficina.business.ordemservico.model.ItemServicoModel;
import br.com.avcar.oficina.business.ordemservico.model.OrdemServicoModel;
import br.com.avcar.oficina.business.ordemservico.model.StatusOrdemServicoModel;
import br.com.avcar.oficina.business.ordemservico.repository.IExecucaoServicoTerceirizadoRepository;
import br.com.avcar.oficina.business.ordemservico.repository.IHistoricoStatusOrdemRepository;
import br.com.avcar.oficina.business.ordemservico.repository.IItemServicoRepository;
import br.com.avcar.oficina.business.ordemservico.repository.IOrdemServicoRepository;
import br.com.avcar.oficina.business.ordemservico.validation.OrdemServicoValidation;
import br.com.avcar.oficina.business.peca.model.ItemPecaModel;
import br.com.avcar.oficina.business.peca.repository.IItemPecaRepository;
import br.com.avcar.oficina.business.pagamento.enums.StatusPagamento;
import br.com.avcar.oficina.business.pagamento.repository.IPagamentoRepository;
import br.com.avcar.oficina.business.pessoa.model.ClienteModel;
import br.com.avcar.oficina.business.pessoa.repository.IClienteRepository;
import br.com.avcar.oficina.business.veiculo.model.VeiculoModel;
import br.com.avcar.oficina.business.veiculo.repository.IHistoricoProprietarioRepository;
import br.com.avcar.oficina.business.veiculo.repository.IVeiculoRepository;
import br.com.avcar.oficina.core.exception.BusinessException;
import br.com.avcar.oficina.core.exception.RuleValidationException;
import br.com.avcar.oficina.core.notification.service.NotificacaoService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service central do módulo Ordem de Serviço.
 *
 * Regras atendidas:
 * - Cliente solicita OrdemServico.
 * - Veiculo recebe OrdemServico.
 * - OrdemServico possui HistoricoStatusOrdem.
 * - OrdemServico segue o fluxo Orçamento, Execução, Pagamento e Finalizado.
 * - OrdemServico possui pelo menos um ItemServico antes de avançar para execução.
 */
@Service
public class OrdemServicoService {

    private final IOrdemServicoRepository ordemServicoRepository;
    private final IClienteRepository clienteRepository;
    private final IVeiculoRepository veiculoRepository;
    private final IHistoricoProprietarioRepository historicoProprietarioRepository;
    private final IHistoricoStatusOrdemRepository historicoStatusRepository;
    private final IItemServicoRepository itemServicoRepository;
    private final IItemPecaRepository itemPecaRepository;
    private final IExecucaoServicoTerceirizadoRepository execucaoTerceirizadaRepository;
    private final IPagamentoRepository pagamentoRepository;
    private final StatusOrdemServicoService statusService;
    private final GarantiaService garantiaService;
    private final OrdemServicoValidation validation;
    private final OrdemServicoMapper ordemServicoMapper;
    private final HistoricoStatusOrdemMapper historicoStatusMapper;
    private final ItemServicoMapper itemServicoMapper;
    private final NotificacaoService notificacaoService;

    public OrdemServicoService(IOrdemServicoRepository ordemServicoRepository,
                               IClienteRepository clienteRepository,
                               IVeiculoRepository veiculoRepository,
                               IHistoricoProprietarioRepository historicoProprietarioRepository,
                               IHistoricoStatusOrdemRepository historicoStatusRepository,
                               IItemServicoRepository itemServicoRepository,
                               IItemPecaRepository itemPecaRepository,
                               IExecucaoServicoTerceirizadoRepository execucaoTerceirizadaRepository,
                               IPagamentoRepository pagamentoRepository,
                               StatusOrdemServicoService statusService,
                               GarantiaService garantiaService,
                               OrdemServicoValidation validation,
                               OrdemServicoMapper ordemServicoMapper,
                               HistoricoStatusOrdemMapper historicoStatusMapper,
                               ItemServicoMapper itemServicoMapper,
                               NotificacaoService notificacaoService) {
        this.ordemServicoRepository = ordemServicoRepository;
        this.clienteRepository = clienteRepository;
        this.veiculoRepository = veiculoRepository;
        this.historicoProprietarioRepository = historicoProprietarioRepository;
        this.historicoStatusRepository = historicoStatusRepository;
        this.itemServicoRepository = itemServicoRepository;
        this.itemPecaRepository = itemPecaRepository;
        this.execucaoTerceirizadaRepository = execucaoTerceirizadaRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.statusService = statusService;
        this.garantiaService = garantiaService;
        this.validation = validation;
        this.ordemServicoMapper = ordemServicoMapper;
        this.historicoStatusMapper = historicoStatusMapper;
        this.itemServicoMapper = itemServicoMapper;
        this.notificacaoService = notificacaoService;
    }

    @Transactional
    public OrdemServicoDTO cadastrar(OrdemServicoDTO dto) {
        validation.validateInsert(dto);

        ClienteModel cliente = buscarClienteAtivo(dto.getIdCliente());
        VeiculoModel veiculo = buscarVeiculoAtivo(dto.getIdVeiculo());
        validarVeiculoPertenceAoClienteAtual(cliente.getId(), veiculo.getId());

        OrdemServicoModel ordemServico = ordemServicoMapper.toModel(dto, cliente, veiculo);
        ordemServico.setNumeroOs(gerarProximoNumeroOsSequencial());
        OrdemServicoModel saved = ordemServicoRepository.save(ordemServico);

        StatusOrdemServicoModel statusInicial = statusService.buscarPorFluxo(StatusFluxoOrdemServico.ORCAMENTO);
        historicoStatusRepository.save(historicoStatusMapper.criarHistorico(saved, statusInicial, "Abertura da Ordem de Serviço em orçamento."));

        return montarDetalhe(saved.getId());
    }

    @Transactional
    public OrdemServicoDTO atualizar(Long id, OrdemServicoDTO dto) {
        validation.validateUpdate(id, dto);
        OrdemServicoModel ordemServico = buscarModelAtivo(id);
        impedirAlteracaoSeFinalizada(ordemServico);

        ClienteModel cliente = buscarClienteAtivo(dto.getIdCliente());
        VeiculoModel veiculo = buscarVeiculoAtivo(dto.getIdVeiculo());
        validarVeiculoPertenceAoClienteAtual(cliente.getId(), veiculo.getId());

        ordemServicoMapper.atualizarModel(ordemServico, dto, cliente, veiculo);
        OrdemServicoModel saved = ordemServicoRepository.save(ordemServico);
        return montarDetalhe(saved.getId());
    }

    @Transactional(readOnly = true)
    public OrdemServicoDTO buscar(Long id) {
        validation.validateId(id);
        return montarDetalhe(id);
    }

    @Transactional(readOnly = true)
    public Page<OrdemServicoResumoDTO> listar(Pageable pageable) {
        return ordemServicoRepository.findAllByAtivoTrue(pageable).map(this::montarResumo);
    }

    @Transactional(readOnly = true)
    public Page<OrdemServicoResumoDTO> pesquisar(String termo, Pageable pageable) {
        if (termo == null || termo.isBlank()) {
            return listar(pageable);
        }
        return ordemServicoRepository.search(termo.trim(), pageable).map(this::montarResumo);
    }

    /**
     * Avanço de status solicitado pela tela de Ordens de Serviço.
     *
     * Esta operação manual deve conduzir a OS somente até PAGAMENTO. A etapa
     * FINALIZADO é responsabilidade do módulo Pagamentos, pois depende da
     * validação de quitação financeira e inicia as garantias após a finalização.
     */
    @Transactional
    public OrdemServicoDTO alterarStatusManual(Long id, AlterarStatusOrdemServicoDTO dto) {
        if (dto != null && StatusFluxoOrdemServico.FINALIZADO.equals(dto.getNovoStatus())) {
            throw new RuleValidationException("A OS não pode ser finalizada manualmente nesta tela. Registre o pagamento e, após a quitação, o sistema finalizará a OS automaticamente.");
        }
        return alterarStatus(id, dto);
    }

    /**
     * Conclui o orçamento operacional e encaminha a OS para PAGAMENTO.
     *
     * A tela de Itens da OS é responsável por montar o orçamento com serviços
     * e peças. Quando o orçamento estiver preenchido e aprovado, a OS pode ser
     * enviada ao financeiro sem depender do botão genérico de fluxo da tela
     * principal de Ordens de Serviço.
     */
    @Transactional
    public OrdemServicoDTO enviarOrcamentoParaPagamento(Long id) {
        validation.validateId(id);
        OrdemServicoModel ordemServico = buscarModelAtivo(id);
        HistoricoStatusOrdemModel statusAtual = buscarStatusAtual(ordemServico.getId());

        if (statusAtual == null || statusAtual.getStatusOrdemServico() == null) {
            throw new RuleValidationException("A Ordem de Serviço não possui histórico de status inicial.");
        }

        String statusAtualNome = statusAtual.getStatusOrdemServico().getNomeStatus();
        if (!StatusFluxoOrdemServico.ORCAMENTO.name().equals(statusAtualNome)) {
            throw new RuleValidationException("Somente Ordens de Serviço em ORÇAMENTO podem ser enviadas para PAGAMENTO por esta tela.");
        }

        if (!itemServicoRepository.existsByOrdemServicoIdAndAtivoTrue(ordemServico.getId())) {
            throw new RuleValidationException("Inclua pelo menos um serviço no orçamento antes de enviar a OS para pagamento.");
        }

        recalcularValorTotal(ordemServico.getId());
        ordemServico = buscarModelAtivo(id);
        BigDecimal valorTotal = ordemServico.getValorTotal() == null ? BigDecimal.ZERO : ordemServico.getValorTotal();
        if (valorTotal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuleValidationException("O orçamento deve possuir valor total maior que zero antes de ser enviado para pagamento.");
        }

        if (ordemServico.getDataAprovacao() == null) {
            ordemServico.setDataAprovacao(LocalDateTime.now());
            ordemServicoRepository.save(ordemServico);
        }

        StatusOrdemServicoModel statusPagamento = statusService.buscarPorFluxo(StatusFluxoOrdemServico.PAGAMENTO);
        OrdemServicoModel atualizada = buscarModelAtivo(id);
        historicoStatusRepository.save(historicoStatusMapper.criarHistorico(
                atualizada,
                statusPagamento,
                "Orçamento concluído e enviado para pagamento pelo módulo de Itens da OS."));

        notificacaoService.notificarMudancaStatusOrdemServico(
                atualizada.getNumeroOs(),
                statusPagamento.getNomeStatus(),
                "Orçamento concluído e enviado para pagamento.");

        return montarDetalhe(id);
    }

    @Transactional
    public OrdemServicoDTO alterarStatus(Long id, AlterarStatusOrdemServicoDTO dto) {
        validation.validateId(id);
        OrdemServicoModel ordemServico = buscarModelAtivo(id);
        HistoricoStatusOrdemModel statusAtual = buscarStatusAtual(ordemServico.getId());
        if (dto == null || dto.getNovoStatus() == null) {
            throw new RuleValidationException("O novo status da Ordem de Serviço é obrigatório.");
        }
        StatusOrdemServicoModel novoStatus = statusService.buscarPorFluxo(dto.getNovoStatus());

        validation.validateStatusChange(dto, statusAtual, novoStatus);
        recalcularValorTotal(ordemServico.getId());
        ordemServico = buscarModelAtivo(id);
        validarPreCondicoesDoFluxo(ordemServico, novoStatus);
        aplicarEfeitosDoStatus(ordemServico, novoStatus);

        OrdemServicoModel atualizada = buscarModelAtivo(id);
        historicoStatusRepository.save(historicoStatusMapper.criarHistorico(atualizada, novoStatus, dto.getObservacao()));

        /*
         * PADRÃO DE PROJETO: DECORATOR
         * Aplicação: toda alteração de status da OS gera uma notificação interna.
         * A notificação base é envolvida por NotificadorAuditoriaDecorator,
         * adicionando auditoria sem alterar o componente operacional.
         */
        notificacaoService.notificarMudancaStatusOrdemServico(
                atualizada.getNumeroOs(),
                novoStatus.getNomeStatus(),
                dto.getObservacao());

        return montarDetalhe(id);
    }

    @Transactional
    public void inativar(Long id) {
        validation.validateId(id);
        OrdemServicoModel ordemServico = buscarModelAtivo(id);
        impedirAlteracaoSeFinalizada(ordemServico);
        ordemServico.setAtivo(Boolean.FALSE);
        ordemServicoRepository.save(ordemServico);
    }

    @Transactional
    public void recalcularValorTotal(Long idOrdemServico) {
        OrdemServicoModel ordemServico = buscarModelAtivo(idOrdemServico);

        BigDecimal totalServicos = itemServicoRepository.findByOrdemServicoIdAndAtivoTrue(idOrdemServico)
                .stream()
                .map(item -> item.getValorTotal() == null ? BigDecimal.ZERO : item.getValorTotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPecas = itemPecaRepository.findByIdOrdemServicoAndAtivoTrue(idOrdemServico)
                .stream()
                .map(item -> item.getValorTotal() == null ? BigDecimal.ZERO : item.getValorTotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        ordemServico.setValorTotal(totalServicos.add(totalPecas));
        ordemServicoRepository.save(ordemServico);
    }

    public OrdemServicoModel buscarModelAtivo(Long id) {
        return ordemServicoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Ordem de Serviço não encontrada ou inativa."));
    }

    public void validarOrdemNaoFinalizada(Long idOrdemServico) {
        OrdemServicoModel ordemServico = buscarModelAtivo(idOrdemServico);
        impedirAlteracaoSeFinalizada(ordemServico);
    }

    public void validarOrdemEmOrcamento(Long idOrdemServico) {
        OrdemServicoModel ordemServico = buscarModelAtivo(idOrdemServico);
        HistoricoStatusOrdemModel statusAtual = buscarStatusAtualOuNulo(ordemServico.getId());
        if (statusAtual == null
                || statusAtual.getStatusOrdemServico() == null
                || !StatusFluxoOrdemServico.ORCAMENTO.name().equals(statusAtual.getStatusOrdemServico().getNomeStatus())) {
            throw new RuleValidationException("Serviços e peças só podem ser alterados enquanto a Ordem de Serviço está em ORÇAMENTO.");
        }
    }

    private OrdemServicoDTO montarDetalhe(Long id) {
        OrdemServicoModel ordemServico = buscarModelAtivo(id);
        HistoricoStatusOrdemModel statusAtual = buscarStatusAtualOuNulo(id);
        List<HistoricoStatusOrdemDTO> historicoStatus = historicoStatusRepository
                .findByOrdemServicoIdAndAtivoTrueOrderByDataStatusAsc(id)
                .stream()
                .map(historicoStatusMapper::toDto)
                .toList();
        List<ItemServicoDTO> itensServico = itemServicoRepository
                .findByOrdemServicoIdAndAtivoTrue(id)
                .stream()
                .map(item -> itemServicoMapper.toDto(item, buscarExecucaoTerceirizadaOuNula(item.getId())))
                .toList();

        return ordemServicoMapper.toDetalheDto(ordemServico, statusAtual, historicoStatus, itensServico);
    }

    private OrdemServicoResumoDTO montarResumo(OrdemServicoModel ordemServico) {
        return ordemServicoMapper.toResumoDto(ordemServico, buscarStatusAtualOuNulo(ordemServico.getId()));
    }

    private ClienteModel buscarClienteAtivo(Long id) {
        return clienteRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado ou inativo."));
    }

    private VeiculoModel buscarVeiculoAtivo(Long id) {
        return veiculoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Veículo não encontrado ou inativo."));
    }

    private void validarVeiculoPertenceAoClienteAtual(Long idCliente, Long idVeiculo) {
        historicoProprietarioRepository.findFirstByVeiculoIdAndAtivoTrueAndProprietarioAtualTrue(idVeiculo)
                .filter(historico -> historico.getCliente() != null
                        && historico.getCliente().getId() != null
                        && historico.getCliente().getId().equals(idCliente))
                .orElseThrow(() -> new RuleValidationException("O veículo selecionado não pertence ao cliente informado como proprietário atual."));
    }

    private HistoricoStatusOrdemModel buscarStatusAtual(Long idOrdemServico) {
        return buscarStatusAtualOuNulo(idOrdemServico);
    }

    private HistoricoStatusOrdemModel buscarStatusAtualOuNulo(Long idOrdemServico) {
        List<HistoricoStatusOrdemModel> historico = historicoStatusRepository.findHistoricoFluxoDesc(idOrdemServico);
        return historico.isEmpty() ? null : historico.get(0);
    }

    private ExecucaoServicoTerceirizadoModel buscarExecucaoTerceirizadaOuNula(Long idItemServico) {
        return execucaoTerceirizadaRepository.findByItemServicoIdAndAtivoTrue(idItemServico).orElse(null);
    }

    private void validarPreCondicoesDoFluxo(OrdemServicoModel ordemServico, StatusOrdemServicoModel novoStatus) {
        if (StatusFluxoOrdemServico.EXECUCAO.name().equals(novoStatus.getNomeStatus())
                && !itemServicoRepository.existsByOrdemServicoIdAndAtivoTrue(ordemServico.getId())) {
            throw new RuleValidationException("A Ordem de Serviço deve possuir pelo menos um Item de Serviço antes de entrar em execução.");
        }

        if (StatusFluxoOrdemServico.FINALIZADO.name().equals(novoStatus.getNomeStatus())) {
            BigDecimal valorTotal = ordemServico.getValorTotal() == null ? BigDecimal.ZERO : ordemServico.getValorTotal();
            BigDecimal valorPago = pagamentoRepository.somarValorPorStatus(ordemServico.getId(), StatusPagamento.PAGO);
            valorPago = valorPago == null ? BigDecimal.ZERO : valorPago;
            if (valorPago.compareTo(valorTotal) < 0) {
                throw new RuleValidationException("A Ordem de Serviço só pode ser finalizada quando o valor pago for igual ou superior ao valor total da OS.");
            }
        }
    }

    private void aplicarEfeitosDoStatus(OrdemServicoModel ordemServico, StatusOrdemServicoModel novoStatus) {
        if (StatusFluxoOrdemServico.EXECUCAO.name().equals(novoStatus.getNomeStatus())
                && ordemServico.getDataAprovacao() == null) {
            ordemServico.setDataAprovacao(LocalDateTime.now());
        }

        if (StatusFluxoOrdemServico.FINALIZADO.name().equals(novoStatus.getNomeStatus())) {
            ordemServico.setDataFinalizacao(LocalDateTime.now());
        }
        OrdemServicoModel saved = ordemServicoRepository.save(ordemServico);
        if (StatusFluxoOrdemServico.FINALIZADO.name().equals(novoStatus.getNomeStatus())) {
            garantiaService.iniciarGarantiasDaOrdem(saved.getId(), saved.getDataFinalizacao().toLocalDate());
        }
    }

    private void impedirAlteracaoSeFinalizada(OrdemServicoModel ordemServico) {
        HistoricoStatusOrdemModel statusAtual = buscarStatusAtualOuNulo(ordemServico.getId());
        if (statusAtual != null
                && StatusFluxoOrdemServico.FINALIZADO.name().equals(statusAtual.getStatusOrdemServico().getNomeStatus())) {
            throw new RuleValidationException("Ordem de Serviço finalizada não pode ser alterada ou inativada.");
        }
    }

    /**
     * Gera numeração sequencial da Ordem de Serviço.
     *
     * A numeração considera todas as OS registradas na tabela, inclusive as
     * inativas. Assim, se a OS 2 for inativada, nenhuma nova OS assumirá o
     * número 2, preservando histórico, rastreabilidade e integridade documental.
     */
    private String gerarProximoNumeroOsSequencial() {
        long proximoNumero = ordemServicoRepository.buscarMaiorNumeroOsNumerico() + 1L;
        String numero = String.valueOf(proximoNumero);
        while (ordemServicoRepository.existsByNumeroOsIgnoreCase(numero)) {
            proximoNumero++;
            numero = String.valueOf(proximoNumero);
        }
        return numero;
    }
}
