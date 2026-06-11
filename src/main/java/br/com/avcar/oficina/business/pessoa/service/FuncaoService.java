package br.com.avcar.oficina.business.pessoa.service;

import br.com.avcar.oficina.business.pessoa.dto.FuncaoDTO;
import br.com.avcar.oficina.business.pessoa.mapper.FuncaoMapper;
import br.com.avcar.oficina.business.pessoa.model.FuncaoModel;
import br.com.avcar.oficina.business.pessoa.repository.IFuncaoRepository;
import br.com.avcar.oficina.business.pessoa.validation.FuncaoValidation;
import br.com.avcar.oficina.core.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsável pelas regras de negócio de Função.
 */
@Service
public class FuncaoService {

    private final IFuncaoRepository funcaoRepository;
    private final FuncaoValidation validation;
    private final FuncaoMapper mapper;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public FuncaoService(IFuncaoRepository funcaoRepository,
                         FuncaoValidation validation,
                         FuncaoMapper mapper) {
        this.funcaoRepository = funcaoRepository;
        this.validation = validation;
        this.mapper = mapper;
    }

    @Transactional
    /**
     * Função: Valida os dados recebidos, monta as entidades necessárias e grava o cadastro de funcao.
     * Uso no sistema: centraliza a regra de cadastro na camada Service, mantendo Controller e tela
     * mais simples.
     */
    public FuncaoDTO cadastrar(FuncaoDTO dto) {
        validation.validateInsert(dto);
        FuncaoModel saved = funcaoRepository.save(mapper.toModel(dto));
        return mapper.toDto(saved);
    }

    @Transactional
    /**
     * Função: Busca o registro ativo, aplica as alterações permitidas e salva a atualização de funcao.
     * Uso no sistema: garante que alterações passem por validação e não quebrem vínculos já existentes
     * no sistema.
     */
    public FuncaoDTO atualizar(Long id, FuncaoDTO dto) {
        validation.validateUpdate(id, dto);
        FuncaoModel funcao = buscarModelAtivo(id);
        mapper.atualizarModel(funcao, dto);
        return mapper.toDto(funcaoRepository.save(funcao));
    }

    @Transactional(readOnly = true)
    /**
     * Função: Localiza informações de funcao conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    public FuncaoDTO buscar(Long id) {
        validation.validateId(id);
        return mapper.toDto(buscarModelAtivo(id));
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de funcao aplicando filtros, paginação ou critérios de busca quando
     * informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<FuncaoDTO> listar(Pageable pageable) {
        return funcaoRepository.findAllByAtivoTrue(pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de funcao aplicando filtros, paginação ou critérios de busca quando
     * informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<FuncaoDTO> pesquisar(String termo, Pageable pageable) {
        if (termo == null || termo.isBlank()) {
            return listar(pageable);
        }
        return funcaoRepository.findByNomeFuncaoContainingIgnoreCaseAndAtivoTrue(termo.trim(), pageable).map(mapper::toDto);
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
        FuncaoModel funcao = buscarModelAtivo(id);
        funcao.setAtivo(Boolean.FALSE);
        funcaoRepository.save(funcao);
    }


    @Transactional(readOnly = true)
    /**
     * Função: Lista cadastros inativados para que o usuário possa localizar e reativar registros sem
     * recriá-los.
     * Uso no sistema: reforça a rastreabilidade, pois registros antigos continuam no banco e podem
     * voltar a ficar ativos.
     */
    public Page<FuncaoDTO> listarInativos(Pageable pageable) {
        return funcaoRepository.findAllByAtivoFalse(pageable).map(mapper::toDto);
    }

    @Transactional
    /**
     * Função: Localiza um registro inativado, altera seu campo ativo para verdadeiro e salva a
     * reativação.
     * Uso no sistema: permite recuperar cadastros feitos anteriormente sem duplicar clientes,
     * veículos, peças ou serviços.
     */
    public FuncaoDTO ativar(Long id) {
        validation.validateId(id);
        FuncaoModel model = funcaoRepository.findByIdAndAtivoFalse(id)
                .orElseThrow(() -> new BusinessException("Função não encontrado entre os inativos."));
        model.setAtivo(Boolean.TRUE);
        return mapper.toDto(funcaoRepository.save(model));
    }

    /**
     * Função: Localiza informações de funcao conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    private FuncaoModel buscarModelAtivo(Long id) {
        return funcaoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Função não encontrada ou inativa."));
    }
}
