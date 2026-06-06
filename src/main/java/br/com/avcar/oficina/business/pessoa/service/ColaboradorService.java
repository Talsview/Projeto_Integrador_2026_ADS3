package br.com.avcar.oficina.business.pessoa.service;

import br.com.avcar.oficina.business.pessoa.dto.ColaboradorDTO;
import br.com.avcar.oficina.business.pessoa.dto.ColaboradorResumoDTO;
import br.com.avcar.oficina.business.pessoa.mapper.ColaboradorMapper;
import br.com.avcar.oficina.business.pessoa.model.ColaboradorFuncaoModel;
import br.com.avcar.oficina.business.pessoa.model.ColaboradorModel;
import br.com.avcar.oficina.business.pessoa.model.FuncaoModel;
import br.com.avcar.oficina.business.pessoa.model.PessoaModel;
import br.com.avcar.oficina.business.pessoa.repository.IColaboradorFuncaoRepository;
import br.com.avcar.oficina.business.pessoa.repository.IColaboradorRepository;
import br.com.avcar.oficina.business.pessoa.repository.IFuncaoRepository;
import br.com.avcar.oficina.business.pessoa.repository.IPessoaRepository;
import br.com.avcar.oficina.business.pessoa.validation.ColaboradorValidation;
import br.com.avcar.oficina.core.exception.BusinessException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service do módulo Colaborador.
 *
 * Regras atendidas:
 * - Pessoa pode ser Cliente, Colaborador ou ambos.
 * - Colaborador deve possuir ao menos uma Função.
 * - Mecânico é registro de Funcao, não entidade própria.
 * - ColaboradorFuncao preserva histórico das funções exercidas.
 */
@Service
public class ColaboradorService {

    private final IPessoaRepository pessoaRepository;
    private final IColaboradorRepository colaboradorRepository;
    private final IFuncaoRepository funcaoRepository;
    private final IColaboradorFuncaoRepository colaboradorFuncaoRepository;
    private final ColaboradorValidation validation;
    private final ColaboradorMapper mapper;

    public ColaboradorService(IPessoaRepository pessoaRepository,
                              IColaboradorRepository colaboradorRepository,
                              IFuncaoRepository funcaoRepository,
                              IColaboradorFuncaoRepository colaboradorFuncaoRepository,
                              ColaboradorValidation validation,
                              ColaboradorMapper mapper) {
        this.pessoaRepository = pessoaRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.funcaoRepository = funcaoRepository;
        this.colaboradorFuncaoRepository = colaboradorFuncaoRepository;
        this.validation = validation;
        this.mapper = mapper;
    }

    @Transactional
    public ColaboradorDTO cadastrar(ColaboradorDTO dto) {
        validation.validateInsert(dto);

        PessoaModel pessoa = obterOuCriarPessoa(dto);
        mapper.atualizarPessoa(pessoa, dto);
        PessoaModel pessoaSalva = pessoaRepository.save(pessoa);

        ColaboradorModel colaborador = mapper.criarColaborador(pessoaSalva, dto);
        ColaboradorModel colaboradorSalvo = colaboradorRepository.save(colaborador);

        List<Long> funcoesIds = validation.extractFuncoesIds(dto);
        for (Long funcaoId : funcoesIds) {
            FuncaoModel funcao = buscarFuncaoAtiva(funcaoId);
            ColaboradorFuncaoModel colaboradorFuncao = mapper.criarColaboradorFuncao(colaboradorSalvo, funcao, LocalDate.now());
            colaboradorFuncaoRepository.save(colaboradorFuncao);
        }

        return montarDetalhe(colaboradorSalvo.getId());
    }

    @Transactional
    public ColaboradorDTO atualizar(Long id, ColaboradorDTO dto) {
        validation.validateUpdate(id, dto);

        ColaboradorModel colaborador = buscarColaboradorAtivo(id);
        PessoaModel pessoa = colaborador.getPessoa();

        mapper.atualizarPessoa(pessoa, dto);
        mapper.atualizarColaborador(colaborador, dto);

        pessoaRepository.save(pessoa);
        colaboradorRepository.save(colaborador);
        sincronizarFuncoes(colaborador, validation.extractFuncoesIds(dto));

        return montarDetalhe(id);
    }

