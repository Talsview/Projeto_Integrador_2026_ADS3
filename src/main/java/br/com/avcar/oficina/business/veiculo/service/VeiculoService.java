package br.com.avcar.oficina.business.veiculo.service;

import br.com.avcar.oficina.business.pessoa.model.ClienteModel;
import br.com.avcar.oficina.business.pessoa.repository.IClienteRepository;
import br.com.avcar.oficina.business.veiculo.adapter.VeiculoResponseAdapter;
import br.com.avcar.oficina.business.veiculo.dto.TransferenciaProprietarioDTO;
import br.com.avcar.oficina.business.veiculo.dto.VeiculoDTO;
import br.com.avcar.oficina.business.veiculo.dto.VeiculoResumoDTO;
import br.com.avcar.oficina.business.veiculo.mapper.HistoricoProprietarioMapper;
import br.com.avcar.oficina.business.veiculo.mapper.VeiculoMapper;
import br.com.avcar.oficina.business.veiculo.model.HistoricoProprietarioModel;
import br.com.avcar.oficina.business.veiculo.model.ModeloModel;
import br.com.avcar.oficina.business.veiculo.model.VeiculoModel;
import br.com.avcar.oficina.business.veiculo.repository.IHistoricoProprietarioRepository;
import br.com.avcar.oficina.business.veiculo.repository.IModeloRepository;
import br.com.avcar.oficina.business.veiculo.repository.IVeiculoRepository;
import br.com.avcar.oficina.business.veiculo.validation.VeiculoValidation;
import br.com.avcar.oficina.core.exception.BusinessException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service do módulo Veículo.
 *
 * Regras atendidas:
 * - Veículo pertence a um Modelo, que pertence a uma Marca.
 * - Veículo não armazena proprietário fixo; a posse é controlada por HistoricoProprietario.
 * - Todo veículo cadastrado recebe um histórico inicial de proprietário.
 * - A transferência encerra a posse atual e cria novo histórico, preservando rastreabilidade.
 */
@Service
public class VeiculoService {

    private final IVeiculoRepository veiculoRepository;
    private final IModeloRepository modeloRepository;
    private final IClienteRepository clienteRepository;
    private final IHistoricoProprietarioRepository historicoRepository;
    private final VeiculoValidation validation;
    private final VeiculoMapper veiculoMapper;
    private final HistoricoProprietarioMapper historicoMapper;
    private final VeiculoResponseAdapter responseAdapter;

    public VeiculoService(IVeiculoRepository veiculoRepository,
                          IModeloRepository modeloRepository,
                          IClienteRepository clienteRepository,
                          IHistoricoProprietarioRepository historicoRepository,
                          VeiculoValidation validation,
                          VeiculoMapper veiculoMapper,
                          HistoricoProprietarioMapper historicoMapper,
                          VeiculoResponseAdapter responseAdapter) {
        this.veiculoRepository = veiculoRepository;
        this.modeloRepository = modeloRepository;
        this.clienteRepository = clienteRepository;
        this.historicoRepository = historicoRepository;
        this.validation = validation;
        this.veiculoMapper = veiculoMapper;
        this.historicoMapper = historicoMapper;
        this.responseAdapter = responseAdapter;
    }

    @Transactional
    public VeiculoDTO cadastrar(VeiculoDTO dto) {
        validation.validateInsert(dto);

        ModeloModel modelo = buscarModeloAtivo(dto.getModeloId());
        ClienteModel proprietario = buscarClienteAtivo(dto.getProprietarioAtualId());

        VeiculoModel veiculo = veiculoMapper.toModel(dto, modelo);
        VeiculoModel veiculoSalvo = veiculoRepository.save(veiculo);

        HistoricoProprietarioModel historicoInicial = historicoMapper.criarHistoricoInicial(veiculoSalvo, proprietario, dto);
        historicoRepository.save(historicoInicial);

        return montarDetalhe(veiculoSalvo.getId());
    }

