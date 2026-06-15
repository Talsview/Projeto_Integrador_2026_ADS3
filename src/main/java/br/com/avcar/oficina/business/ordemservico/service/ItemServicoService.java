package br.com.avcar.oficina.business.ordemservico.service;

import br.com.avcar.oficina.business.garantia.service.GarantiaService;
import br.com.avcar.oficina.business.ordemservico.dto.ItemServicoDTO;
import br.com.avcar.oficina.business.ordemservico.mapper.ItemServicoMapper;
import br.com.avcar.oficina.business.ordemservico.model.ExecucaoServicoTerceirizadoModel;
import br.com.avcar.oficina.business.ordemservico.model.ItemServicoModel;
import br.com.avcar.oficina.business.ordemservico.model.OrdemServicoModel;
import br.com.avcar.oficina.business.ordemservico.repository.IExecucaoServicoTerceirizadoRepository;
import br.com.avcar.oficina.business.ordemservico.repository.IItemServicoRepository;
import br.com.avcar.oficina.business.ordemservico.validation.ItemServicoValidation;
import br.com.avcar.oficina.business.pessoa.model.ColaboradorModel;
import br.com.avcar.oficina.business.pessoa.repository.IColaboradorRepository;
import br.com.avcar.oficina.business.servico.model.EmpresaTerceirizadaModel;
import br.com.avcar.oficina.business.servico.model.ServicoModel;
import br.com.avcar.oficina.business.servico.model.ServicoTerceirizadoModel;
import br.com.avcar.oficina.business.servico.repository.IServicoTerceirizadoRepository;
import br.com.avcar.oficina.business.servico.service.EmpresaTerceirizadaService;
import br.com.avcar.oficina.business.servico.service.ServicoService;
import br.com.avcar.oficina.core.exception.BusinessException;
import br.com.avcar.oficina.core.exception.RuleValidationException;
import java.math.BigDecimal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsável pelos serviços executados dentro da OS.
 *
 * Regras atendidas:
 * - Todo ItemServico possui Serviço cadastrado.
 * - Serviço interno possui Colaborador responsável da oficina.
 * - Serviço terceirizado gera ExecucaoServicoTerceirizado com empresa executora.
 * - Serviço terceirizado não exige colaborador interno, pois a execução é feita por empresa externa.
 */
@Service
public class ItemServicoService {

    private final IItemServicoRepository itemServicoRepository;
    private final IExecucaoServicoTerceirizadoRepository execucaoRepository;
    private final IColaboradorRepository colaboradorRepository;
    private final IServicoTerceirizadoRepository servicoTerceirizadoRepository;
    private final OrdemServicoService ordemServicoService;
    private final ServicoService servicoService;
    private final EmpresaTerceirizadaService empresaTerceirizadaService;
    private final GarantiaService garantiaService;
    private final ItemServicoValidation validation;
    private final ItemServicoMapper mapper;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public ItemServicoService(IItemServicoRepository itemServicoRepository,
                              IExecucaoServicoTerceirizadoRepository execucaoRepository,
                              IColaboradorRepository colaboradorRepository,
                              IServicoTerceirizadoRepository servicoTerceirizadoRepository,
                              OrdemServicoService ordemServicoService,
                              ServicoService servicoService,
                              EmpresaTerceirizadaService empresaTerceirizadaService,
                              GarantiaService garantiaService,
                              ItemServicoValidation validation,
                              ItemServicoMapper mapper) {
        this.itemServicoRepository = itemServicoRepository;
        this.execucaoRepository = execucaoRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.servicoTerceirizadoRepository = servicoTerceirizadoRepository;
        this.ordemServicoService = ordemServicoService;
        this.servicoService = servicoService;
        this.empresaTerceirizadaService = empresaTerceirizadaService;
        this.garantiaService = garantiaService;
        this.validation = validation;
        this.mapper = mapper;
    }

    @Transactional
    /**
     * Função: Valida os dados recebidos, monta as entidades necessárias e grava o cadastro de
     * ordemservico.
     * Uso no sistema: centraliza a regra de cadastro na camada Service, mantendo Controller e tela
     * mais simples.
     */
    public ItemServicoDTO cadastrar(ItemServicoDTO dto) {
        if (dto == null) {
            validation.validateInsert(null);
        }
        ordemServicoService.validarOrdemEmOrcamento(dto.getIdOrdemServico());

        OrdemServicoModel ordemServico = ordemServicoService.buscarModelAtivo(dto.getIdOrdemServico());
        ServicoModel servico = servicoService.buscarModelAtivo(dto.getIdServico());
        boolean terceirizado = isServicoTerceirizado(servico);
        prepararDtoConformeTipoServico(dto, servico, terceirizado);
        validation.validateInsert(dto);
        validarResponsavelEExecucaoExterna(terceirizado, dto);
        ColaboradorModel colaborador = terceirizado ? null : buscarColaboradorAtivo(dto.getIdColaborador());

        ItemServicoModel saved = itemServicoRepository.save(mapper.toModel(dto, ordemServico, servico, colaborador));
        garantiaService.criarGarantiaServicoAguardando(saved);
        salvarOuAtualizarExecucaoTerceirizada(saved, dto, servico);
        ordemServicoService.recalcularValorTotal(ordemServico.getId());

        return buscar(saved.getId());
    }

