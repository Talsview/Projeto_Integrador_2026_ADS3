package br.com.avcar.oficina.business.servico.service;

import br.com.avcar.oficina.business.servico.dto.ServicoDTO;
import br.com.avcar.oficina.business.servico.enums.TipoServico;
import br.com.avcar.oficina.business.servico.mapper.ServicoMapper;
import br.com.avcar.oficina.business.servico.model.EmpresaTerceirizadaModel;
import br.com.avcar.oficina.business.servico.model.ServicoInternoModel;
import br.com.avcar.oficina.business.servico.model.ServicoModel;
import br.com.avcar.oficina.business.servico.model.ServicoTerceirizadoModel;
import br.com.avcar.oficina.business.servico.repository.IServicoInternoRepository;
import br.com.avcar.oficina.business.servico.repository.IServicoRepository;
import br.com.avcar.oficina.business.servico.repository.IServicoTerceirizadoRepository;
import br.com.avcar.oficina.business.servico.validation.ServicoValidation;
import br.com.avcar.oficina.core.exception.BusinessException;
import br.com.avcar.oficina.core.exception.RuleValidationException;
import br.com.avcar.oficina.core.service.GenericService;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsável pelas regras de negócio de Serviço.
 *
 * Regra de negócio implementada: todo Serviço deve ser classificado como
 * Interno ou Terceirizado, de forma exclusiva e total. Serviços terceirizados
 * permanecem sob responsabilidade da oficina perante o cliente.
 */
@Service
public class ServicoService extends GenericService<ServicoModel> {

    private final IServicoRepository servicoRepository;
    private final IServicoInternoRepository servicoInternoRepository;
    private final IServicoTerceirizadoRepository servicoTerceirizadoRepository;
    private final ServicoValidation validation;
    private final ServicoMapper mapper;
    private final EmpresaTerceirizadaService empresaTerceirizadaService;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public ServicoService(IServicoRepository servicoRepository,
                          IServicoInternoRepository servicoInternoRepository,
                          IServicoTerceirizadoRepository servicoTerceirizadoRepository,
                          ServicoValidation validation,
                          ServicoMapper mapper,
                          EmpresaTerceirizadaService empresaTerceirizadaService) {
        super(servicoRepository, null);
        this.servicoRepository = servicoRepository;
        this.servicoInternoRepository = servicoInternoRepository;
        this.servicoTerceirizadoRepository = servicoTerceirizadoRepository;
        this.validation = validation;
        this.mapper = mapper;
        this.empresaTerceirizadaService = empresaTerceirizadaService;
    }

    @Transactional
    /**
     * Função: Valida os dados recebidos, monta as entidades necessárias e grava o cadastro de servico.
     * Uso no sistema: centraliza a regra de cadastro na camada Service, mantendo Controller e tela
     * mais simples.
     */
    public ServicoDTO cadastrar(ServicoDTO dto) {
        validation.validateInsert(dto);

        ServicoModel servico = mapper.toServicoModel(dto);
        ServicoModel saved = servicoRepository.save(servico);

        salvarEspecializacao(saved, dto);
        return buscar(saved.getId());
    }

    @Transactional
    /**
     * Função: Busca o registro ativo, aplica as alterações permitidas e salva a atualização de
     * servico.
     * Uso no sistema: garante que alterações passem por validação e não quebrem vínculos já existentes
     * no sistema.
     */
    public ServicoDTO atualizar(Long id, ServicoDTO dto) {
        validation.validateUpdate(id, dto);

        ServicoModel servico = buscarModelAtivo(id);
        mapper.atualizarServicoModel(servico, dto);
        ServicoModel saved = servicoRepository.save(servico);

        atualizarEspecializacao(saved, dto);
        return buscar(saved.getId());
    }

