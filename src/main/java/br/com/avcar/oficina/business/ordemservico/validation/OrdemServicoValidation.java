package br.com.avcar.oficina.business.ordemservico.validation;

import br.com.avcar.oficina.business.ordemservico.dto.AlterarStatusOrdemServicoDTO;
import br.com.avcar.oficina.business.ordemservico.dto.OrdemServicoDTO;
import br.com.avcar.oficina.business.ordemservico.model.HistoricoStatusOrdemModel;
import br.com.avcar.oficina.business.ordemservico.model.StatusOrdemServicoModel;
import br.com.avcar.oficina.business.ordemservico.repository.IOrdemServicoRepository;
import br.com.avcar.oficina.core.exception.RuleValidationException;
import br.com.avcar.oficina.core.validation.ValidationUtils;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

/**
 * Validações de domínio da Ordem de Serviço.
 */
@Component
public class OrdemServicoValidation {

    private final IOrdemServicoRepository ordemServicoRepository;

    public OrdemServicoValidation(IOrdemServicoRepository ordemServicoRepository) {
        this.ordemServicoRepository = ordemServicoRepository;
    }

    public void validateInsert(OrdemServicoDTO dto) {
        validateDto(dto);
        if (dto.getNumeroOs() != null && !dto.getNumeroOs().isBlank()
                && ordemServicoRepository.existsActiveByNumeroOs(dto.getNumeroOs().trim())) {
            throw new RuleValidationException("Já existe uma Ordem de Serviço ativa com esse número.");
        }
    }

    public void validateUpdate(Long id, OrdemServicoDTO dto) {
        validateId(id);
        validateDto(dto);
        if (dto.getNumeroOs() != null && !dto.getNumeroOs().isBlank()
                && ordemServicoRepository.existsActiveByNumeroOsAndIdNot(dto.getNumeroOs().trim(), id)) {
            throw new RuleValidationException("Já existe outra Ordem de Serviço ativa com esse número.");
        }
    }

    public void validateStatusChange(AlterarStatusOrdemServicoDTO dto,
                                     HistoricoStatusOrdemModel statusAtual,
                                     StatusOrdemServicoModel novoStatus) {
        if (dto == null || dto.getNovoStatus() == null) {
            throw new RuleValidationException("O novo status da Ordem de Serviço é obrigatório.");
        }
        if (statusAtual == null) {
            throw new RuleValidationException("A Ordem de Serviço não possui histórico de status inicial.");
        }
        if (novoStatus == null) {
            throw new RuleValidationException("Status de destino não encontrado no cadastro de status da OS.");
        }

        Integer fluxoAtual = statusAtual.getStatusOrdemServico().getOrdemFluxo();
        Integer fluxoNovo = novoStatus.getOrdemFluxo();

        if (fluxoNovo.equals(fluxoAtual)) {
            throw new RuleValidationException("A Ordem de Serviço já está no status informado.");
        }
        if (fluxoNovo < fluxoAtual) {
            throw new RuleValidationException("A Ordem de Serviço não pode retornar para um status anterior.");
        }
        if (fluxoNovo > fluxoAtual + 1) {
            throw new RuleValidationException("A Ordem de Serviço deve seguir o fluxo sem pular etapas.");
        }
    }

    public void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new RuleValidationException("ID inválido para Ordem de Serviço.");
        }
    }

    private void validateDto(OrdemServicoDTO dto) {
        if (dto == null) {
            throw new RuleValidationException("Os dados da Ordem de Serviço são obrigatórios.");
        }
        if (dto.getIdCliente() == null || dto.getIdCliente() <= 0) {
            throw new RuleValidationException("O cliente da Ordem de Serviço é obrigatório.");
        }
        if (dto.getIdVeiculo() == null || dto.getIdVeiculo() <= 0) {
            throw new RuleValidationException("O veículo da Ordem de Serviço é obrigatório.");
        }
        ValidationUtils.notFuture(dto.getDataAbertura(), "data de abertura da OS");
        ValidationUtils.notFuture(dto.getDataAprovacao(), "data de aprovação da OS");
        ValidationUtils.notFuture(dto.getDataFinalizacao(), "data de finalização da OS");
        ValidationUtils.dateNotBefore(dto.getDataAprovacao(), dto.getDataAbertura(), "data de aprovação", "data de abertura");
        ValidationUtils.dateNotBefore(dto.getDataFinalizacao(), dto.getDataAbertura(), "data de finalização", "data de abertura");
        ValidationUtils.dateNotBefore(dto.getDataFinalizacao(), dto.getDataAprovacao(), "data de finalização", "data de aprovação");
        if (dto.getPrioridade() == null) {
            dto.setPrioridade(br.com.avcar.oficina.business.ordemservico.enums.PrioridadeOrdemServico.NORMAL);
        }
        if (dto.getValorTotal() != null && dto.getValorTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuleValidationException("O valor total da OS não pode ser negativo.");
        }
        if (dto.getNumeroOs() != null && !dto.getNumeroOs().isBlank()
                && !dto.getNumeroOs().trim().matches("^[A-Za-z0-9_/-]{1,30}$")) {
            throw new RuleValidationException("O número da OS contém caracteres inválidos.");
        }
        ValidationUtils.maxLength(dto.getObservacao(), 2000, "observação da OS");
    }
}
