package br.com.avcar.oficina.business.veiculo.service;

import br.com.avcar.oficina.business.veiculo.dto.ModeloDTO;
import br.com.avcar.oficina.business.veiculo.mapper.ModeloMapper;
import br.com.avcar.oficina.business.veiculo.model.MarcaModel;
import br.com.avcar.oficina.business.veiculo.model.ModeloModel;
import br.com.avcar.oficina.business.veiculo.repository.IMarcaRepository;
import br.com.avcar.oficina.business.veiculo.repository.IModeloRepository;
import br.com.avcar.oficina.business.veiculo.validation.ModeloValidation;
import br.com.avcar.oficina.core.exception.BusinessException;
import br.com.avcar.oficina.core.service.GenericService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsável pelas regras de negócio de Modelo.
 */
@Service
public class ModeloService extends GenericService<ModeloModel> {

    private final IMarcaRepository marcaRepository;
    private final IModeloRepository modeloRepository;
    private final ModeloValidation validation;
    private final ModeloMapper mapper;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public ModeloService(IMarcaRepository marcaRepository,
                         IModeloRepository modeloRepository,
                         ModeloValidation validation,
                         ModeloMapper mapper) {
        super(modeloRepository, null);
        this.marcaRepository = marcaRepository;
        this.modeloRepository = modeloRepository;
        this.validation = validation;
        this.mapper = mapper;
    }

    @Transactional
    /**
     * Função: Valida os dados recebidos, monta as entidades necessárias e grava o cadastro de veiculo.
     * Uso no sistema: centraliza a regra de cadastro na camada Service, mantendo Controller e tela
     * mais simples.
     */
    public ModeloDTO cadastrar(ModeloDTO dto) {
        validation.validateInsert(dto);
        MarcaModel marca = buscarMarcaAtiva(dto.getMarcaId());
        ModeloModel saved = modeloRepository.save(mapper.toModel(dto, marca));
        return mapper.toDto(saved);
    }

    @Transactional
    /**
     * Função: Busca o registro ativo, aplica as alterações permitidas e salva a atualização de
     * veiculo.
     * Uso no sistema: garante que alterações passem por validação e não quebrem vínculos já existentes
     * no sistema.
     */
    public ModeloDTO atualizar(Long id, ModeloDTO dto) {
        validation.validateUpdate(id, dto);
        ModeloModel modelo = buscarModelAtivo(id);
        MarcaModel marca = buscarMarcaAtiva(dto.getMarcaId());
        mapper.atualizarModel(modelo, dto, marca);
        return mapper.toDto(modeloRepository.save(modelo));
    }

    @Transactional(readOnly = true)
    /**
     * Função: Localiza informações de veiculo conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    public ModeloDTO buscar(Long id) {
        validation.validateId(id);
        return mapper.toDto(buscarModelAtivo(id));
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de veiculo aplicando filtros, paginação ou critérios de busca quando
     * informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<ModeloDTO> listar(Pageable pageable) {
        return modeloRepository.findAllByAtivoTrue(pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de veiculo aplicando filtros, paginação ou critérios de busca quando
     * informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<ModeloDTO> listarPorMarca(Long marcaId, Pageable pageable) {
        validation.validateMarca(marcaId);
        return modeloRepository.findByMarcaIdAndAtivoTrue(marcaId, pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de veiculo aplicando filtros, paginação ou critérios de busca quando
     * informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<ModeloDTO> pesquisar(String termo, Pageable pageable) {
        if (termo == null || termo.isBlank()) {
            return listar(pageable);
        }
        return modeloRepository.search(termo.trim(), pageable).map(mapper::toDto);
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
        ModeloModel modelo = buscarModelAtivo(id);
        modelo.setAtivo(Boolean.FALSE);
        modeloRepository.save(modelo);
    }

    /**
     * Função: Localiza informações de veiculo conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    public ModeloModel buscarModelAtivo(Long id) {
        return modeloRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Modelo não encontrado ou inativo."));
    }


    @Transactional(readOnly = true)
    /**
     * Função: Lista cadastros inativados para que o usuário possa localizar e reativar registros sem
     * recriá-los.
     * Uso no sistema: reforça a rastreabilidade, pois registros antigos continuam no banco e podem
     * voltar a ficar ativos.
     */
    public Page<ModeloDTO> listarInativos(Pageable pageable) {
        return modeloRepository.findAllByAtivoFalse(pageable).map(mapper::toDto);
    }

    @Transactional
    /**
     * Função: Localiza um registro inativado, altera seu campo ativo para verdadeiro e salva a
     * reativação.
     * Uso no sistema: permite recuperar cadastros feitos anteriormente sem duplicar clientes,
     * veículos, peças ou serviços.
     */
    public ModeloDTO ativar(Long id) {
        validation.validateId(id);
        ModeloModel model = modeloRepository.findByIdAndAtivoFalse(id)
                .orElseThrow(() -> new BusinessException("Modelo não encontrado entre os inativos."));
        model.setAtivo(Boolean.TRUE);
        return mapper.toDto(modeloRepository.save(model));
    }

    /**
     * Função: Localiza informações de veiculo conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    private MarcaModel buscarMarcaAtiva(Long id) {
        return marcaRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Marca não encontrada ou inativa."));
    }
}
