package br.com.avcar.oficina.business.peca.service;

import br.com.avcar.oficina.business.peca.dto.PecaDTO;
import br.com.avcar.oficina.business.peca.mapper.PecaMapper;
import br.com.avcar.oficina.business.peca.model.FornecedorModel;
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
    private final FornecedorService fornecedorService;

    public PecaService(IPecaRepository pecaRepository,
                       PecaValidation validation,
                       PecaMapper mapper,
                       FornecedorService fornecedorService) {
        this.pecaRepository = pecaRepository;
        this.validation = validation;
        this.mapper = mapper;
        this.fornecedorService = fornecedorService;
    }

    @Transactional
    public PecaDTO cadastrar(PecaDTO dto) {
        validation.validateInsert(dto);
        FornecedorModel fornecedorPadrao = buscarFornecedorPadrao(dto);
        PecaModel saved = pecaRepository.save(mapper.toModel(dto, fornecedorPadrao));
        return mapper.toDto(saved);
    }

    @Transactional
    public PecaDTO atualizar(Long id, PecaDTO dto) {
        validation.validateUpdate(id, dto);
        PecaModel peca = buscarModelAtivo(id);
        FornecedorModel fornecedorPadrao = buscarFornecedorPadrao(dto);
        mapper.atualizarModel(peca, dto, fornecedorPadrao);
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

    private FornecedorModel buscarFornecedorPadrao(PecaDTO dto) {
        if (dto.getIdFornecedorPadrao() == null || dto.getIdFornecedorPadrao() <= 0) {
            return null;
        }
        return fornecedorService.buscarModelAtivo(dto.getIdFornecedorPadrao());
    }

    public PecaModel buscarModelAtivo(Long id) {
        return pecaRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Peça não encontrada ou inativa."));
    }
}
