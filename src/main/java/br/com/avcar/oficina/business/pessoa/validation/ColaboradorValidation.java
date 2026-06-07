package br.com.avcar.oficina.business.pessoa.validation;

import br.com.avcar.oficina.business.pessoa.dto.ColaboradorDTO;
import br.com.avcar.oficina.business.pessoa.dto.ColaboradorFuncaoDTO;
import br.com.avcar.oficina.business.pessoa.repository.IColaboradorRepository;
import br.com.avcar.oficina.business.pessoa.repository.IFuncaoRepository;
import br.com.avcar.oficina.business.pessoa.repository.IPessoaRepository;
import br.com.avcar.oficina.core.exception.FieldValidationException;
import br.com.avcar.oficina.core.validation.ValidationUtils;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * Valida as regras de entrada do cadastro de Colaborador.
 */
@Component
public class ColaboradorValidation {

    private final IPessoaRepository pessoaRepository;
    private final IColaboradorRepository colaboradorRepository;
    private final IFuncaoRepository funcaoRepository;

    public ColaboradorValidation(IPessoaRepository pessoaRepository,
                                 IColaboradorRepository colaboradorRepository,
                                 IFuncaoRepository funcaoRepository) {
        this.pessoaRepository = pessoaRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.funcaoRepository = funcaoRepository;
    }

    public void validateInsert(ColaboradorDTO dto) {
        validateFields(dto);
        validatePessoaParaInsert(dto);
        validateFuncoes(extractFuncoesIds(dto));
    }

    public void validateUpdate(Long id, ColaboradorDTO dto) {
        validateId(id);
        validateFields(dto);
        validateFuncoes(extractFuncoesIds(dto));
    }

    public void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new FieldValidationException("O identificador do colaborador é obrigatório.");
        }
    }

    public List<Long> extractFuncoesIds(ColaboradorDTO dto) {
        Set<Long> ids = new LinkedHashSet<>();

        if (dto.getFuncoesIds() != null) {
            ids.addAll(dto.getFuncoesIds());
        }

        if (dto.getFuncoes() != null) {
            dto.getFuncoes().stream()
                    .map(ColaboradorFuncaoDTO::getFuncaoId)
                    .filter(id -> id != null)
                    .forEach(ids::add);
        }

        return new ArrayList<>(ids);
    }

    private void validateFields(ColaboradorDTO dto) {
        if (dto == null) {
            throw new FieldValidationException("Os dados do colaborador são obrigatórios.");
        }
        ValidationUtils.validatePersonName(dto.getNome(), "nome do colaborador");
        ValidationUtils.validatePhone(dto.getTelefone(), false);
        ValidationUtils.validateEmail(dto.getEmail(), false);
        ValidationUtils.maxLength(dto.getEndereco(), 255, "endereço");
        ValidationUtils.notFuture(dto.getDataAdmissao(), "data de admissão");
        if (dto.getStatusColaborador() == null) {
            throw new FieldValidationException("O status do colaborador é obrigatório.");
        }
    }

    private void validatePessoaParaInsert(ColaboradorDTO dto) {
        if (dto.getPessoaId() == null) {
            return;
        }
        if (dto.getPessoaId() <= 0) {
            throw new FieldValidationException("O identificador da pessoa é inválido.");
        }
        if (pessoaRepository.findByIdAndAtivoTrue(dto.getPessoaId()).isEmpty()) {
            throw new FieldValidationException("A pessoa informada para o colaborador não foi localizada ou está inativa.");
        }
        if (colaboradorRepository.existsByPessoaIdAndAtivoTrue(dto.getPessoaId())) {
            throw new FieldValidationException("Esta pessoa já possui cadastro ativo de colaborador.");
        }
    }

    private void validateFuncoes(List<Long> funcoesIds) {
        if (funcoesIds == null || funcoesIds.isEmpty()) {
            throw new FieldValidationException("O colaborador deve possuir ao menos uma função.");
        }
        for (Long funcaoId : funcoesIds) {
            if (funcaoId == null || funcaoId <= 0) {
                throw new FieldValidationException("Todas as funções informadas devem possuir identificador válido.");
            }
            if (funcaoRepository.findByIdAndAtivoTrue(funcaoId).isEmpty()) {
                throw new FieldValidationException("Função informada não encontrada ou inativa: " + funcaoId + ".");
            }
        }
    }
}
