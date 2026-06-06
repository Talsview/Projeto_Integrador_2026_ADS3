package br.com.avcar.oficina.business.servico.service;

import br.com.avcar.oficina.business.servico.dto.EmpresaTerceirizadaDTO;
import br.com.avcar.oficina.business.servico.mapper.EmpresaTerceirizadaMapper;
import br.com.avcar.oficina.business.servico.model.EmpresaTerceirizadaModel;
import br.com.avcar.oficina.business.servico.repository.IEmpresaTerceirizadaRepository;
import br.com.avcar.oficina.business.servico.validation.EmpresaTerceirizadaValidation;
import br.com.avcar.oficina.core.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsável pelas regras de negócio de Empresa Terceirizada.
 */
@Service
public class EmpresaTerceirizadaService {

    private final IEmpresaTerceirizadaRepository empresaRepository;
    private final EmpresaTerceirizadaValidation validation;
    private final EmpresaTerceirizadaMapper mapper;

    public EmpresaTerceirizadaService(IEmpresaTerceirizadaRepository empresaRepository,
                                      EmpresaTerceirizadaValidation validation,
                                      EmpresaTerceirizadaMapper mapper) {
        this.empresaRepository = empresaRepository;
        this.validation = validation;
        this.mapper = mapper;
    }

    @Transactional
    public EmpresaTerceirizadaDTO cadastrar(EmpresaTerceirizadaDTO dto) {
        validation.validateInsert(dto);
        EmpresaTerceirizadaModel saved = empresaRepository.save(mapper.toModel(dto));
        return mapper.toDto(saved);
    }

    @Transactional
    public EmpresaTerceirizadaDTO atualizar(Long id, EmpresaTerceirizadaDTO dto) {
        validation.validateUpdate(id, dto);
        EmpresaTerceirizadaModel empresa = buscarModelAtivo(id);
        mapper.atualizarModel(empresa, dto);
        return mapper.toDto(empresaRepository.save(empresa));
    }

    @Transactional(readOnly = true)
    public EmpresaTerceirizadaDTO buscar(Long id) {
        validation.validateId(id);
        return mapper.toDto(buscarModelAtivo(id));
    }

    @Transactional(readOnly = true)
    public Page<EmpresaTerceirizadaDTO> listar(Pageable pageable) {
        return empresaRepository.findAllByAtivoTrue(pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<EmpresaTerceirizadaDTO> pesquisar(String termo, Pageable pageable) {
        if (termo == null || termo.isBlank()) {
            return listar(pageable);
        }
        return empresaRepository.search(termo.trim(), pageable).map(mapper::toDto);
    }

    @Transactional
    public void inativar(Long id) {
        validation.validateId(id);
        EmpresaTerceirizadaModel empresa = buscarModelAtivo(id);
        empresa.setAtivo(Boolean.FALSE);
        empresaRepository.save(empresa);
    }

    public EmpresaTerceirizadaModel buscarModelAtivo(Long id) {
        return empresaRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Empresa terceirizada não encontrada ou inativa."));
    }
}