    @Transactional
    /**
     * Função: Busca o registro ativo, aplica as alterações permitidas e salva a atualização de
     * ordemservico.
     * Uso no sistema: garante que alterações passem por validação e não quebrem vínculos já existentes
     * no sistema.
     */
    public ItemServicoDTO atualizar(Long id, ItemServicoDTO dto) {
        validation.validateId(id);
        if (dto == null) {
            validation.validateUpdate(id, null);
        }
        ItemServicoModel itemServico = buscarModelAtivo(id);
        Long idOrdemServicoAnterior = itemServico.getOrdemServico().getId();
        ordemServicoService.validarOrdemEmOrcamento(idOrdemServicoAnterior);

        OrdemServicoModel ordemServico = ordemServicoService.buscarModelAtivo(dto.getIdOrdemServico());
        ordemServicoService.validarOrdemEmOrcamento(ordemServico.getId());
        ServicoModel servico = servicoService.buscarModelAtivo(dto.getIdServico());
        boolean terceirizado = isServicoTerceirizado(servico);
        prepararDtoConformeTipoServico(dto, servico, terceirizado);
        validation.validateUpdate(id, dto);
        validarResponsavelEExecucaoExterna(terceirizado, dto);
        ColaboradorModel colaborador = terceirizado ? null : buscarColaboradorAtivo(dto.getIdColaborador());

        mapper.atualizarModel(itemServico, dto, ordemServico, servico, colaborador);
        ItemServicoModel saved = itemServicoRepository.save(itemServico);
        garantiaService.criarGarantiaServicoAguardando(saved);
        salvarOuAtualizarExecucaoTerceirizada(saved, dto, servico);
        ordemServicoService.recalcularValorTotal(ordemServico.getId());
        if (!idOrdemServicoAnterior.equals(ordemServico.getId())) {
            ordemServicoService.recalcularValorTotal(idOrdemServicoAnterior);
        }

        return buscar(saved.getId());
    }

    @Transactional(readOnly = true)
    /**
     * Função: Localiza informações de ordemservico conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    public ItemServicoDTO buscar(Long id) {
        validation.validateId(id);
        ItemServicoModel itemServico = buscarModelAtivo(id);
        return mapper.toDto(itemServico, buscarExecucaoTerceirizadaOuNula(id));
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de ordemservico aplicando filtros, paginação ou critérios de busca
     * quando informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<ItemServicoDTO> listar(Pageable pageable) {
        return itemServicoRepository.findAllByAtivoTrue(pageable)
                .map(item -> mapper.toDto(item, buscarExecucaoTerceirizadaOuNula(item.getId())));
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de ordemservico aplicando filtros, paginação ou critérios de busca
     * quando informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<ItemServicoDTO> listarPorOrdemServico(Long idOrdemServico, Pageable pageable) {
        validation.validateIdOrdemServico(idOrdemServico);
        return itemServicoRepository.findAllByOrdemServicoIdAndAtivoTrue(idOrdemServico, pageable)
                .map(item -> mapper.toDto(item, buscarExecucaoTerceirizadaOuNula(item.getId())));
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de ordemservico aplicando filtros, paginação ou critérios de busca
     * quando informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<ItemServicoDTO> pesquisarPorOrdemServico(Long idOrdemServico, String termo, Pageable pageable) {
        validation.validateIdOrdemServico(idOrdemServico);
        if (termo == null || termo.isBlank()) {
            return listarPorOrdemServico(idOrdemServico, pageable);
        }
        return itemServicoRepository.searchByOrdemServico(idOrdemServico, termo.trim(), pageable)
                .map(item -> mapper.toDto(item, buscarExecucaoTerceirizadaOuNula(item.getId())));
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
        ItemServicoModel itemServico = buscarModelAtivo(id);
        ordemServicoService.validarOrdemEmOrcamento(itemServico.getOrdemServico().getId());

        execucaoRepository.findByItemServicoIdAndAtivoTrue(id).ifPresent(execucao -> {
            execucao.setAtivo(Boolean.FALSE);
            execucaoRepository.save(execucao);
        });

        garantiaService.inativarGarantiaPorItemServico(itemServico.getId());
        itemServico.setAtivo(Boolean.FALSE);
        itemServicoRepository.save(itemServico);
        ordemServicoService.recalcularValorTotal(itemServico.getOrdemServico().getId());
    }

    /**
     * Função: Localiza informações de ordemservico conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    public ItemServicoModel buscarModelAtivo(Long id) {
        return itemServicoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Item de Serviço não encontrado ou inativo."));
    }

    /**
     * Função: completa quantidade e valor unitário a partir do cadastro do serviço.
     * Uso no sistema: mantém o orçamento da OS coerente mesmo quando o usuário não digita
     * manualmente o valor base já cadastrado para o serviço.
     */
    private void completarDadosAutomaticosDoServico(ItemServicoDTO dto, ServicoModel servico) {
        if ((dto.getValorUnitario() == null || dto.getValorUnitario().compareTo(BigDecimal.ZERO) == 0)
                && servico.getValorBase() != null
                && servico.getValorBase().compareTo(BigDecimal.ZERO) > 0) {
            dto.setValorUnitario(servico.getValorBase());
        }
        if (dto.getQuantidade() == null) {
            dto.setQuantidade(BigDecimal.ONE);
        }
    }