    @Transactional(readOnly = true)
    public ColaboradorDTO buscar(Long id) {
        validation.validateId(id);
        return montarDetalhe(id);
    }

    @Transactional(readOnly = true)
    public Page<ColaboradorResumoDTO> listar(Pageable pageable) {
        return colaboradorRepository.findAllByAtivoTrue(pageable)
                .map(this::montarResumo);
    }

    @Transactional(readOnly = true)
    public Page<ColaboradorResumoDTO> pesquisar(String termo, Pageable pageable) {
        if (termo == null || termo.isBlank()) {
            return listar(pageable);
        }
        return colaboradorRepository.searchByPessoa(termo.trim(), pageable)
                .map(this::montarResumo);
    }

    @Transactional
    public void inativar(Long id) {
        validation.validateId(id);
        ColaboradorModel colaborador = buscarColaboradorAtivo(id);

        List<ColaboradorFuncaoModel> funcoesAtivas = colaboradorFuncaoRepository
                .findByColaboradorIdAndAtivoTrueAndDataFimIsNull(colaborador.getId());

        for (ColaboradorFuncaoModel funcaoAtual : funcoesAtivas) {
            funcaoAtual.setDataFim(LocalDate.now());
            funcaoAtual.setAtivo(Boolean.FALSE);
            colaboradorFuncaoRepository.save(funcaoAtual);
        }

        colaborador.setAtivo(Boolean.FALSE);
        colaboradorRepository.save(colaborador);
    }

    private PessoaModel obterOuCriarPessoa(ColaboradorDTO dto) {
        if (dto.getPessoaId() == null) {
            return mapper.criarPessoa(dto);
        }
        return pessoaRepository.findByIdAndAtivoTrue(dto.getPessoaId())
                .orElseThrow(() -> new BusinessException("Pessoa informada não encontrada ou inativa."));
    }

    private void sincronizarFuncoes(ColaboradorModel colaborador, List<Long> funcoesIdsDesejadas) {
        List<ColaboradorFuncaoModel> funcoesAtivas = new ArrayList<>(colaboradorFuncaoRepository
                .findByColaboradorIdAndAtivoTrueAndDataFimIsNull(colaborador.getId()));

        for (ColaboradorFuncaoModel funcaoAtual : funcoesAtivas) {
            boolean deveManter = funcoesIdsDesejadas.contains(funcaoAtual.getFuncao().getId());
            if (!deveManter) {
                funcaoAtual.setDataFim(LocalDate.now());
                funcaoAtual.setAtivo(Boolean.FALSE);
                colaboradorFuncaoRepository.save(funcaoAtual);
            }
        }

        for (Long funcaoId : funcoesIdsDesejadas) {
            boolean jaExisteAtiva = funcoesAtivas.stream()
                    .anyMatch(item -> item.getFuncao().getId().equals(funcaoId) && Boolean.TRUE.equals(item.getAtivo()));

            if (!jaExisteAtiva) {
                FuncaoModel funcao = buscarFuncaoAtiva(funcaoId);
                ColaboradorFuncaoModel novaFuncao = mapper.criarColaboradorFuncao(colaborador, funcao, LocalDate.now());
                colaboradorFuncaoRepository.save(novaFuncao);
            }
        }
    }

    private ColaboradorDTO montarDetalhe(Long id) {
        ColaboradorModel colaborador = buscarColaboradorAtivo(id);
        List<ColaboradorFuncaoModel> funcoesAtivas = colaboradorFuncaoRepository
                .findByColaboradorIdAndAtivoTrueAndDataFimIsNull(colaborador.getId());
        return mapper.toDto(colaborador, funcoesAtivas);
    }

    private ColaboradorResumoDTO montarResumo(ColaboradorModel colaborador) {
        List<ColaboradorFuncaoModel> funcoesAtivas = colaboradorFuncaoRepository
                .findByColaboradorIdAndAtivoTrueAndDataFimIsNull(colaborador.getId());
        return mapper.toResumo(colaborador, funcoesAtivas);
    }

    private ColaboradorModel buscarColaboradorAtivo(Long id) {
        return colaboradorRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Colaborador não encontrado ou inativo."));
    }

    private FuncaoModel buscarFuncaoAtiva(Long id) {
        return funcaoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Função não encontrada ou inativa."));
    }
}