    @Transactional
    public VeiculoDTO atualizar(Long id, VeiculoDTO dto) {
        validation.validateUpdate(id, dto);

        VeiculoModel veiculo = buscarVeiculoAtivo(id);
        ModeloModel modelo = buscarModeloAtivo(dto.getModeloId());

        veiculoMapper.atualizarCampos(veiculo, dto, modelo);
        veiculoRepository.save(veiculo);

        return montarDetalhe(id);
    }

    @Transactional(readOnly = true)
    public VeiculoDTO buscar(Long id) {
        validation.validateId(id);
        return montarDetalhe(id);
    }

    @Transactional(readOnly = true)
    public Page<VeiculoResumoDTO> listar(Pageable pageable) {
        return veiculoRepository.findAllByAtivoTrue(pageable)
                .map(this::montarResumo);
    }

    @Transactional(readOnly = true)
    public Page<VeiculoResumoDTO> pesquisar(String termo, Pageable pageable) {
        if (termo == null || termo.isBlank()) {
            return listar(pageable);
        }
        return veiculoRepository.search(normalizeTermoPesquisa(termo), pageable)
                .map(this::montarResumo);
    }

    @Transactional
    public VeiculoDTO transferirProprietario(Long veiculoId, TransferenciaProprietarioDTO dto) {
        VeiculoModel veiculo = buscarVeiculoAtivo(veiculoId);
        HistoricoProprietarioModel proprietarioAtual = buscarHistoricoAtualOuNulo(veiculoId);

        validation.validateTransferencia(veiculoId, dto, proprietarioAtual);

        ClienteModel novoCliente = buscarClienteAtivo(dto.getNovoClienteId());
        LocalDate dataInicioNovaPosse = dto.getDataInicioPosse() == null ? LocalDate.now() : dto.getDataInicioPosse();

        if (proprietarioAtual != null) {
            proprietarioAtual.setProprietarioAtual(Boolean.FALSE);
            proprietarioAtual.setDataFimPosse(dataInicioNovaPosse);
            historicoRepository.saveAndFlush(proprietarioAtual);
        }

        HistoricoProprietarioModel novoHistorico = historicoMapper.criarNovoHistorico(veiculo, novoCliente, dto);
        historicoRepository.save(novoHistorico);

        return montarDetalhe(veiculoId);
    }

    @Transactional
    public void inativar(Long id) {
        validation.validateId(id);
        VeiculoModel veiculo = buscarVeiculoAtivo(id);
        veiculo.setAtivo(Boolean.FALSE);
        veiculoRepository.save(veiculo);
    }

    private VeiculoDTO montarDetalhe(Long id) {
        VeiculoModel veiculo = buscarVeiculoAtivo(id);
        HistoricoProprietarioModel proprietarioAtual = buscarHistoricoAtualOuNulo(id);
        List<HistoricoProprietarioModel> historico = historicoRepository.findByVeiculoIdAndAtivoTrueOrderByDataInicioPosseDesc(id);
        return responseAdapter.adaptarParaDetalhe(veiculo, proprietarioAtual, historico);
    }

    private VeiculoResumoDTO montarResumo(VeiculoModel veiculo) {
        HistoricoProprietarioModel proprietarioAtual = buscarHistoricoAtualOuNulo(veiculo.getId());
        return responseAdapter.adaptarParaResumo(veiculo, proprietarioAtual);
    }

    private VeiculoModel buscarVeiculoAtivo(Long id) {
        return veiculoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Veículo não encontrado ou inativo."));
    }

    private ModeloModel buscarModeloAtivo(Long id) {
        return modeloRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Modelo não encontrado ou inativo."));
    }

    private ClienteModel buscarClienteAtivo(Long id) {
        return clienteRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado ou inativo."));
    }

    private HistoricoProprietarioModel buscarHistoricoAtualOuNulo(Long veiculoId) {
        return historicoRepository.findFirstByVeiculoIdAndAtivoTrueAndProprietarioAtualTrue(veiculoId)
                .orElse(null);
    }

    private String normalizeTermoPesquisa(String termo) {
        String trimmed = termo.trim();
        String apenasPlaca = veiculoMapper.normalizePlaca(trimmed);
        if (apenasPlaca != null && apenasPlaca.length() >= 7 && apenasPlaca.length() <= 8) {
            return apenasPlaca;
        }
        return trimmed;
    }
}
