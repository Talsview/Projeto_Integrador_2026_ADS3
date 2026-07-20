package br.com.avcar.oficina.business.veiculo.service;

import br.com.avcar.oficina.business.veiculo.dto.MarcaDTO;
import br.com.avcar.oficina.business.veiculo.mapper.MarcaMapper;
import br.com.avcar.oficina.business.veiculo.model.MarcaModel;
import br.com.avcar.oficina.business.veiculo.repository.IMarcaRepository;
import br.com.avcar.oficina.business.veiculo.validation.MarcaValidation;
import br.com.avcar.oficina.core.exception.BusinessException;
import br.com.avcar.oficina.core.service.GenericService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsável pelas regras de negócio de Marca.
 */
@Service
public class MarcaService extends GenericService<MarcaModel> {

    private final IMarcaRepository marcaRepository;
    private final MarcaValidation validation;
    private final MarcaMapper mapper;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public MarcaService(IMarcaRepository marcaRepository,
                        MarcaValidation validation,
                        MarcaMapper mapper) {
        super(marcaRepository, null);
        this.marcaRepository = marcaRepository;
        this.validation = validation;
        this.mapper = mapper;
    }

    @Transactional
    /**
     * Função: Valida os dados recebidos, monta as entidades necessárias e grava o cadastro de veiculo.
     * Uso no sistema: centraliza a regra de cadastro na camada Service, mantendo Controller e tela
     * mais simples.
     */
    public MarcaDTO cadastrar(MarcaDTO dto) {
        validation.validateInsert(dto);
        MarcaModel saved = marcaRepository.save(mapper.toModel(dto));
        return mapper.toDto(saved);
    }

    @Transactional
    /**
     * Função: Busca o registro ativo, aplica as alterações permitidas e salva a atualização de
     * veiculo.
     * Uso no sistema: garante que alterações passem por validação e não quebrem vínculos já existentes
     * no sistema.
     */
    public MarcaDTO atualizar(Long id, MarcaDTO dto) {
        validation.validateUpdate(id, dto);
        MarcaModel marca = buscarModelAtivo(id);
        mapper.atualizarModel(marca, dto);
        return mapper.toDto(marcaRepository.save(marca));
    }

    @Transactional(readOnly = true)
    /**
     * Função: Localiza informações de veiculo conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    public MarcaDTO buscar(Long id) {
        validation.validateId(id);
        return mapper.toDto(buscarModelAtivo(id));
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de veiculo aplicando filtros, paginação ou critérios de busca quando
     * informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<MarcaDTO> listar(Pageable pageable) {
        return marcaRepository.findAllByAtivoTrue(pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de veiculo aplicando filtros, paginação ou critérios de busca quando
     * informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<MarcaDTO> pesquisar(String termo, Pageable pageable) {
        if (termo == null || termo.isBlank()) {
            return listar(pageable);
        }
        return marcaRepository.findByNomeMarcaContainingIgnoreCaseAndAtivoTrue(termo.trim(), pageable).map(mapper::toDto);
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
        MarcaModel marca = buscarModelAtivo(id);
        marca.setAtivo(Boolean.FALSE);
        marcaRepository.save(marca);
    }


    @Transactional(readOnly = true)
    /**
     * Função: Lista cadastros inativados para que o usuário possa localizar e reativar registros sem
     * recriá-los.
     * Uso no sistema: reforça a rastreabilidade, pois registros antigos continuam no banco e podem
     * voltar a ficar ativos.
     */
    public Page<MarcaDTO> listarInativos(Pageable pageable) {
        return marcaRepository.findAllByAtivoFalse(pageable).map(mapper::toDto);
    }

    @Transactional
    /**
     * Função: Localiza um registro inativado, altera seu campo ativo para verdadeiro e salva a
     * reativação.
     * Uso no sistema: permite recuperar cadastros feitos anteriormente sem duplicar clientes,
     * veículos, peças ou serviços.
     */
    public MarcaDTO ativar(Long id) {
        validation.validateId(id);
        MarcaModel model = marcaRepository.findByIdAndAtivoFalse(id)
                .orElseThrow(() -> new BusinessException("Marca não encontrado entre os inativos."));
        model.setAtivo(Boolean.TRUE);
        return mapper.toDto(marcaRepository.save(model));
    }

    /**
     * Função: Localiza informações de veiculo conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    public MarcaModel buscarModelAtivo(Long id) {
        return marcaRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Marca não encontrada ou inativa."));
    }
}