    @Transactional(readOnly = true)
    /**
     * Função: Localiza informações de servico conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    public ServicoDTO buscar(Long id) {
        validation.validateId(id);
        ServicoModel servico = buscarModelAtivo(id);
        return montarDto(servico);
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de servico aplicando filtros, paginação ou critérios de busca quando
     * informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<ServicoDTO> listar(Pageable pageable) {
        return servicoRepository.findAllByAtivoTrue(pageable).map(this::montarDto);
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de servico aplicando filtros, paginação ou critérios de busca quando
     * informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<ServicoDTO> listarPorTipo(TipoServico tipoServico, Pageable pageable) {
        if (tipoServico == null) {
            throw new RuleValidationException("O tipo do serviço é obrigatório para a consulta.");
        }

        if (tipoServico == TipoServico.INTERNO) {
            return servicoRepository.findServicosInternosAtivos(pageable).map(this::montarDto);
        }
        return servicoRepository.findServicosTerceirizadosAtivos(pageable).map(this::montarDto);
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de servico aplicando filtros, paginação ou critérios de busca quando
     * informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<ServicoDTO> pesquisar(String termo, Pageable pageable) {
        if (termo == null || termo.isBlank()) {
            return listar(pageable);
        }
        return servicoRepository.search(termo.trim(), pageable).map(this::montarDto);
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
        ServicoModel servico = buscarModelAtivo(id);
        servico.setAtivo(Boolean.FALSE);
        servicoRepository.save(servico);

        servicoInternoRepository.findByIdAndAtivoTrue(id).ifPresent(interno -> {
            interno.setAtivo(Boolean.FALSE);
            servicoInternoRepository.save(interno);
        });

        servicoTerceirizadoRepository.findByIdAndAtivoTrue(id).ifPresent(terceirizado -> {
            terceirizado.setAtivo(Boolean.FALSE);
            servicoTerceirizadoRepository.save(terceirizado);
        });
    }



    @Transactional(readOnly = true)
    /**
     * Função: Lista cadastros inativados para que o usuário possa localizar e reativar registros sem
     * recriá-los.
     * Uso no sistema: reforça a rastreabilidade, pois registros antigos continuam no banco e podem
     * voltar a ficar ativos.
     */
    public Page<ServicoDTO> listarInativos(Pageable pageable) {
        return servicoRepository.findAllByAtivoFalse(pageable).map(this::montarDtoInativo);
    }

    @Transactional
    /**
     * Função: Localiza um registro inativado, altera seu campo ativo para verdadeiro e salva a
     * reativação.
     * Uso no sistema: permite recuperar cadastros feitos anteriormente sem duplicar clientes,
     * veículos, peças ou serviços.
     */
    public ServicoDTO ativar(Long id) {
        validation.validateId(id);
        ServicoModel servico = servicoRepository.findByIdAndAtivoFalse(id)
                .orElseThrow(() -> new BusinessException("Serviço não encontrado entre os inativos."));
        servico.setAtivo(Boolean.TRUE);
        servicoRepository.save(servico);

        servicoInternoRepository.findByIdAndAtivoFalse(id).ifPresent(interno -> {
            interno.setAtivo(Boolean.TRUE);
            servicoInternoRepository.save(interno);
        });

        servicoTerceirizadoRepository.findByIdAndAtivoFalse(id).ifPresent(terceirizado -> {
            terceirizado.setAtivo(Boolean.TRUE);
            servicoTerceirizadoRepository.save(terceirizado);
        });

        return montarDtoInativo(servico);
    }

    /**
     * Função: Localiza informações de servico conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    public ServicoModel buscarModelAtivo(Long id) {
        return servicoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Serviço não encontrado ou inativo."));
    }

    /**
     * Função: Valida os dados recebidos, monta as entidades necessárias e grava o cadastro de servico.
     * Uso no sistema: centraliza a regra de cadastro na camada Service, mantendo Controller e tela
     * mais simples.
     */
    private void salvarEspecializacao(ServicoModel servico, ServicoDTO dto) {
        if (dto.getTipoServico() == TipoServico.INTERNO) {
            servicoInternoRepository.save(mapper.toServicoInternoModel(servico, dto));
            return;
        }
        EmpresaTerceirizadaModel empresaPadrao = empresaTerceirizadaService.buscarModelAtivo(dto.getIdEmpresaTerceirizadaPadrao());
        servicoTerceirizadoRepository.save(mapper.toServicoTerceirizadoModel(servico, dto, empresaPadrao));
    }

