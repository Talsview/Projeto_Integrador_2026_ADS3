package br.com.avcar.oficina.business.veiculo.service;

import br.com.avcar.oficina.business.veiculo.dto.MarcaDTO;
import br.com.avcar.oficina.business.veiculo.mapper.MarcaMapper;
import br.com.avcar.oficina.business.veiculo.model.MarcaModel;
import br.com.avcar.oficina.business.veiculo.repository.IMarcaRepository;
import br.com.avcar.oficina.business.veiculo.validation.MarcaValidation;
import br.com.avcar.oficina.core.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsável pelas regras de negócio de Marca.
 */
@Service
public class MarcaService {

    private final IMarcaRepository marcaRepository;
    private final MarcaValidation validation;
    private final MarcaMapper mapper;

    public MarcaService(IMarcaRepository marcaRepository,
                        MarcaValidation validation,
                        MarcaMapper mapper) {
        this.marcaRepository = marcaRepository;
        this.validation = validation;
        this.mapper = mapper;
    }

    @Transactional
    public MarcaDTO cadastrar(MarcaDTO dto) {
        validation.validateInsert(dto);
        MarcaModel saved = marcaRepository.save(mapper.toModel(dto));
        return mapper.toDto(saved);
    }

    @Transactional
    public MarcaDTO atualizar(Long id, MarcaDTO dto) {
        validation.validateUpdate(id, dto);
        MarcaModel marca = buscarModelAtivo(id);
        mapper.atualizarModel(marca, dto);
        return mapper.toDto(marcaRepository.save(marca));
    }

    @Transactional(readOnly = true)
    public MarcaDTO buscar(Long id) {
        validation.validateId(id);
        return mapper.toDto(buscarModelAtivo(id));
    }

    @Transactional(readOnly = true)
    public Page<MarcaDTO> listar(Pageable pageable) {
        return marcaRepository.findAllByAtivoTrue(pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<MarcaDTO> pesquisar(String termo, Pageable pageable) {
        if (termo == null || termo.isBlank()) {
            return listar(pageable);
        }
        return marcaRepository.findByNomeMarcaContainingIgnoreCaseAndAtivoTrue(termo.trim(), pageable).map(mapper::toDto);
    }

    @Transactional
    public void inativar(Long id) {
        validation.validateId(id);
        MarcaModel marca = buscarModelAtivo(id);
        marca.setAtivo(Boolean.FALSE);
        marcaRepository.save(marca);
    }

    public MarcaModel buscarModelAtivo(Long id) {
        return marcaRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Marca não encontrada ou inativa."));
    }
}
