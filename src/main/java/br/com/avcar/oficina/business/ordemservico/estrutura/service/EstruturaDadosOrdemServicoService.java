package br.com.avcar.oficina.business.ordemservico.estrutura.service;

import br.com.avcar.oficina.business.ordemservico.dto.OrdemServicoResumoDTO;
import br.com.avcar.oficina.business.ordemservico.enums.StatusFluxoOrdemServico;
import br.com.avcar.oficina.business.ordemservico.estrutura.dto.CalculoRecursivoTotalOSDTO;
import br.com.avcar.oficina.business.ordemservico.estrutura.dto.FilaAtendimentoOrdemServicoDTO;
import br.com.avcar.oficina.business.ordemservico.estrutura.dto.ResultadoOrdenacaoOrdemServicoDTO;
import br.com.avcar.oficina.business.ordemservico.estrutura.dto.ResultadoPesquisaOrdemServicoDTO;
import br.com.avcar.oficina.business.ordemservico.estrutura.enums.CriterioOrdenacaoOrdemServico;
import br.com.avcar.oficina.business.ordemservico.estrutura.ordenacao.OrdenadorOrdemServicoPorDataAbertura;
import br.com.avcar.oficina.business.ordemservico.estrutura.ordenacao.OrdenadorOrdemServicoPorPrioridade;
import br.com.avcar.oficina.business.ordemservico.estrutura.ordenacao.OrdenadorOrdemServicoPorValorTotal;
import br.com.avcar.oficina.business.ordemservico.mapper.OrdemServicoMapper;
import br.com.avcar.oficina.business.ordemservico.model.HistoricoStatusOrdemModel;
import br.com.avcar.oficina.business.ordemservico.model.ItemServicoModel;
import br.com.avcar.oficina.business.ordemservico.model.OrdemServicoModel;
import br.com.avcar.oficina.business.ordemservico.repository.IHistoricoStatusOrdemRepository;
import br.com.avcar.oficina.business.ordemservico.repository.IItemServicoRepository;
import br.com.avcar.oficina.business.ordemservico.repository.IOrdemServicoRepository;
import br.com.avcar.oficina.business.peca.model.ItemPecaModel;
import br.com.avcar.oficina.business.peca.repository.IItemPecaRepository;
import br.com.avcar.oficina.core.estrutura.fila.FilaAtendimento;
import br.com.avcar.oficina.core.estrutura.iterator.OficinaIterator;
import br.com.avcar.oficina.core.estrutura.lista.ListaLinearBusca;
import br.com.avcar.oficina.core.estrutura.ordenacao.OrdenadorTemplate;
import br.com.avcar.oficina.core.exception.BusinessException;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service acadêmico responsável por reunir as funcionalidades de Estrutura de
 * Dados I aplicadas ao controle de Ordens de Serviço da oficina.
 */
@Service
public class EstruturaDadosOrdemServicoService {

    private final IOrdemServicoRepository ordemServicoRepository;
    private final IHistoricoStatusOrdemRepository historicoStatusRepository;
    private final IItemServicoRepository itemServicoRepository;
    private final IItemPecaRepository itemPecaRepository;
    private final OrdemServicoMapper ordemServicoMapper;

    public EstruturaDadosOrdemServicoService(IOrdemServicoRepository ordemServicoRepository,
                                             IHistoricoStatusOrdemRepository historicoStatusRepository,
                                             IItemServicoRepository itemServicoRepository,
                                             IItemPecaRepository itemPecaRepository,
                                             OrdemServicoMapper ordemServicoMapper) {
        this.ordemServicoRepository = ordemServicoRepository;
        this.historicoStatusRepository = historicoStatusRepository;
        this.itemServicoRepository = itemServicoRepository;
        this.itemPecaRepository = itemPecaRepository;
        this.ordemServicoMapper = ordemServicoMapper;
    }

