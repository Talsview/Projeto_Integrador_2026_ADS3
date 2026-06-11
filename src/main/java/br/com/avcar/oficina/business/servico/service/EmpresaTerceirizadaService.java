package br.com.avcar.oficina.business.servico.service;

import br.com.avcar.oficina.business.servico.dto.EmpresaTerceirizadaDTO;
import br.com.avcar.oficina.business.servico.mapper.EmpresaTerceirizadaMapper;
import br.com.avcar.oficina.business.servico.model.EmpresaTerceirizadaModel;
import br.com.avcar.oficina.business.servico.repository.IEmpresaTerceirizadaRepository;
import br.com.avcar.oficina.business.servico.validation.EmpresaTerceirizadaValidation;
import br.com.avcar.oficina.core.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsável pelas regras de negócio de Empresa Terceirizada.
 */
@Service
public class EmpresaTerceirizadaService {

    private final IEmpresaTerceirizadaRepository empresaRepository;
    private final EmpresaTerceirizadaValidation validation;
    private final EmpresaTerceirizadaMapper mapper;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public EmpresaTerceirizadaService(IEmpresaTerceirizadaRepository empresaRepository,
                                      EmpresaTerceirizadaValidation validation,
                                      EmpresaTerceirizadaMapper mapper) {
        this.empresaRepository = empresaRepository;
        this.validation = validation;
        this.mapper = mapper;
    }

    @Transactional
    /**
     * Função: Valida os dados recebidos, monta as entidades necessárias e grava o cadastro de servico.
     * Uso no sistema: centraliza a regra de cadastro na camada Service, mantendo Controller e tela
     * mais simples.
     */
    public EmpresaTerceirizadaDTO cadastrar(EmpresaTerceirizadaDTO dto) {
        validation.validateInsert(dto);
        EmpresaTerceirizadaModel saved = empresaRepository.save(mapper.toModel(dto));
        return mapper.toDto(saved);
    }

    @Transactional
    /**
     * Função: Busca o registro ativo, aplica as alterações permitidas e salva a atualização de
     * servico.
     * Uso no sistema: garante que alterações passem por validação e não quebrem vínculos já existentes
     * no sistema.
     */
    public EmpresaTerceirizadaDTO atualizar(Long id, EmpresaTerceirizadaDTO dto) {
        validation.validateUpdate(id, dto);
        EmpresaTerceirizadaModel empresa = buscarModelAtivo(id);
        mapper.atualizarModel(empresa, dto);
        return mapper.toDto(empresaRepository.save(empresa));
    }

    @Transactional(readOnly = true)
    /**
     * Função: Localiza informações de servico conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    public EmpresaTerceirizadaDTO buscar(Long id) {
        validation.validateId(id);
        return mapper.toDto(buscarModelAtivo(id));
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de servico aplicando filtros, paginação ou critérios de busca quando
     * informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<EmpresaTerceirizadaDTO> listar(Pageable pageable) {
        return empresaRepository.findAllByAtivoTrue(pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de servico aplicando filtros, paginação ou critérios de busca quando
     * informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<EmpresaTerceirizadaDTO> pesquisar(String termo, Pageable pageable) {
        if (termo == null || termo.isBlank()) {
            return listar(pageable);
        }
        return empresaRepository.search(termo.trim(), pageable).map(mapper::toDto);
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
        EmpresaTerceirizadaModel empresa = buscarModelAtivo(id);
        empresa.setAtivo(Boolean.FALSE);
        empresaRepository.save(empresa);
    }


    @Transactional(readOnly = true)
    /**
     * Função: Lista cadastros inativados para que o usuário possa localizar e reativar registros sem
     * recriá-los.
     * Uso no sistema: reforça a rastreabilidade, pois registros antigos continuam no banco e podem
     * voltar a ficar ativos.
     */
    public Page<EmpresaTerceirizadaDTO> listarInativos(Pageable pageable) {
        return empresaRepository.findAllByAtivoFalse(pageable).map(mapper::toDto);
    }

    @Transactional
    /**
     * Função: Localiza um registro inativado, altera seu campo ativo para verdadeiro e salva a
     * reativação.
     * Uso no sistema: permite recuperar cadastros feitos anteriormente sem duplicar clientes,
     * veículos, peças ou serviços.
     */
    public EmpresaTerceirizadaDTO ativar(Long id) {
        validation.validateId(id);
        EmpresaTerceirizadaModel model = empresaRepository.findByIdAndAtivoFalse(id)
                .orElseThrow(() -> new BusinessException("Empresa terceirizada não encontrado entre os inativos."));
        model.setAtivo(Boolean.TRUE);
        return mapper.toDto(empresaRepository.save(model));
    }

    /**
     * Função: Localiza informações de servico conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    public EmpresaTerceirizadaModel buscarModelAtivo(Long id) {
        return empresaRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Empresa terceirizada não encontrada ou inativa."));
    }
}
