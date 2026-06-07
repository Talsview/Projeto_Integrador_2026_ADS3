package br.com.avcar.oficina.business.servico.validation;

import br.com.avcar.oficina.business.servico.dto.ServicoDTO;
import br.com.avcar.oficina.business.servico.repository.IServicoRepository;
import br.com.avcar.oficina.core.exception.FieldValidationException;
import br.com.avcar.oficina.core.validation.ValidationUtils;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

/**
 * Valida regras de entrada do cadastro de Serviço.
 */
@Component
public class ServicoValidation {

    private final IServicoRepository servicoRepository;

    public ServicoValidation(IServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    public void validateInsert(ServicoDTO dto) {
        validateFields(dto);
        if (servicoRepository.existsActiveByNome(dto.getNomeServico().trim())) {
            throw new FieldValidationException("Já existe serviço ativo com este nome.");
        }
    }

    public void validateUpdate(Long id, ServicoDTO dto) {
        validateId(id);
        validateFields(dto);
        if (servicoRepository.existsActiveByNomeAndIdNot(dto.getNomeServico().trim(), id)) {
            throw new FieldValidationException("Já existe outro serviço ativo com este nome.");
        }
    }

    public void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new FieldValidationException("O identificador do serviço é obrigatório.");
        }
    }

    private void validateFields(ServicoDTO dto) {
        if (dto == null) {
            throw new FieldValidationException("Os dados do serviço são obrigatórios.");
        }
        ValidationUtils.validateBusinessText(dto.getNomeServico(), "nome do serviço", true);
        ValidationUtils.maxLength(dto.getDescricao(), 2000, "descrição do serviço");
        ValidationUtils.maxLength(dto.getObservacaoInterna(), 2000, "observação interna");
        ValidationUtils.maxLength(dto.getObservacaoTerceirizacao(), 2000, "observação de terceirização");
        if (dto.getTipoServico() == null) {
            throw new FieldValidationException("O tipo do serviço é obrigatório: INTERNO ou TERCEIRIZADO.");
        }
        if (dto.getPrazoGarantiaDias() == null || dto.getPrazoGarantiaDias() < 0) {
            throw new FieldValidationException("O prazo de garantia do serviço deve ser maior ou igual a zero.");
        }
        if (dto.getValorBase() == null || dto.getValorBase().compareTo(BigDecimal.ZERO) < 0) {
            throw new FieldValidationException("O valor base do serviço deve ser maior ou igual a zero.");
        }
    }
}
