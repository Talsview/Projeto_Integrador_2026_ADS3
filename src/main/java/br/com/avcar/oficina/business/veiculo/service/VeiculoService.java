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

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
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
    /**
     * Função: Valida os dados recebidos, monta as entidades necessárias e grava o cadastro de veiculo.
     * Uso no sistema: centraliza a regra de cadastro na camada Service, mantendo Controller e tela
     * mais simples.
     */
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
    /**
     * Função: Busca o registro ativo, aplica as alterações permitidas e salva a atualização de
     * veiculo.
     * Uso no sistema: garante que alterações passem por validação e não quebrem vínculos já existentes
     * no sistema.
     */
    public VeiculoDTO atualizar(Long id, VeiculoDTO dto) {
        validation.validateUpdate(id, dto);

        VeiculoModel veiculo = buscarVeiculoAtivo(id);
        ModeloModel modelo = buscarModeloAtivo(dto.getModeloId());

        veiculoMapper.atualizarCampos(veiculo, dto, modelo);
        veiculoRepository.save(veiculo);

        return montarDetalhe(id);
    }

    @Transactional(readOnly = true)
    /**
     * Função: Localiza informações de veiculo conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    public VeiculoDTO buscar(Long id) {
        validation.validateId(id);
        return montarDetalhe(id);
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de veiculo aplicando filtros, paginação ou critérios de busca quando
     * informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<VeiculoResumoDTO> listar(Pageable pageable) {
        return veiculoRepository.findAllByAtivoTrue(pageable)
                .map(this::montarResumo);
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de veiculo aplicando filtros, paginação ou critérios de busca quando
     * informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<VeiculoResumoDTO> pesquisar(String termo, Pageable pageable) {
        if (termo == null || termo.isBlank()) {
            return listar(pageable);
        }
        return veiculoRepository.search(normalizeTermoPesquisa(termo), pageable)
                .map(this::montarResumo);
    }

    @Transactional
    /**
     * Função: Encerra o vínculo anterior e registra novo proprietário para o veículo.
     * Uso no sistema: preserva o histórico de proprietários ao longo do tempo.
     */
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
    /**
     * Função: Localiza um registro inativado, altera seu campo ativo para verdadeiro e salva a
     * reativação.
     * Uso no sistema: permite recuperar cadastros feitos anteriormente sem duplicar clientes,
     * veículos, peças ou serviços.
     */
    public void inativar(Long id) {
        validation.validateId(id);
        VeiculoModel veiculo = buscarVeiculoAtivo(id);
        veiculo.setAtivo(Boolean.FALSE);
        veiculoRepository.save(veiculo);
    }



    @Transactional(readOnly = true)
    /**
     * Função: Lista cadastros inativados para que o usuário possa localizar e reativar registros sem
     * recriá-los.
     * Uso no sistema: reforça a rastreabilidade, pois registros antigos continuam no banco e podem
     * voltar a ficar ativos.
     */
    public Page<VeiculoResumoDTO> listarInativos(Pageable pageable) {
        return veiculoRepository.findAllByAtivoFalse(pageable)
                .map(this::montarResumo);
    }

    @Transactional
    /**
     * Função: Localiza um registro inativado, altera seu campo ativo para verdadeiro e salva a
     * reativação.
     * Uso no sistema: permite recuperar cadastros feitos anteriormente sem duplicar clientes,
     * veículos, peças ou serviços.
     */
    public VeiculoResumoDTO ativar(Long id) {
        validation.validateId(id);
        VeiculoModel veiculo = veiculoRepository.findByIdAndAtivoFalse(id)
                .orElseThrow(() -> new BusinessException("Veículo não encontrado entre os inativos."));
        veiculo.setAtivo(Boolean.TRUE);
        VeiculoModel saved = veiculoRepository.save(veiculo);
        return montarResumo(saved);
    }

    /**
     * Função: Monta o objeto ou resposta necessária para a operação montar detalhe.
     * Uso no sistema: isola a preparação dos dados e melhora a legibilidade do fluxo principal.
     */
    private VeiculoDTO montarDetalhe(Long id) {
        VeiculoModel veiculo = buscarVeiculoAtivo(id);
        HistoricoProprietarioModel proprietarioAtual = buscarHistoricoAtualOuNulo(id);
        List<HistoricoProprietarioModel> historico = historicoRepository.findByVeiculoIdAndAtivoTrueOrderByDataInicioPosseDesc(id);
        return responseAdapter.adaptarParaDetalhe(veiculo, proprietarioAtual, historico);
    }

    /**
     * Função: Monta o objeto ou resposta necessária para a operação montar resumo.
     * Uso no sistema: isola a preparação dos dados e melhora a legibilidade do fluxo principal.
     */
    private VeiculoResumoDTO montarResumo(VeiculoModel veiculo) {
        HistoricoProprietarioModel proprietarioAtual = buscarHistoricoAtualOuNulo(veiculo.getId());
        return responseAdapter.adaptarParaResumo(veiculo, proprietarioAtual);
    }

    /**
     * Função: Localiza informações de veiculo conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    private VeiculoModel buscarVeiculoAtivo(Long id) {
        return veiculoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Veículo não encontrado ou inativo."));
    }

    /**
     * Função: Localiza informações de veiculo conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    private ModeloModel buscarModeloAtivo(Long id) {
        return modeloRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Modelo não encontrado ou inativo."));
    }

    /**
     * Função: Localiza informações de veiculo conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    private ClienteModel buscarClienteAtivo(Long id) {
        return clienteRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado ou inativo."));
    }

    /**
     * Função: Localiza informações de veiculo conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    private HistoricoProprietarioModel buscarHistoricoAtualOuNulo(Long veiculoId) {
        return historicoRepository.findFirstByVeiculoIdAndAtivoTrueAndProprietarioAtualTrue(veiculoId)
                .orElse(null);
    }

    /**
     * Função: Normaliza o termo digitado pelo usuário antes de pesquisar.
     * Uso no sistema: melhora a busca por nome, documento, placa ou código independentemente de
     * acentos e letras maiúsculas.
     */
    private String normalizeTermoPesquisa(String termo) {
        String trimmed = termo.trim();
        String apenasPlaca = veiculoMapper.normalizePlaca(trimmed);
        if (apenasPlaca != null && apenasPlaca.length() >= 7 && apenasPlaca.length() <= 8) {
            return apenasPlaca;
        }
        return trimmed;
    }
}
