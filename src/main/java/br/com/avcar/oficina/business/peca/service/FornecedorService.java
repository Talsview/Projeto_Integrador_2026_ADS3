package br.com.avcar.oficina.business.peca.service;

import br.com.avcar.oficina.business.peca.dto.FornecedorDTO;
import br.com.avcar.oficina.business.peca.mapper.FornecedorMapper;
import br.com.avcar.oficina.business.peca.model.FornecedorModel;
import br.com.avcar.oficina.business.peca.repository.IFornecedorRepository;
import br.com.avcar.oficina.business.peca.validation.FornecedorValidation;
import br.com.avcar.oficina.core.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsável pelas regras de negócio de Fornecedor.
 */
@Service
public class FornecedorService {

    private final IFornecedorRepository fornecedorRepository;
    private final FornecedorValidation validation;
    private final FornecedorMapper mapper;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public FornecedorService(IFornecedorRepository fornecedorRepository,
                             FornecedorValidation validation,
                             FornecedorMapper mapper) {
        this.fornecedorRepository = fornecedorRepository;
        this.validation = validation;
        this.mapper = mapper;
    }

    @Transactional
    /**
     * Função: Valida os dados recebidos, monta as entidades necessárias e grava o cadastro de peca.
     * Uso no sistema: centraliza a regra de cadastro na camada Service, mantendo Controller e tela
     * mais simples.
     */
    public FornecedorDTO cadastrar(FornecedorDTO dto) {
        validation.validateInsert(dto);
        FornecedorModel saved = fornecedorRepository.save(mapper.toModel(dto));
        return mapper.toDto(saved);
    }

    @Transactional
    /**
     * Função: Busca o registro ativo, aplica as alterações permitidas e salva a atualização de peca.
     * Uso no sistema: garante que alterações passem por validação e não quebrem vínculos já existentes
     * no sistema.
     */
    public FornecedorDTO atualizar(Long id, FornecedorDTO dto) {
        validation.validateUpdate(id, dto);
        FornecedorModel fornecedor = buscarModelAtivo(id);
        mapper.atualizarModel(fornecedor, dto);
        return mapper.toDto(fornecedorRepository.save(fornecedor));
    }

    @Transactional(readOnly = true)
    /**
     * Função: Localiza informações de peca conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    public FornecedorDTO buscar(Long id) {
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
    public Page<FornecedorDTO> listar(Pageable pageable) {
        return fornecedorRepository.findAllByAtivoTrue(pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de peca aplicando filtros, paginação ou critérios de busca quando
     * informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<FornecedorDTO> pesquisar(String termo, Pageable pageable) {
        if (termo == null || termo.isBlank()) {
            return listar(pageable);
        }
        return fornecedorRepository.search(termo.trim(), pageable).map(mapper::toDto);
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
        FornecedorModel fornecedor = buscarModelAtivo(id);
        fornecedor.setAtivo(Boolean.FALSE);
        fornecedorRepository.save(fornecedor);
    }


    @Transactional(readOnly = true)
    /**
     * Função: Lista cadastros inativados para que o usuário possa localizar e reativar registros sem
     * recriá-los.
     * Uso no sistema: reforça a rastreabilidade, pois registros antigos continuam no banco e podem
     * voltar a ficar ativos.
     */
    public Page<FornecedorDTO> listarInativos(Pageable pageable) {
        return fornecedorRepository.findAllByAtivoFalse(pageable).map(mapper::toDto);
    }

    @Transactional
    /**
     * Função: Localiza um registro inativado, altera seu campo ativo para verdadeiro e salva a
     * reativação.
     * Uso no sistema: permite recuperar cadastros feitos anteriormente sem duplicar clientes,
     * veículos, peças ou serviços.
     */
    public FornecedorDTO ativar(Long id) {
        validation.validateId(id);
        FornecedorModel model = fornecedorRepository.findByIdAndAtivoFalse(id)
                .orElseThrow(() -> new BusinessException("Fornecedor não encontrado entre os inativos."));
        model.setAtivo(Boolean.TRUE);
        return mapper.toDto(fornecedorRepository.save(model));
    }

    /**
     * Função: Localiza informações de peca conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    public FornecedorModel buscarModelAtivo(Long id) {
        return fornecedorRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Fornecedor não encontrado ou inativo."));
    }
}
