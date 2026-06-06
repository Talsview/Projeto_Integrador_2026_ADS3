package br.com.avcar.oficina.business.peca.service;

import br.com.avcar.oficina.business.peca.dto.FornecedorDTO;
import br.com.avcar.oficina.business.peca.mapper.FornecedorMapper;
import br.com.avcar.oficina.business.peca.model.FornecedorModel;
import br.com.avcar.oficina.business.peca.repository.IFornecedorRepository;
import br.com.avcar.oficina.business.peca.validation.FornecedorValidation;
import br.com.avcar.oficina.core.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsável pelas regras de negócio de Fornecedor.
 */
@Service
public class FornecedorService {

    private final IFornecedorRepository fornecedorRepository;
    private final FornecedorValidation validation;
    private final FornecedorMapper mapper;

    public FornecedorService(IFornecedorRepository fornecedorRepository,
                             FornecedorValidation validation,
                             FornecedorMapper mapper) {
        this.fornecedorRepository = fornecedorRepository;
        this.validation = validation;
        this.mapper = mapper;
    }

    @Transactional
    public FornecedorDTO cadastrar(FornecedorDTO dto) {
        validation.validateInsert(dto);
        FornecedorModel saved = fornecedorRepository.save(mapper.toModel(dto));
        return mapper.toDto(saved);
    }

    @Transactional
    public FornecedorDTO atualizar(Long id, FornecedorDTO dto) {
        validation.validateUpdate(id, dto);
        FornecedorModel fornecedor = buscarModelAtivo(id);
        mapper.atualizarModel(fornecedor, dto);
        return mapper.toDto(fornecedorRepository.save(fornecedor));
    }

    @Transactional(readOnly = true)
    public FornecedorDTO buscar(Long id) {
        validation.validateId(id);
        return mapper.toDto(buscarModelAtivo(id));
    }

    @Transactional(readOnly = true)
    public Page<FornecedorDTO> listar(Pageable pageable) {
        return fornecedorRepository.findAllByAtivoTrue(pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<FornecedorDTO> pesquisar(String termo, Pageable pageable) {
        if (termo == null || termo.isBlank()) {
            return listar(pageable);
        }
        return fornecedorRepository.search(termo.trim(), pageable).map(mapper::toDto);
    }

    @Transactional
    public void inativar(Long id) {
        validation.validateId(id);
        FornecedorModel fornecedor = buscarModelAtivo(id);
        fornecedor.setAtivo(Boolean.FALSE);
        fornecedorRepository.save(fornecedor);
    }

    public FornecedorModel buscarModelAtivo(Long id) {
        return fornecedorRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Fornecedor não encontrado ou inativo."));
    }
}
