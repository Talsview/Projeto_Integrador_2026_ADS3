package br.com.avcar.oficina.business.veiculo.service;

import br.com.avcar.oficina.business.veiculo.dto.ModeloDTO;
import br.com.avcar.oficina.business.veiculo.mapper.ModeloMapper;
import br.com.avcar.oficina.business.veiculo.model.MarcaModel;
import br.com.avcar.oficina.business.veiculo.model.ModeloModel;
import br.com.avcar.oficina.business.veiculo.repository.IMarcaRepository;
import br.com.avcar.oficina.business.veiculo.repository.IModeloRepository;
import br.com.avcar.oficina.business.veiculo.validation.ModeloValidation;
import br.com.avcar.oficina.core.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsável pelas regras de negócio de Modelo.
 */
@Service
public class ModeloService {

    private final IMarcaRepository marcaRepository;
    private final IModeloRepository modeloRepository;
    private final ModeloValidation validation;
    private final ModeloMapper mapper;

    public ModeloService(IMarcaRepository marcaRepository,
                         IModeloRepository modeloRepository,
                         ModeloValidation validation,
                         ModeloMapper mapper) {
        this.marcaRepository = marcaRepository;
        this.modeloRepository = modeloRepository;
        this.validation = validation;
        this.mapper = mapper;
    }

    @Transactional
    public ModeloDTO cadastrar(ModeloDTO dto) {
        validation.validateInsert(dto);
        MarcaModel marca = buscarMarcaAtiva(dto.getMarcaId());
        ModeloModel saved = modeloRepository.save(mapper.toModel(dto, marca));
        return mapper.toDto(saved);
    }

    @Transactional
    public ModeloDTO atualizar(Long id, ModeloDTO dto) {
        validation.validateUpdate(id, dto);
        ModeloModel modelo = buscarModelAtivo(id);
        MarcaModel marca = buscarMarcaAtiva(dto.getMarcaId());
        mapper.atualizarModel(modelo, dto, marca);
        return mapper.toDto(modeloRepository.save(modelo));
    }

    @Transactional(readOnly = true)
    public ModeloDTO buscar(Long id) {
        validation.validateId(id);
        return mapper.toDto(buscarModelAtivo(id));
    }

    @Transactional(readOnly = true)
    public Page<ModeloDTO> listar(Pageable pageable) {
        return modeloRepository.findAllByAtivoTrue(pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ModeloDTO> listarPorMarca(Long marcaId, Pageable pageable) {
        validation.validateMarca(marcaId);
        return modeloRepository.findByMarcaIdAndAtivoTrue(marcaId, pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ModeloDTO> pesquisar(String termo, Pageable pageable) {
        if (termo == null || termo.isBlank()) {
            return listar(pageable);
        }
        return modeloRepository.search(termo.trim(), pageable).map(mapper::toDto);
    }

    @Transactional
    public void inativar(Long id) {
        validation.validateId(id);
        ModeloModel modelo = buscarModelAtivo(id);
        modelo.setAtivo(Boolean.FALSE);
        modeloRepository.save(modelo);
    }

    public ModeloModel buscarModelAtivo(Long id) {
        return modeloRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Modelo não encontrado ou inativo."));
    }

    private MarcaModel buscarMarcaAtiva(Long id) {
        return marcaRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Marca não encontrada ou inativa."));
    }
}