    /**
     * Estrutura de Dados Linear: Fila.
     *
     * Regra aplicada: Ordens de Serviço em EXECUCAO ficam na fila de
     * atendimento operacional. Elas representam orçamentos já montados e
     * aprovados para execução, antes da etapa financeira de pagamento.
     */
    @Transactional(readOnly = true)
    public FilaAtendimentoOrdemServicoDTO montarFilaAtendimento() {
        List<OrdemServicoResumoDTO> resumos = carregarResumosAtivos();
        OrdenadorTemplate<OrdemServicoResumoDTO> ordenador = new OrdenadorOrdemServicoPorDataAbertura();
        List<OrdemServicoResumoDTO> ordenadasPorChegada = ordenador.ordenar(resumos);

        FilaAtendimento<OrdemServicoResumoDTO> fila = new FilaAtendimento<>();
        for (OrdemServicoResumoDTO resumo : ordenadasPorChegada) {
            if (StatusFluxoOrdemServico.EXECUCAO.name().equals(normalizarStatus(resumo.getStatusAtual()))) {
                fila.enfileirar(resumo);
            }
        }

        List<OrdemServicoResumoDTO> saida = new ArrayList<>();
        OficinaIterator<OrdemServicoResumoDTO> iterator = fila.iterator();
        while (iterator.hasNext()) {
            saida.add(iterator.next());
        }

        FilaAtendimentoOrdemServicoDTO dto = new FilaAtendimentoOrdemServicoDTO();
        dto.setEstruturaUtilizada("Fila encadeada de atendimento de Ordens de Serviço");
        dto.setJustificativa("A fila exibe apenas OS em EXECUÇÃO, ou seja, orçamentos já definidos que aguardam execução do serviço antes de seguir para pagamento.");
        dto.setQuantidadeNaFila(fila.tamanho());
        dto.setOrdens(saida);
        return dto;
    }

    /**
     * Algoritmo de ordenação manual com Template Method.
     */
    @Transactional(readOnly = true)
    public ResultadoOrdenacaoOrdemServicoDTO ordenar(CriterioOrdenacaoOrdemServico criterio) {
        CriterioOrdenacaoOrdemServico criterioEfetivo = criterio == null
                ? CriterioOrdenacaoOrdemServico.DATA_ABERTURA
                : criterio;

        OrdenadorTemplate<OrdemServicoResumoDTO> ordenador = selecionarOrdenador(criterioEfetivo);
        List<OrdemServicoResumoDTO> ordenadas = ordenador.ordenar(carregarResumosAtivos());

        ResultadoOrdenacaoOrdemServicoDTO dto = new ResultadoOrdenacaoOrdemServicoDTO();
        dto.setAlgoritmoUtilizado("Insertion Sort implementado manualmente");
        dto.setPadraoProjetoAplicado("Template Method");
        dto.setCriterio(criterioEfetivo);
        dto.setQuantidadeOrdenada(ordenadas.size());
        dto.setOrdens(ordenadas);
        return dto;
    }

    /**
     * Pesquisa manual usando lista linear encadeada e Iterator.
     */
    @Transactional(readOnly = true)
    public ResultadoPesquisaOrdemServicoDTO pesquisarLinear(String termo) {
        String termoNormalizado = normalizarTexto(termo);
        ListaLinearBusca<OrdemServicoResumoDTO> lista = new ListaLinearBusca<>();

        for (OrdemServicoResumoDTO resumo : carregarResumosAtivos()) {
            lista.adicionar(resumo);
        }

        List<OrdemServicoResumoDTO> encontrados = lista.buscarTodos(resumo -> correspondeAoTermo(resumo, termoNormalizado));

        ResultadoPesquisaOrdemServicoDTO dto = new ResultadoPesquisaOrdemServicoDTO();
        dto.setEstruturaUtilizada("Lista encadeada simples com Iterator");
        dto.setAlgoritmoUtilizado("Busca linear manual por número da OS, placa, cliente, veículo, prioridade ou status");
        dto.setTermoPesquisado(termo);
        dto.setQuantidadeEncontrada(encontrados.size());
        dto.setResultados(encontrados);
        return dto;
    }