    /**
     * Função: Busca o registro ativo, aplica as alterações permitidas e salva a atualização de
     * servico.
     * Uso no sistema: garante que alterações passem por validação e não quebrem vínculos já existentes
     * no sistema.
     */
    private void atualizarEspecializacao(ServicoModel servico, ServicoDTO dto) {
        if (dto.getTipoServico() == TipoServico.INTERNO) {
            inativarEspecializacaoTerceirizada(servico.getId());
            ServicoInternoModel interno = servicoInternoRepository.findById(servico.getId())
                    .orElseGet(ServicoInternoModel::new);
            mapper.atualizarServicoInternoModel(interno, servico, dto);
            servicoInternoRepository.save(interno);
            return;
        }

        inativarEspecializacaoInterna(servico.getId());
        ServicoTerceirizadoModel terceirizado = servicoTerceirizadoRepository.findById(servico.getId())
                .orElseGet(ServicoTerceirizadoModel::new);
        EmpresaTerceirizadaModel empresaPadrao = empresaTerceirizadaService.buscarModelAtivo(dto.getIdEmpresaTerceirizadaPadrao());
        mapper.atualizarServicoTerceirizadoModel(terceirizado, servico, dto, empresaPadrao);
        servicoTerceirizadoRepository.save(terceirizado);
    }

    /**
     * Função: Localiza um registro inativado, altera seu campo ativo para verdadeiro e salva a
     * reativação.
     * Uso no sistema: permite recuperar cadastros feitos anteriormente sem duplicar clientes,
     * veículos, peças ou serviços.
     */
    private void inativarEspecializacaoInterna(Long idServico) {
        servicoInternoRepository.findByIdAndAtivoTrue(idServico).ifPresent(interno -> {
            interno.setAtivo(Boolean.FALSE);
            servicoInternoRepository.save(interno);
        });
    }

    /**
     * Função: Localiza um registro inativado, altera seu campo ativo para verdadeiro e salva a
     * reativação.
     * Uso no sistema: permite recuperar cadastros feitos anteriormente sem duplicar clientes,
     * veículos, peças ou serviços.
     */
    private void inativarEspecializacaoTerceirizada(Long idServico) {
        servicoTerceirizadoRepository.findByIdAndAtivoTrue(idServico).ifPresent(terceirizado -> {
            terceirizado.setAtivo(Boolean.FALSE);
            servicoTerceirizadoRepository.save(terceirizado);
        });
    }

    /**
     * Função: Monta o objeto ou resposta necessária para a operação montar dto.
     * Uso no sistema: isola a preparação dos dados e melhora a legibilidade do fluxo principal.
     */
    private ServicoDTO montarDto(ServicoModel servico) {
        Optional<ServicoInternoModel> interno = servicoInternoRepository.findByIdAndAtivoTrue(servico.getId());
        Optional<ServicoTerceirizadoModel> terceirizado = servicoTerceirizadoRepository.findByIdAndAtivoTrue(servico.getId());
        return mapper.toDto(servico, interno.orElse(null), terceirizado.orElse(null));
    }

    /**
     * Função: Monta o objeto ou resposta necessária para a operação montar dto inativo.
     * Uso no sistema: isola a preparação dos dados e melhora a legibilidade do fluxo principal.
     */
    private ServicoDTO montarDtoInativo(ServicoModel servico) {
        Optional<ServicoInternoModel> interno = servicoInternoRepository.findByIdAndAtivoFalse(servico.getId());
        Optional<ServicoTerceirizadoModel> terceirizado = servicoTerceirizadoRepository.findByIdAndAtivoFalse(servico.getId());
        return mapper.toDto(servico, interno.orElse(null), terceirizado.orElse(null));
    }

}
