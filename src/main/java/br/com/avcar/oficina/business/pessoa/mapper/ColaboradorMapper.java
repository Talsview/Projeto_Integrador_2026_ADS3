package br.com.avcar.oficina.business.pessoa.mapper;

import br.com.avcar.oficina.business.pessoa.dto.ColaboradorDTO;
import br.com.avcar.oficina.business.pessoa.dto.ColaboradorFuncaoDTO;
import br.com.avcar.oficina.business.pessoa.dto.ColaboradorResumoDTO;
import br.com.avcar.oficina.business.pessoa.enums.StatusColaborador;
import br.com.avcar.oficina.business.pessoa.model.ColaboradorFuncaoModel;
import br.com.avcar.oficina.business.pessoa.model.ColaboradorModel;
import br.com.avcar.oficina.business.pessoa.model.FuncaoModel;
import br.com.avcar.oficina.business.pessoa.model.PessoaModel;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Centraliza a conversão entre Models e DTOs do módulo Colaborador.
 */
@Component
public class ColaboradorMapper {

    public PessoaModel criarPessoa(ColaboradorDTO dto) {
        PessoaModel pessoa = new PessoaModel();
        atualizarPessoa(pessoa, dto);
        return pessoa;
    }

    public ColaboradorModel criarColaborador(PessoaModel pessoa, ColaboradorDTO dto) {
        ColaboradorModel colaborador = new ColaboradorModel();
        colaborador.setPessoa(pessoa);
        colaborador.setDataAdmissao(dto.getDataAdmissao());
        colaborador.setStatusColaborador(dto.getStatusColaborador() == null ? StatusColaborador.ATIVO : dto.getStatusColaborador());
        return colaborador;
    }

    public ColaboradorFuncaoModel criarColaboradorFuncao(ColaboradorModel colaborador, FuncaoModel funcao, LocalDate dataInicio) {
        ColaboradorFuncaoModel colaboradorFuncao = new ColaboradorFuncaoModel();
        colaboradorFuncao.setColaborador(colaborador);
        colaboradorFuncao.setFuncao(funcao);
        colaboradorFuncao.setDataInicio(dataInicio == null ? LocalDate.now() : dataInicio);
        return colaboradorFuncao;
    }

    public ColaboradorDTO toDto(ColaboradorModel colaborador, List<ColaboradorFuncaoModel> funcoesAtivas) {
        if (colaborador == null) {
            return null;
        }
        PessoaModel pessoa = colaborador.getPessoa();

        ColaboradorDTO dto = new ColaboradorDTO();
        dto.setId(colaborador.getId());
        dto.setPessoaId(pessoa.getId());
        dto.setAtivo(colaborador.getAtivo());
        dto.setNome(pessoa.getNome());
        dto.setTelefone(pessoa.getTelefone());
        dto.setEmail(pessoa.getEmail());
        dto.setEndereco(pessoa.getEndereco());
        dto.setDataAdmissao(colaborador.getDataAdmissao());
        dto.setStatusColaborador(colaborador.getStatusColaborador());
        dto.setFuncoes(funcoesAtivas.stream().map(this::toColaboradorFuncaoDto).collect(Collectors.toList()));
        dto.setFuncoesIds(dto.getFuncoes().stream().map(ColaboradorFuncaoDTO::getFuncaoId).collect(Collectors.toList()));
        return dto;
    }

    public ColaboradorResumoDTO toResumo(ColaboradorModel colaborador, List<ColaboradorFuncaoModel> funcoesAtivas) {
        PessoaModel pessoa = colaborador.getPessoa();

        ColaboradorResumoDTO dto = new ColaboradorResumoDTO();
        dto.setId(colaborador.getId());
        dto.setPessoaId(pessoa.getId());
        dto.setAtivo(colaborador.getAtivo());
        dto.setNome(pessoa.getNome());
        dto.setTelefone(pessoa.getTelefone());
        dto.setEmail(pessoa.getEmail());
        dto.setDataAdmissao(colaborador.getDataAdmissao());
        dto.setStatusColaborador(colaborador.getStatusColaborador());
        dto.setFuncoes(formatarFuncoes(funcoesAtivas));
        return dto;
    }

    public ColaboradorFuncaoDTO toColaboradorFuncaoDto(ColaboradorFuncaoModel model) {
        ColaboradorFuncaoDTO dto = new ColaboradorFuncaoDTO();
        dto.setId(model.getId());
        dto.setAtivo(model.getAtivo());
        dto.setColaboradorId(model.getColaborador().getId());
        dto.setFuncaoId(model.getFuncao().getId());
        dto.setNomeFuncao(model.getFuncao().getNomeFuncao());
        dto.setDataInicio(model.getDataInicio());
        dto.setDataFim(model.getDataFim());
        return dto;
    }

    public void atualizarPessoa(PessoaModel pessoa, ColaboradorDTO dto) {
        pessoa.setNome(dto.getNome());
        pessoa.setTelefone(dto.getTelefone());
        pessoa.setEmail(dto.getEmail());
        pessoa.setEndereco(dto.getEndereco());
    }

    public void atualizarColaborador(ColaboradorModel colaborador, ColaboradorDTO dto) {
        colaborador.setDataAdmissao(dto.getDataAdmissao());
        colaborador.setStatusColaborador(dto.getStatusColaborador() == null ? StatusColaborador.ATIVO : dto.getStatusColaborador());
    }

    private String formatarFuncoes(List<ColaboradorFuncaoModel> funcoesAtivas) {
        if (funcoesAtivas == null || funcoesAtivas.isEmpty()) {
            return "";
        }
        return funcoesAtivas.stream()
                .sorted(Comparator.comparing(item -> item.getFuncao().getNomeFuncao()))
                .map(item -> item.getFuncao().getNomeFuncao())
                .collect(Collectors.joining(", "));
    }
}