    /**
     * Função recursiva aplicada ao cálculo total da OS.
     */
    @Transactional(readOnly = true)
    public CalculoRecursivoTotalOSDTO calcularTotalRecursivo(Long idOrdemServico) {
        OrdemServicoModel ordemServico = ordemServicoRepository.findByIdAndAtivoTrue(idOrdemServico)
                .orElseThrow(() -> new BusinessException("Ordem de Serviço não encontrada ou inativa."));

        List<ItemServicoModel> itensServico = itemServicoRepository.findByOrdemServicoIdAndAtivoTrue(idOrdemServico);
        List<ItemPecaModel> itensPeca = itemPecaRepository.findByIdOrdemServicoAndAtivoTrue(idOrdemServico);

        CalculadoraRecursivaTotalOrdemServico calculadora = new CalculadoraRecursivaTotalOrdemServico();
        BigDecimal totalServicos = calculadora.somarServicos(itensServico);
        BigDecimal totalPecas = calculadora.somarPecas(itensPeca);

        CalculoRecursivoTotalOSDTO dto = new CalculoRecursivoTotalOSDTO();
        dto.setIdOrdemServico(ordemServico.getId());
        dto.setNumeroOs(ordemServico.getNumeroOs());
        dto.setQuantidadeItensServico(itensServico.size());
        dto.setQuantidadeItensPeca(itensPeca.size());
        dto.setTotalServicos(totalServicos);
        dto.setTotalPecas(totalPecas);
        dto.setTotalGeral(totalServicos.add(totalPecas));
        dto.setFuncaoUtilizada("Soma recursiva dos itens de serviço e dos itens de peça");
        dto.setJustificativa("A recursividade foi aplicada ao cálculo total da OS por ser uma operação de agregação sobre uma sequência de itens.");
        return dto;
    }

    private List<OrdemServicoResumoDTO> carregarResumosAtivos() {
        return ordemServicoRepository.findAllByAtivoTrue(Pageable.unpaged())
                .getContent()
                .stream()
                .map(this::montarResumo)
                .toList();
    }

    private OrdemServicoResumoDTO montarResumo(OrdemServicoModel ordemServico) {
        HistoricoStatusOrdemModel statusAtual = historicoStatusRepository
                .findHistoricoFluxoDesc(ordemServico.getId())
                .stream()
                .findFirst()
                .orElse(null);
        return ordemServicoMapper.toResumoDto(ordemServico, statusAtual);
    }

    private OrdenadorTemplate<OrdemServicoResumoDTO> selecionarOrdenador(CriterioOrdenacaoOrdemServico criterio) {
        return switch (criterio) {
            case VALOR_TOTAL -> new OrdenadorOrdemServicoPorValorTotal();
            case PRIORIDADE -> new OrdenadorOrdemServicoPorPrioridade();
            case DATA_ABERTURA -> new OrdenadorOrdemServicoPorDataAbertura();
        };
    }

    private boolean correspondeAoTermo(OrdemServicoResumoDTO resumo, String termoNormalizado) {
        if (termoNormalizado == null || termoNormalizado.isBlank()) {
            return true;
        }
        return contem(resumo.getNumeroOs(), termoNormalizado)
                || contem(resumo.getNomeCliente(), termoNormalizado)
                || contem(resumo.getPlacaVeiculo(), termoNormalizado)
                || contem(resumo.getDescricaoVeiculo(), termoNormalizado)
                || contem(resumo.getStatusAtual(), termoNormalizado)
                || contem(resumo.getPrioridade() == null ? null : resumo.getPrioridade().name(), termoNormalizado);
    }

    private boolean contem(String valor, String termoNormalizado) {
        return normalizarTexto(valor).contains(termoNormalizado);
    }

    private String normalizarStatus(String status) {
        return status == null ? null : status.trim().toUpperCase();
    }

    private String normalizarTexto(String valor) {
        if (valor == null) {
            return "";
        }
        String semAcento = Normalizer.normalize(valor, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return semAcento.trim().toLowerCase();
    }
}
