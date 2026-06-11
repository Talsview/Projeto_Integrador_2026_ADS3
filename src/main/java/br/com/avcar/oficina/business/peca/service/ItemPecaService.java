package br.com.avcar.oficina.business.peca.service;

import br.com.avcar.oficina.business.garantia.service.GarantiaService;
import br.com.avcar.oficina.business.ordemservico.service.OrdemServicoService;
import br.com.avcar.oficina.business.peca.dto.ItemPecaDTO;
import br.com.avcar.oficina.business.peca.mapper.ItemPecaMapper;
import br.com.avcar.oficina.business.peca.model.FornecedorModel;
import br.com.avcar.oficina.business.peca.model.ItemPecaModel;
import br.com.avcar.oficina.business.peca.model.PecaModel;
import br.com.avcar.oficina.business.peca.repository.IItemPecaRepository;
import br.com.avcar.oficina.business.peca.validation.ItemPecaValidation;
import br.com.avcar.oficina.core.exception.BusinessException;
import java.math.BigDecimal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsável pelas regras de negócio de ItemPeca.
 *
 * Regra de negócio implementada: toda peça aplicada em OS deve estar vinculada
 * a uma peça cadastrada e a um fornecedor identificado.
 */
@Service
public class ItemPecaService {

    private final IItemPecaRepository itemPecaRepository;
    private final PecaService pecaService;
    private final FornecedorService fornecedorService;
    private final OrdemServicoService ordemServicoService;
    private final GarantiaService garantiaService;
    private final ItemPecaValidation validation;
    private final ItemPecaMapper mapper;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public ItemPecaService(IItemPecaRepository itemPecaRepository,
                           PecaService pecaService,
                           FornecedorService fornecedorService,
                           OrdemServicoService ordemServicoService,
                           GarantiaService garantiaService,
                           ItemPecaValidation validation,
                           ItemPecaMapper mapper) {
        this.itemPecaRepository = itemPecaRepository;
        this.pecaService = pecaService;
        this.fornecedorService = fornecedorService;
        this.ordemServicoService = ordemServicoService;
        this.garantiaService = garantiaService;
        this.validation = validation;
        this.mapper = mapper;
    }

    @Transactional
    /**
     * Função: Valida os dados recebidos, monta as entidades necessárias e grava o cadastro de peca.
     * Uso no sistema: centraliza a regra de cadastro na camada Service, mantendo Controller e tela
     * mais simples.
     */
    public ItemPecaDTO cadastrar(ItemPecaDTO dto) {
        if (dto == null) {
            validation.validateInsert(null);
        }
        ordemServicoService.validarOrdemEmOrcamento(dto.getIdOrdemServico());
        PecaModel peca = pecaService.buscarModelAtivo(dto.getIdPeca());
        completarDadosAutomaticosDaPeca(dto, peca);
        validation.validateInsert(dto);
        FornecedorModel fornecedor = fornecedorService.buscarModelAtivo(dto.getIdFornecedor());
        ItemPecaModel saved = itemPecaRepository.save(mapper.toModel(dto, peca, fornecedor));
        garantiaService.criarGarantiaPecaAguardando(saved);
        ordemServicoService.recalcularValorTotal(dto.getIdOrdemServico());
        return mapper.toDto(saved);
    }

    @Transactional
    /**
     * Função: Busca o registro ativo, aplica as alterações permitidas e salva a atualização de peca.
     * Uso no sistema: garante que alterações passem por validação e não quebrem vínculos já existentes
     * no sistema.
     */
    public ItemPecaDTO atualizar(Long id, ItemPecaDTO dto) {
        validation.validateId(id);
        if (dto == null) {
            validation.validateUpdate(id, null);
        }
        ItemPecaModel itemPeca = buscarModelAtivo(id);
        Long idOrdemServicoAnterior = itemPeca.getIdOrdemServico();
        ordemServicoService.validarOrdemEmOrcamento(idOrdemServicoAnterior);
        ordemServicoService.validarOrdemEmOrcamento(dto.getIdOrdemServico());
        PecaModel peca = pecaService.buscarModelAtivo(dto.getIdPeca());
        completarDadosAutomaticosDaPeca(dto, peca);
        validation.validateUpdate(id, dto);
        FornecedorModel fornecedor = fornecedorService.buscarModelAtivo(dto.getIdFornecedor());
        mapper.atualizarModel(itemPeca, dto, peca, fornecedor);
        ItemPecaModel saved = itemPecaRepository.save(itemPeca);
        garantiaService.criarGarantiaPecaAguardando(saved);
        ordemServicoService.recalcularValorTotal(dto.getIdOrdemServico());
        if (!idOrdemServicoAnterior.equals(dto.getIdOrdemServico())) {
            ordemServicoService.recalcularValorTotal(idOrdemServicoAnterior);
        }
        return mapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    /**
     * Função: Localiza informações de peca conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    public ItemPecaDTO buscar(Long id) {
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
    public Page<ItemPecaDTO> listar(Pageable pageable) {
        return itemPecaRepository.findAllByAtivoTrue(pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de peca aplicando filtros, paginação ou critérios de busca quando
     * informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<ItemPecaDTO> listarPorOrdemServico(Long idOrdemServico, Pageable pageable) {
        validation.validateIdOrdemServico(idOrdemServico);
        return itemPecaRepository.findAllByIdOrdemServicoAndAtivoTrue(idOrdemServico, pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de peca aplicando filtros, paginação ou critérios de busca quando
     * informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<ItemPecaDTO> pesquisarPorOrdemServico(Long idOrdemServico, String termo, Pageable pageable) {
        validation.validateIdOrdemServico(idOrdemServico);
        if (termo == null || termo.isBlank()) {
            return listarPorOrdemServico(idOrdemServico, pageable);
        }
        return itemPecaRepository.searchByOrdemServico(idOrdemServico, termo.trim(), pageable).map(mapper::toDto);
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
        ItemPecaModel itemPeca = buscarModelAtivo(id);
        ordemServicoService.validarOrdemEmOrcamento(itemPeca.getIdOrdemServico());
        garantiaService.inativarGarantiaPorItemPeca(itemPeca.getId());
        itemPeca.setAtivo(Boolean.FALSE);
        itemPecaRepository.save(itemPeca);
        ordemServicoService.recalcularValorTotal(itemPeca.getIdOrdemServico());
    }

    /**
     * Função: Processa dados de peça, fornecedor, quantidade ou valor unitário conforme a operação
     * solicitada.
     * Uso no sistema: garante rastreabilidade entre peça utilizada, fornecedor responsável e valor
     * aplicado na OS.
     */
    private void completarDadosAutomaticosDaPeca(ItemPecaDTO dto, PecaModel peca) {
        if ((dto.getIdFornecedor() == null || dto.getIdFornecedor() <= 0) && peca.getFornecedorPadrao() != null) {
            dto.setIdFornecedor(peca.getFornecedorPadrao().getId());
        }
        if ((dto.getValorUnitario() == null || dto.getValorUnitario().compareTo(BigDecimal.ZERO) == 0)
                && peca.getValorUnitarioPadrao() != null
                && peca.getValorUnitarioPadrao().compareTo(BigDecimal.ZERO) > 0) {
            dto.setValorUnitario(peca.getValorUnitarioPadrao());
        }
        if (dto.getQuantidade() == null) {
            dto.setQuantidade(BigDecimal.ONE);
        }
    }

    /**
     * Função: Localiza informações de peca conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    public ItemPecaModel buscarModelAtivo(Long id) {
        return itemPecaRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Item de peça não encontrado ou inativo."));
    }
}
