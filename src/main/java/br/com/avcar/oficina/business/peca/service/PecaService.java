package br.com.avcar.oficina.business.peca.service;

import br.com.avcar.oficina.business.peca.dto.PecaDTO;
import br.com.avcar.oficina.business.peca.mapper.PecaMapper;
import br.com.avcar.oficina.business.peca.model.FornecedorModel;
import br.com.avcar.oficina.business.peca.model.PecaModel;
import br.com.avcar.oficina.business.peca.repository.IPecaRepository;
import br.com.avcar.oficina.business.peca.validation.PecaValidation;
import br.com.avcar.oficina.core.exception.BusinessException;
import br.com.avcar.oficina.core.service.GenericService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsável pelas regras de negócio de Peça.
 */
@Service
public class PecaService extends GenericService<PecaModel> {

    private final IPecaRepository pecaRepository;
    private final PecaValidation validation;
    private final PecaMapper mapper;
    private final FornecedorService fornecedorService;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public PecaService(IPecaRepository pecaRepository,
                       PecaValidation validation,
                       PecaMapper mapper,
                       FornecedorService fornecedorService) {
        super(pecaRepository, null);
        this.pecaRepository = pecaRepository;
        this.validation = validation;
        this.mapper = mapper;
        this.fornecedorService = fornecedorService;
    }

    @Transactional
    /**
     * Função: Valida os dados recebidos, monta as entidades necessárias e grava o cadastro de peca.
     * Uso no sistema: centraliza a regra de cadastro na camada Service, mantendo Controller e tela
     * mais simples.
     */
    public PecaDTO cadastrar(PecaDTO dto) {
        validation.validateInsert(dto);
        FornecedorModel fornecedorPadrao = buscarFornecedorPadrao(dto);
        PecaModel saved = pecaRepository.save(mapper.toModel(dto, fornecedorPadrao));
        return mapper.toDto(saved);
    }

    @Transactional
    /**
     * Função: Busca o registro ativo, aplica as alterações permitidas e salva a atualização de peca.
     * Uso no sistema: garante que alterações passem por validação e não quebrem vínculos já existentes
     * no sistema.
     */
    public PecaDTO atualizar(Long id, PecaDTO dto) {
        validation.validateUpdate(id, dto);
        PecaModel peca = buscarModelAtivo(id);
        FornecedorModel fornecedorPadrao = buscarFornecedorPadrao(dto);
        mapper.atualizarModel(peca, dto, fornecedorPadrao);
        return mapper.toDto(pecaRepository.save(peca));
    }

    @Transactional(readOnly = true)
    /**
     * Função: Localiza informações de peca conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    public PecaDTO buscar(Long id) {
        validation.validateId(id);
        return mapper.toDto(buscarModelAtivo(id));
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de peca aplicando filtros, paginação ou critérios de busca quando
     * informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<PecaDTO> listar(Pageable pageable) {
        return pecaRepository.findAllByAtivoTrue(pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de peca aplicando filtros, paginação ou critérios de busca quando
     * informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<PecaDTO> pesquisar(String termo, Pageable pageable) {
        if (termo == null || termo.isBlank()) {
            return listar(pageable);
        }
        return pecaRepository.search(termo.trim(), pageable).map(mapper::toDto);
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
        PecaModel peca = buscarModelAtivo(id);
        peca.setAtivo(Boolean.FALSE);
        pecaRepository.save(peca);
    }

    /**
     * Função: Localiza informações de peca conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    private FornecedorModel buscarFornecedorPadrao(PecaDTO dto) {
        if (dto.getIdFornecedorPadrao() == null || dto.getIdFornecedorPadrao() <= 0) {
            return null;
        }
        return fornecedorService.buscarModelAtivo(dto.getIdFornecedorPadrao());
    }


    @Transactional(readOnly = true)
    /**
     * Função: Lista cadastros inativados para que o usuário possa localizar e reativar registros sem
     * recriá-los.
     * Uso no sistema: reforça a rastreabilidade, pois registros antigos continuam no banco e podem
     * voltar a ficar ativos.
     */
    public Page<PecaDTO> listarInativos(Pageable pageable) {
        return pecaRepository.findAllByAtivoFalse(pageable).map(mapper::toDto);
    }

    @Transactional
    /**
     * Função: Localiza um registro inativado, altera seu campo ativo para verdadeiro e salva a
     * reativação.
     * Uso no sistema: permite recuperar cadastros feitos anteriormente sem duplicar clientes,
     * veículos, peças ou serviços.
     */
    public PecaDTO ativar(Long id) {
        validation.validateId(id);
        PecaModel model = pecaRepository.findByIdAndAtivoFalse(id)
                .orElseThrow(() -> new BusinessException("Peça não encontrado entre os inativos."));
        model.setAtivo(Boolean.TRUE);
        return mapper.toDto(pecaRepository.save(model));
    }

    /**
     * Função: Localiza informações de peca conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    public PecaModel buscarModelAtivo(Long id) {
        return pecaRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Peça não encontrada ou inativa."));
    }
}
