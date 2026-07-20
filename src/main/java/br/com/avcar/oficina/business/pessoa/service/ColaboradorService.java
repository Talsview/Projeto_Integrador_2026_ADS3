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
import br.com.avcar.oficina.core.service.GenericService;
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
public class ColaboradorService extends GenericService<ColaboradorModel> {

    private final IPessoaRepository pessoaRepository;
    private final IColaboradorRepository colaboradorRepository;
    private final IFuncaoRepository funcaoRepository;
    private final IColaboradorFuncaoRepository colaboradorFuncaoRepository;
    private final ColaboradorValidation validation;
    private final ColaboradorMapper mapper;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public ColaboradorService(IPessoaRepository pessoaRepository,
                              IColaboradorRepository colaboradorRepository,
                              IFuncaoRepository funcaoRepository,
                              IColaboradorFuncaoRepository colaboradorFuncaoRepository,
                              ColaboradorValidation validation,
                              ColaboradorMapper mapper) {
        super(colaboradorRepository, null);
        this.pessoaRepository = pessoaRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.funcaoRepository = funcaoRepository;
        this.colaboradorFuncaoRepository = colaboradorFuncaoRepository;
        this.validation = validation;
        this.mapper = mapper;
    }

    @Transactional
    /**
     * Função: Valida os dados recebidos, monta as entidades necessárias e grava o cadastro de
     * colaborador.
     * Uso no sistema: centraliza a regra de cadastro na camada Service, mantendo Controller e tela
     * mais simples.
     */
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
    /**
     * Função: Busca o registro ativo, aplica as alterações permitidas e salva a atualização de
     * colaborador.
     * Uso no sistema: garante que alterações passem por validação e não quebrem vínculos já existentes
     * no sistema.
     */
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
    /**
     * Função: Localiza informações de colaborador conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    public ColaboradorDTO buscar(Long id) {
        validation.validateId(id);
        return montarDetalhe(id);
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de colaborador aplicando filtros, paginação ou critérios de busca
     * quando informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<ColaboradorResumoDTO> listar(Pageable pageable) {
        return colaboradorRepository.findAllByAtivoTrue(pageable)
                .map(this::montarResumo);
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de colaborador aplicando filtros, paginação ou critérios de busca
     * quando informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<ColaboradorResumoDTO> pesquisar(String termo, Pageable pageable) {
        if (termo == null || termo.isBlank()) {
            return listar(pageable);
        }
        return colaboradorRepository.searchByPessoa(termo.trim(), pageable)
                .map(this::montarResumo);
    }

    @Transactional
    /**
     * Função: Localiza um registro inativado, altera seu campo ativo para verdadeiro e salva a
     * reativação.
     * Uso no sistema: permite recuperar cadastros feitos anteriormente sem duplicar clientes,
     * veículos, peças ou serviços.
     */
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



    @Transactional(readOnly = true)
    /**
     * Função: Lista cadastros inativados para que o usuário possa localizar e reativar registros sem
     * recriá-los.
     * Uso no sistema: reforça a rastreabilidade, pois registros antigos continuam no banco e podem
     * voltar a ficar ativos.
     */
    public Page<ColaboradorResumoDTO> listarInativos(Pageable pageable) {
        return colaboradorRepository.findAllByAtivoFalse(pageable)
                .map(this::montarResumoInativo);
    }

    @Transactional
    /**
     * Função: Localiza um registro inativado, altera seu campo ativo para verdadeiro e salva a
     * reativação.
     * Uso no sistema: permite recuperar cadastros feitos anteriormente sem duplicar clientes,
     * veículos, peças ou serviços.
     */
    public ColaboradorResumoDTO ativar(Long id) {
        validation.validateId(id);
        ColaboradorModel colaborador = colaboradorRepository.findByIdAndAtivoFalse(id)
                .orElseThrow(() -> new BusinessException("Colaborador não encontrado entre os inativos."));
        colaborador.setAtivo(Boolean.TRUE);
        if (colaborador.getPessoa() != null) {
            colaborador.getPessoa().setAtivo(Boolean.TRUE);
            pessoaRepository.save(colaborador.getPessoa());
        }
        ColaboradorModel saved = colaboradorRepository.save(colaborador);

        List<ColaboradorFuncaoModel> funcoesInativas = colaboradorFuncaoRepository
                .findByColaboradorIdAndAtivoFalse(saved.getId());
        for (ColaboradorFuncaoModel funcaoHistorica : funcoesInativas) {
            funcaoHistorica.setAtivo(Boolean.TRUE);
            funcaoHistorica.setDataFim(null);
            colaboradorFuncaoRepository.save(funcaoHistorica);
        }

        return montarResumo(saved);
    }

    /**
     * Função: Localiza informações de colaborador conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    private PessoaModel obterOuCriarPessoa(ColaboradorDTO dto) {
        if (dto.getPessoaId() == null) {
            return mapper.criarPessoa(dto);
        }
        return pessoaRepository.findByIdAndAtivoTrue(dto.getPessoaId())
                .orElseThrow(() -> new BusinessException("Pessoa informada não encontrada ou inativa."));
    }

    /**
     * Função: Atualiza os vínculos entre colaborador e funções conforme a seleção feita no cadastro.
     * Uso no sistema: mantém a regra de que um colaborador pode possuir uma ou mais funções.
     */
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

    /**
     * Função: Monta o objeto ou resposta necessária para a operação montar detalhe.
     * Uso no sistema: isola a preparação dos dados e melhora a legibilidade do fluxo principal.
     */
    private ColaboradorDTO montarDetalhe(Long id) {
        ColaboradorModel colaborador = buscarColaboradorAtivo(id);
        List<ColaboradorFuncaoModel> funcoesAtivas = colaboradorFuncaoRepository
                .findByColaboradorIdAndAtivoTrueAndDataFimIsNull(colaborador.getId());
        return mapper.toDto(colaborador, funcoesAtivas);
    }

    /**
     * Função: Monta o objeto ou resposta necessária para a operação montar resumo.
     * Uso no sistema: isola a preparação dos dados e melhora a legibilidade do fluxo principal.
     */
    private ColaboradorResumoDTO montarResumo(ColaboradorModel colaborador) {
        List<ColaboradorFuncaoModel> funcoesAtivas = colaboradorFuncaoRepository
                .findByColaboradorIdAndAtivoTrueAndDataFimIsNull(colaborador.getId());
        return mapper.toResumo(colaborador, funcoesAtivas);
    }



    /**
     * Função: Monta o objeto ou resposta necessária para a operação montar resumo inativo.
     * Uso no sistema: isola a preparação dos dados e melhora a legibilidade do fluxo principal.
     */
    private ColaboradorResumoDTO montarResumoInativo(ColaboradorModel colaborador) {
        List<ColaboradorFuncaoModel> funcoes = colaboradorFuncaoRepository
                .findByColaboradorIdAndAtivoFalse(colaborador.getId());
        return mapper.toResumo(colaborador, funcoes);
    }

    /**
     * Função: Localiza informações de colaborador conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    private ColaboradorModel buscarColaboradorAtivo(Long id) {
        return colaboradorRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Colaborador não encontrado ou inativo."));
    }

    /**
     * Função: Localiza informações de colaborador conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    private FuncaoModel buscarFuncaoAtiva(Long id) {
        return funcaoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Função não encontrada ou inativa."));
    }
}