    /**
     * Função: ajusta o DTO conforme o tipo do serviço selecionado.
     * Uso no sistema: serviço interno mantém colaborador e datas de execução da oficina;
     * serviço terceirizado remove colaborador interno e usa os dados da empresa externa.
     */
    private void prepararDtoConformeTipoServico(ItemServicoDTO dto, ServicoModel servico, boolean terceirizado) {
        completarDadosAutomaticosDoServico(dto, servico);

        if (terceirizado) {
            dto.setIdColaborador(null);
            dto.setDataInicio(null);
            dto.setDataFim(null);
            ServicoTerceirizadoModel especializacao = servicoTerceirizadoRepository.findByIdAndAtivoTrue(servico.getId())
                    .orElseThrow(() -> new RuleValidationException("Serviço terceirizado sem cadastro de empresa padrão."));
            if (especializacao.getEmpresaTerceirizadaPadrao() == null) {
                throw new RuleValidationException("Serviço terceirizado sem empresa padrão cadastrada.");
            }
            dto.setIdEmpresaTerceirizada(especializacao.getEmpresaTerceirizadaPadrao().getId());
            return;
        }

        dto.setIdEmpresaTerceirizada(null);
        dto.setDataEnvioTerceirizacao(null);
        dto.setDataRetornoTerceirizacao(null);
        dto.setValorCobradoTerceirizacao(null);
        dto.setObservacaoTerceirizacao(null);
    }

    /**
     * Função: Localiza informações de ordemservico conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    private ColaboradorModel buscarColaboradorAtivo(Long id) {
        return colaboradorRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Colaborador responsável não encontrado ou inativo."));
    }

    /**
     * Função: Processa dados de serviço executado, responsável, valor e vínculo com a Ordem de
     * Serviço.
     * Uso no sistema: identifica se o serviço selecionado deve gerar execução por empresa externa.
     */
    private boolean isServicoTerceirizado(ServicoModel servico) {
        return servicoTerceirizadoRepository.findByIdAndAtivoTrue(servico.getId()).isPresent();
    }

    /**
     * Função: valida o responsável correto conforme o tipo de serviço.
     * Uso no sistema: serviço interno exige colaborador da oficina; serviço terceirizado exige
     * empresa externa executora e não grava colaborador interno como responsável direto.
     */
    private void validarResponsavelEExecucaoExterna(boolean terceirizado, ItemServicoDTO dto) {
        if (terceirizado && (dto.getIdEmpresaTerceirizada() == null || dto.getIdEmpresaTerceirizada() <= 0)) {
            throw new RuleValidationException("Serviço terceirizado deve informar a empresa terceirizada executora.");
        }
        if (!terceirizado && (dto.getIdColaborador() == null || dto.getIdColaborador() <= 0)) {
            throw new RuleValidationException("Serviço interno deve possuir colaborador responsável da oficina.");
        }
        if (!terceirizado && dto.getIdEmpresaTerceirizada() != null) {
            throw new RuleValidationException("Serviço interno não deve possuir empresa terceirizada vinculada.");
        }
    }

    /**
     * Função: Valida os dados recebidos, monta as entidades necessárias e grava o cadastro de
     * ordemservico.
     * Uso no sistema: centraliza a regra de cadastro na camada Service, mantendo Controller e tela
     * mais simples.
     */
    private void salvarOuAtualizarExecucaoTerceirizada(ItemServicoModel itemServico, ItemServicoDTO dto, ServicoModel servico) {
        boolean terceirizado = isServicoTerceirizado(servico);

        if (!terceirizado) {
            execucaoRepository.findByItemServicoIdAndAtivoTrue(itemServico.getId()).ifPresent(execucao -> {
                execucao.setAtivo(Boolean.FALSE);
                execucaoRepository.save(execucao);
            });
            return;
        }

        EmpresaTerceirizadaModel empresa = empresaTerceirizadaService.buscarModelAtivo(dto.getIdEmpresaTerceirizada());
        ExecucaoServicoTerceirizadoModel execucao = execucaoRepository.findByItemServicoIdAndAtivoTrue(itemServico.getId())
                .orElseGet(ExecucaoServicoTerceirizadoModel::new);
        mapper.atualizarExecucaoTerceirizada(execucao, itemServico, empresa, dto);
        execucaoRepository.save(execucao);
    }

    /**
     * Função: Localiza informações de ordemservico conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    private ExecucaoServicoTerceirizadoModel buscarExecucaoTerceirizadaOuNula(Long idItemServico) {
        return execucaoRepository.findByItemServicoIdAndAtivoTrue(idItemServico).orElse(null);
    }
}
