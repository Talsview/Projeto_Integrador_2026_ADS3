package br.com.avcar.oficina.business.ordemservico.service;

import br.com.avcar.oficina.business.ordemservico.dto.StatusOrdemServicoDTO;
import br.com.avcar.oficina.business.ordemservico.enums.StatusFluxoOrdemServico;
import br.com.avcar.oficina.business.ordemservico.mapper.StatusOrdemServicoMapper;
import br.com.avcar.oficina.business.ordemservico.model.StatusOrdemServicoModel;
import br.com.avcar.oficina.business.ordemservico.repository.IStatusOrdemServicoRepository;
import br.com.avcar.oficina.business.ordemservico.validation.StatusOrdemServicoValidation;
import br.com.avcar.oficina.core.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service de consulta dos status oficiais da Ordem de Serviço.
 */
@Service
public class StatusOrdemServicoService {

    private final IStatusOrdemServicoRepository statusRepository;
    private final StatusOrdemServicoValidation validation;
    private final StatusOrdemServicoMapper mapper;

    public StatusOrdemServicoService(IStatusOrdemServicoRepository statusRepository,
                                     StatusOrdemServicoValidation validation,
                                     StatusOrdemServicoMapper mapper) {
        this.statusRepository = statusRepository;
        this.validation = validation;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public StatusOrdemServicoDTO buscar(Long id) {
        validation.validateId(id);
        return mapper.toDto(buscarModelAtivo(id));
    }

    @Transactional(readOnly = true)
    public Page<StatusOrdemServicoDTO> listar(Pageable pageable) {
        return statusRepository.findAllActiveOrderByFluxo(pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<StatusOrdemServicoDTO> pesquisar(String termo, Pageable pageable) {
        if (termo == null || termo.isBlank()) {
            return listar(pageable);
        }
        return statusRepository.search(termo.trim(), pageable).map(mapper::toDto);
    }

    public StatusOrdemServicoModel buscarPorFluxo(StatusFluxoOrdemServico status) {
        return statusRepository.findByNomeStatusAndAtivoTrue(status.name())
                .orElseThrow(() -> new BusinessException("Status de Ordem de Serviço não encontrado: " + status.name()));
    }

    public StatusOrdemServicoModel buscarModelAtivo(Long id) {
        return statusRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Status de Ordem de Serviço não encontrado ou inativo."));
    }
}
