package br.com.avcar.oficina.business.pessoa.service;

import br.com.avcar.oficina.business.pessoa.dto.FuncaoDTO;
import br.com.avcar.oficina.business.pessoa.mapper.FuncaoMapper;
import br.com.avcar.oficina.business.pessoa.model.FuncaoModel;
import br.com.avcar.oficina.business.pessoa.repository.IFuncaoRepository;
import br.com.avcar.oficina.business.pessoa.validation.FuncaoValidation;
import br.com.avcar.oficina.core.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsável pelas regras de negócio de Função.
 */
@Service
public class FuncaoService {

    private final IFuncaoRepository funcaoRepository;
    private final FuncaoValidation validation;
    private final FuncaoMapper mapper;

    public FuncaoService(IFuncaoRepository funcaoRepository,
                         FuncaoValidation validation,
                         FuncaoMapper mapper) {
        this.funcaoRepository = funcaoRepository;
        this.validation = validation;
        this.mapper = mapper;
    }

    @Transactional
    public FuncaoDTO cadastrar(FuncaoDTO dto) {
        validation.validateInsert(dto);
        FuncaoModel saved = funcaoRepository.save(mapper.toModel(dto));
        return mapper.toDto(saved);
    }

    @Transactional
    public FuncaoDTO atualizar(Long id, FuncaoDTO dto) {
        validation.validateUpdate(id, dto);
        FuncaoModel funcao = buscarModelAtivo(id);
        mapper.atualizarModel(funcao, dto);
        return mapper.toDto(funcaoRepository.save(funcao));
    }

    @Transactional(readOnly = true)
    public FuncaoDTO buscar(Long id) {
        validation.validateId(id);
        return mapper.toDto(buscarModelAtivo(id));
    }

    @Transactional(readOnly = true)
    public Page<FuncaoDTO> listar(Pageable pageable) {
        return funcaoRepository.findAllByAtivoTrue(pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<FuncaoDTO> pesquisar(String termo, Pageable pageable) {
        if (termo == null || termo.isBlank()) {
            return listar(pageable);
        }
        return funcaoRepository.findByNomeFuncaoContainingIgnoreCaseAndAtivoTrue(termo.trim(), pageable).map(mapper::toDto);
    }

    @Transactional
    public void inativar(Long id) {
        validation.validateId(id);
        FuncaoModel funcao = buscarModelAtivo(id);
        funcao.setAtivo(Boolean.FALSE);
        funcaoRepository.save(funcao);
    }

    private FuncaoModel buscarModelAtivo(Long id) {
        return funcaoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Função não encontrada ou inativa."));
    }
}
