package br.com.avcar.oficina.business.peca.service;

import br.com.avcar.oficina.business.peca.dto.PecaDTO;
import br.com.avcar.oficina.business.peca.mapper.PecaMapper;
import br.com.avcar.oficina.business.peca.model.PecaModel;
import br.com.avcar.oficina.business.peca.repository.IPecaRepository;
import br.com.avcar.oficina.business.peca.validation.PecaValidation;
import br.com.avcar.oficina.core.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsável pelas regras de negócio de Peça.
 */
@Service
public class PecaService {

    private final IPecaRepository pecaRepository;
    private final PecaValidation validation;
    private final PecaMapper mapper;

    public PecaService(IPecaRepository pecaRepository,
                       PecaValidation validation,
                       PecaMapper mapper) {
        this.pecaRepository = pecaRepository;
        this.validation = validation;
        this.mapper = mapper;
    }

    @Transactional
    public PecaDTO cadastrar(PecaDTO dto) {
        validation.validateInsert(dto);
        PecaModel saved = pecaRepository.save(mapper.toModel(dto));
        return mapper.toDto(saved);
    }

    @Transactional
    public PecaDTO atualizar(Long id, PecaDTO dto) {
        validation.validateUpdate(id, dto);
        PecaModel peca = buscarModelAtivo(id);
        mapper.atualizarModel(peca, dto);
        return mapper.toDto(pecaRepository.save(peca));
    }

    @Transactional(readOnly = true)
    public PecaDTO buscar(Long id) {
        validation.validateId(id);
        return mapper.toDto(buscarModelAtivo(id));
    }

    @Transactional(readOnly = true)
    public Page<PecaDTO> listar(Pageable pageable) {
        return pecaRepository.findAllByAtivoTrue(pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<PecaDTO> pesquisar(String termo, Pageable pageable) {
        if (termo == null || termo.isBlank()) {
            return listar(pageable);
        }
        return pecaRepository.search(termo.trim(), pageable).map(mapper::toDto);
    }

    @Transactional
    public void inativar(Long id) {
        validation.validateId(id);
        PecaModel peca = buscarModelAtivo(id);
        peca.setAtivo(Boolean.FALSE);
        pecaRepository.save(peca);
    }

    public PecaModel buscarModelAtivo(Long id) {
        return pecaRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Peça não encontrada ou inativa."));
    }
}
