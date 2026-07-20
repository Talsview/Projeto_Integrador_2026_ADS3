package br.com.avcar.oficina.business.garantia.service;

import br.com.avcar.oficina.business.garantia.dto.AcionamentoGarantiaDTO;
import br.com.avcar.oficina.business.garantia.dto.GarantiaPecaDTO;
import br.com.avcar.oficina.business.garantia.dto.GarantiaServicoDTO;
import br.com.avcar.oficina.business.garantia.enums.StatusGarantia;
import br.com.avcar.oficina.business.garantia.mapper.GarantiaPecaMapper;
import br.com.avcar.oficina.business.garantia.mapper.GarantiaServicoMapper;
import br.com.avcar.oficina.business.garantia.model.GarantiaPecaModel;
import br.com.avcar.oficina.business.garantia.model.GarantiaServicoModel;
import br.com.avcar.oficina.business.garantia.repository.IGarantiaPecaRepository;
import br.com.avcar.oficina.business.garantia.repository.IGarantiaServicoRepository;
import br.com.avcar.oficina.business.garantia.validation.GarantiaValidation;
import br.com.avcar.oficina.business.ordemservico.model.ItemServicoModel;
import br.com.avcar.oficina.business.ordemservico.repository.IItemServicoRepository;
import br.com.avcar.oficina.business.peca.model.ItemPecaModel;
import br.com.avcar.oficina.business.peca.repository.IItemPecaRepository;
import br.com.avcar.oficina.core.exception.BusinessException;
import br.com.avcar.oficina.core.exception.FieldValidationException;
import br.com.avcar.oficina.core.exception.RuleValidationException;
import br.com.avcar.oficina.core.service.GenericService;
import br.com.avcar.oficina.core.validation.ValidationUtils;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsável pelas garantias geradas por ItemPeca e ItemServico.
 *
 * Regras atendidas:
 * - ItemPeca gera GarantiaPeca.
 * - ItemServico gera GarantiaServico.
 * - A garantia inicia após a finalização da OS.
 * - A garantia de serviço usa prazo variável conforme o cadastro do serviço.
 * - A peça pode ter responsabilidade do fornecedor, sem excluir o atendimento da oficina.
 */
@Service
public class GarantiaService extends GenericService<GarantiaServicoModel> {

    private final IGarantiaPecaRepository garantiaPecaRepository;
    private final IGarantiaServicoRepository garantiaServicoRepository;
    private final IItemPecaRepository itemPecaRepository;
    private final IItemServicoRepository itemServicoRepository;
    private final GarantiaPecaMapper garantiaPecaMapper;
    private final GarantiaServicoMapper garantiaServicoMapper;
    private final GarantiaValidation validation;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public GarantiaService(IGarantiaPecaRepository garantiaPecaRepository,
                           IGarantiaServicoRepository garantiaServicoRepository,
                           IItemPecaRepository itemPecaRepository,
                           IItemServicoRepository itemServicoRepository,
                           GarantiaPecaMapper garantiaPecaMapper,
                           GarantiaServicoMapper garantiaServicoMapper,
                           GarantiaValidation validation) {
        super(garantiaServicoRepository, null);
        this.garantiaPecaRepository = garantiaPecaRepository;
        this.garantiaServicoRepository = garantiaServicoRepository;
        this.itemPecaRepository = itemPecaRepository;
        this.itemServicoRepository = itemServicoRepository;
        this.garantiaPecaMapper = garantiaPecaMapper;
        this.garantiaServicoMapper = garantiaServicoMapper;
        this.validation = validation;
    }

    @Transactional
    /**
     * Função: Valida os dados recebidos, monta as entidades necessárias e grava o cadastro de
     * garantia.
     * Uso no sistema: centraliza a regra de cadastro na camada Service, mantendo Controller e tela
     * mais simples.
     */
    public void criarGarantiaPecaAguardando(ItemPecaModel itemPeca) {
        if (itemPeca == null || itemPeca.getId() == null) {
            throw new RuleValidationException("Item de peça inválido para geração de garantia.");
        }
        if (!garantiaPecaRepository.existsByItemPecaIdAndAtivoTrue(itemPeca.getId())) {
            garantiaPecaRepository.save(garantiaPecaMapper.criarAguardandoFinalizacao(itemPeca));
        }
    }

    @Transactional
    /**
     * Função: Valida os dados recebidos, monta as entidades necessárias e grava o cadastro de
     * garantia.
     * Uso no sistema: centraliza a regra de cadastro na camada Service, mantendo Controller e tela
     * mais simples.
     */
    public void criarGarantiaServicoAguardando(ItemServicoModel itemServico) {
        if (itemServico == null || itemServico.getId() == null) {
            throw new RuleValidationException("Item de serviço inválido para geração de garantia.");
        }
        if (!garantiaServicoRepository.existsByItemServicoIdAndAtivoTrue(itemServico.getId())) {
            garantiaServicoRepository.save(garantiaServicoMapper.criarAguardandoFinalizacao(itemServico));
        }
    }

    @Transactional
    /**
     * Função: Inicia a garantia vinculada a peças ou serviços após a finalização da Ordem de Serviço.
     * Uso no sistema: aplica a regra de que a garantia começa somente depois do encerramento da OS.
     */
    public void iniciarGarantiasDaOrdem(Long idOrdemServico, LocalDate dataInicio) {
        validation.validateIdOrdemServico(idOrdemServico);
        LocalDate inicio = dataInicio == null ? LocalDate.now() : dataInicio;

        List<ItemPecaModel> pecas = itemPecaRepository.findByIdOrdemServicoAndAtivoTrue(idOrdemServico);
        for (ItemPecaModel itemPeca : pecas) {
            GarantiaPecaModel garantia = garantiaPecaRepository.findByItemPecaIdAndAtivoTrue(itemPeca.getId())
                    .orElseGet(() -> garantiaPecaMapper.criarAguardandoFinalizacao(itemPeca));
            iniciarGarantiaPeca(garantia, itemPeca, inicio);
            garantiaPecaRepository.save(garantia);
        }

        List<ItemServicoModel> servicos = itemServicoRepository.findByOrdemServicoIdAndAtivoTrue(idOrdemServico);
        for (ItemServicoModel itemServico : servicos) {
            GarantiaServicoModel garantia = garantiaServicoRepository.findByItemServicoIdAndAtivoTrue(itemServico.getId())
                    .orElseGet(() -> garantiaServicoMapper.criarAguardandoFinalizacao(itemServico));
            iniciarGarantiaServico(garantia, itemServico, inicio);
            garantiaServicoRepository.save(garantia);
        }
    }


    @Transactional
    /**
     * Função: Localiza um registro inativado, altera seu campo ativo para verdadeiro e salva a
     * reativação.
     * Uso no sistema: permite recuperar cadastros feitos anteriormente sem duplicar clientes,
     * veículos, peças ou serviços.
     */
    public void inativarGarantiaPorItemPeca(Long idItemPeca) {
        if (idItemPeca == null) {
            return;
        }
        garantiaPecaRepository.findByItemPecaIdAndAtivoTrue(idItemPeca).ifPresent(garantia -> {
            garantia.setAtivo(Boolean.FALSE);
            garantiaPecaRepository.save(garantia);
        });
    }

    @Transactional
    /**
     * Função: Localiza um registro inativado, altera seu campo ativo para verdadeiro e salva a
     * reativação.
     * Uso no sistema: permite recuperar cadastros feitos anteriormente sem duplicar clientes,
     * veículos, peças ou serviços.
     */
    public void inativarGarantiaPorItemServico(Long idItemServico) {
        if (idItemServico == null) {
            return;
        }
        garantiaServicoRepository.findByItemServicoIdAndAtivoTrue(idItemServico).ifPresent(garantia -> {
            garantia.setAtivo(Boolean.FALSE);
            garantiaServicoRepository.save(garantia);
        });
    }

    @Transactional(readOnly = true)
    /**
     * Função: Localiza informações de garantia conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    public GarantiaPecaDTO buscarGarantiaPeca(Long id) {
        validation.validateId(id, "Garantia de Peça");
        return garantiaPecaMapper.toDto(buscarGarantiaPecaModel(id));
    }

    @Transactional(readOnly = true)
    /**
     * Função: Localiza informações de garantia conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    public GarantiaServicoDTO buscarGarantiaServico(Long id) {
        validation.validateId(id, "Garantia de Serviço");
        return garantiaServicoMapper.toDto(buscarGarantiaServicoModel(id));
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de garantia aplicando filtros, paginação ou critérios de busca quando
     * informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<GarantiaPecaDTO> listarGarantiasPeca(Pageable pageable) {
        return garantiaPecaRepository.findAllByAtivoTrue(pageable).map(garantiaPecaMapper::toDto);
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de garantia aplicando filtros, paginação ou critérios de busca quando
     * informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<GarantiaServicoDTO> listarGarantiasServico(Pageable pageable) {
        return garantiaServicoRepository.findAllByAtivoTrue(pageable).map(garantiaServicoMapper::toDto);
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de garantia aplicando filtros, paginação ou critérios de busca quando
     * informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<GarantiaPecaDTO> listarGarantiasPecaPorOrdemServico(Long idOrdemServico, Pageable pageable) {
        validation.validateIdOrdemServico(idOrdemServico);
        return garantiaPecaRepository.findByOrdemServico(idOrdemServico, pageable).map(garantiaPecaMapper::toDto);
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de garantia aplicando filtros, paginação ou critérios de busca quando
     * informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<GarantiaServicoDTO> listarGarantiasServicoPorOrdemServico(Long idOrdemServico, Pageable pageable) {
        validation.validateIdOrdemServico(idOrdemServico);
        return garantiaServicoRepository.findByOrdemServico(idOrdemServico, pageable).map(garantiaServicoMapper::toDto);
    }

    @Transactional(readOnly = true)
    /**
     * Função: Localiza informações de garantia conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    public GarantiaPecaDTO buscarGarantiaPorItemPeca(Long idItemPeca) {
        validation.validateId(idItemPeca, "Item de Peça");
        GarantiaPecaModel garantia = garantiaPecaRepository.findByItemPecaIdAndAtivoTrue(idItemPeca)
                .orElseThrow(() -> new BusinessException("Garantia da peça não encontrada para o item informado."));
        return garantiaPecaMapper.toDto(garantia);
    }

    @Transactional(readOnly = true)
    /**
     * Função: Localiza informações de garantia conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    public GarantiaServicoDTO buscarGarantiaPorItemServico(Long idItemServico) {
        validation.validateId(idItemServico, "Item de Serviço");
        GarantiaServicoModel garantia = garantiaServicoRepository.findByItemServicoIdAndAtivoTrue(idItemServico)
                .orElseThrow(() -> new BusinessException("Garantia do serviço não encontrada para o item informado."));
        return garantiaServicoMapper.toDto(garantia);
    }

    @Transactional
    /**
     * Função: Registra o acionamento de uma garantia quando há defeito ou retorno dentro do prazo.
     * Uso no sistema: rastreia a responsabilidade da oficina e, quando houver peça envolvida,
     * possibilita identificar o fornecedor.
     */
    public GarantiaPecaDTO acionarGarantiaPeca(Long id, AcionamentoGarantiaDTO dto) {
        GarantiaPecaModel garantia = buscarGarantiaPecaModel(id);
        validarGarantiaPodeSerAcionada(garantia.getStatusGarantia(), garantia.getDataFim());
        validarDadosAcionamento(dto, garantia.getDataInicio());

        garantia.setStatusGarantia(StatusGarantia.ACIONADA);
        garantia.setDataAcionamento(resolverDataAcionamento(dto));
        garantia.setMotivoAcionamento(limpar(dto.getMotivoAcionamento()));
        garantia.setDescricaoDefeito(limpar(dto.getDescricaoDefeito()));
        garantia.setResponsavelAnalise(limpar(dto.getResponsavelAnalise()));
        if (dto.getResponsabilidade() != null) {
            garantia.setResponsabilidade(dto.getResponsabilidade());
        }
        garantia.setObservacao(combinarObservacao(garantia.getObservacao(), montarObservacaoAcionamento(dto, "Garantia de peça acionada.")));
        return garantiaPecaMapper.toDto(garantiaPecaRepository.save(garantia));
    }

    @Transactional
    /**
     * Função: Registra o acionamento de uma garantia quando há defeito ou retorno dentro do prazo.
     * Uso no sistema: rastreia a responsabilidade da oficina e, quando houver peça envolvida,
     * possibilita identificar o fornecedor.
     */
    public GarantiaServicoDTO acionarGarantiaServico(Long id, AcionamentoGarantiaDTO dto) {
        GarantiaServicoModel garantia = buscarGarantiaServicoModel(id);
        validarGarantiaPodeSerAcionada(garantia.getStatusGarantia(), garantia.getDataFim());
        validarDadosAcionamento(dto, garantia.getDataInicio());

        garantia.setStatusGarantia(StatusGarantia.ACIONADA);
        garantia.setDataAcionamento(resolverDataAcionamento(dto));
        garantia.setMotivoAcionamento(limpar(dto.getMotivoAcionamento()));
        garantia.setDescricaoDefeito(limpar(dto.getDescricaoDefeito()));
        garantia.setResponsavelAnalise(limpar(dto.getResponsavelAnalise()));
        garantia.setObservacao(combinarObservacao(garantia.getObservacao(), montarObservacaoAcionamento(dto, "Garantia de serviço acionada.")));
        return garantiaServicoMapper.toDto(garantiaServicoRepository.save(garantia));
    }

    @Transactional
    /**
     * Função: Encerra uma ocorrência operacional, como garantia ou atendimento, registrando data e
     * observação.
     * Uso no sistema: deixa histórico claro do fechamento da situação acompanhada pela oficina.
     */
    public GarantiaPecaDTO encerrarGarantiaPeca(Long id, AcionamentoGarantiaDTO dto) {
        GarantiaPecaModel garantia = buscarGarantiaPecaModel(id);
        validarGarantiaPodeSerEncerrada(garantia.getStatusGarantia());
        validarDadosEncerramento(dto, garantia.getDataAcionamento());

        garantia.setStatusGarantia(StatusGarantia.ENCERRADA);
        garantia.setDataEncerramento(resolverDataEncerramento(dto));
        garantia.setSolucaoAplicada(limpar(dto.getSolucaoAplicada()));
        garantia.setCustoAssumidoPor(limpar(dto.getCustoAssumidoPor()));
        garantia.setAtendimentoRealizado(dto.getAtendimentoRealizado() == null ? Boolean.TRUE : dto.getAtendimentoRealizado());
        garantia.setObservacao(combinarObservacao(garantia.getObservacao(), montarObservacaoEncerramento(dto, "Garantia de peça encerrada.")));
        return garantiaPecaMapper.toDto(garantiaPecaRepository.save(garantia));
    }

    @Transactional
    /**
     * Função: Encerra uma ocorrência operacional, como garantia ou atendimento, registrando data e
     * observação.
     * Uso no sistema: deixa histórico claro do fechamento da situação acompanhada pela oficina.
     */
    public GarantiaServicoDTO encerrarGarantiaServico(Long id, AcionamentoGarantiaDTO dto) {
        GarantiaServicoModel garantia = buscarGarantiaServicoModel(id);
        validarGarantiaPodeSerEncerrada(garantia.getStatusGarantia());
        validarDadosEncerramento(dto, garantia.getDataAcionamento());

        garantia.setStatusGarantia(StatusGarantia.ENCERRADA);
        garantia.setDataEncerramento(resolverDataEncerramento(dto));
        garantia.setSolucaoAplicada(limpar(dto.getSolucaoAplicada()));
        garantia.setCustoAssumidoPor(limpar(dto.getCustoAssumidoPor()));
        garantia.setAtendimentoRealizado(dto.getAtendimentoRealizado() == null ? Boolean.TRUE : dto.getAtendimentoRealizado());
        garantia.setObservacao(combinarObservacao(garantia.getObservacao(), montarObservacaoEncerramento(dto, "Garantia de serviço encerrada.")));
        return garantiaServicoMapper.toDto(garantiaServicoRepository.save(garantia));
    }

    /**
     * Função: Inicia a garantia vinculada a peças ou serviços após a finalização da Ordem de Serviço.
     * Uso no sistema: aplica a regra de que a garantia começa somente depois do encerramento da OS.
     */
    private void iniciarGarantiaPeca(GarantiaPecaModel garantia, ItemPecaModel itemPeca, LocalDate inicio) {
        if (StatusGarantia.ENCERRADA.equals(garantia.getStatusGarantia())) {
            return;
        }
        garantia.setItemPeca(itemPeca);
        garantia.setPrazoDias(garantiaPecaMapper.resolverPrazoDias(itemPeca));
        garantia.setDataInicio(inicio);
        garantia.setDataFim(inicio.plusDays(garantia.getPrazoDias()));
        garantia.setStatusGarantia(StatusGarantia.VIGENTE);
        garantia.setObservacao(combinarObservacao(garantia.getObservacao(), "Garantia iniciada automaticamente após finalização da OS."));
    }

    /**
     * Função: Inicia a garantia vinculada a peças ou serviços após a finalização da Ordem de Serviço.
     * Uso no sistema: aplica a regra de que a garantia começa somente depois do encerramento da OS.
     */
    private void iniciarGarantiaServico(GarantiaServicoModel garantia, ItemServicoModel itemServico, LocalDate inicio) {
        if (StatusGarantia.ENCERRADA.equals(garantia.getStatusGarantia())) {
            return;
        }
        garantia.setItemServico(itemServico);
        garantia.setPrazoDias(garantiaServicoMapper.resolverPrazoDias(itemServico));
        garantia.setDataInicio(inicio);
        garantia.setDataFim(inicio.plusDays(garantia.getPrazoDias()));
        garantia.setStatusGarantia(StatusGarantia.VIGENTE);
        garantia.setObservacao(combinarObservacao(garantia.getObservacao(), "Garantia iniciada automaticamente após finalização da OS."));
    }

    /**
     * Função: Localiza informações de garantia conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    private GarantiaPecaModel buscarGarantiaPecaModel(Long id) {
        validation.validateId(id, "Garantia de Peça");
        return garantiaPecaRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Garantia de peça não encontrada ou inativa."));
    }

    /**
     * Função: Localiza informações de garantia conforme identificador ou filtro informado.
     * Uso no sistema: concentra as regras de consulta em uma camada própria, evitando acesso direto da
     * tela ao repositório.
     */
    private GarantiaServicoModel buscarGarantiaServicoModel(Long id) {
        validation.validateId(id, "Garantia de Serviço");
        return garantiaServicoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Garantia de serviço não encontrada ou inativa."));
    }

    /**
     * Função: Confere as regras necessárias antes de continuar a operação validar garantia pode ser
     * acionada.
     * Uso no sistema: evita inconsistências e mensagens de erro tardias no banco de dados.
     */
    private void validarGarantiaPodeSerAcionada(StatusGarantia status, LocalDate dataFim) {
        if (StatusGarantia.AGUARDANDO_FINALIZACAO_OS.equals(status)) {
            throw new RuleValidationException("A garantia ainda não pode ser acionada, pois a OS não foi finalizada.");
        }
        if (StatusGarantia.ENCERRADA.equals(status)) {
            throw new RuleValidationException("Garantia encerrada não pode ser acionada.");
        }
        if (StatusGarantia.EXPIRADA.equals(status) || (dataFim != null && dataFim.isBefore(LocalDate.now()))) {
            throw new RuleValidationException("Garantia expirada não pode ser acionada.");
        }
        if (StatusGarantia.ACIONADA.equals(status)) {
            throw new RuleValidationException("Garantia já está acionada.");
        }
    }

    /**
     * Função: Confere as regras necessárias antes de continuar a operação validar garantia pode ser
     * encerrada.
     * Uso no sistema: evita inconsistências e mensagens de erro tardias no banco de dados.
     */
    private void validarGarantiaPodeSerEncerrada(StatusGarantia status) {
        if (StatusGarantia.ENCERRADA.equals(status)) {
            throw new RuleValidationException("Garantia já está encerrada.");
        }
        if (!StatusGarantia.ACIONADA.equals(status)) {
            throw new RuleValidationException("Somente garantias acionadas podem ser encerradas.");
        }
    }

    /**
     * Função: Confere as regras necessárias antes de continuar a operação validar dados acionamento.
     * Uso no sistema: evita inconsistências e mensagens de erro tardias no banco de dados.
     */
    private void validarDadosAcionamento(AcionamentoGarantiaDTO dto, LocalDate dataInicioGarantia) {
        if (dto == null) {
            throw new FieldValidationException("Informe os dados do acionamento da garantia.");
        }
        ValidationUtils.requireText(dto.getMotivoAcionamento(), "motivo do acionamento");
        ValidationUtils.requireText(dto.getDescricaoDefeito(), "defeito relatado");
        ValidationUtils.requireText(dto.getResponsavelAnalise(), "responsável pela análise");
        ValidationUtils.maxLength(dto.getMotivoAcionamento(), 255, "motivo do acionamento");
        ValidationUtils.maxLength(dto.getResponsavelAnalise(), 150, "responsável pela análise");
        ValidationUtils.maxLength(dto.getObservacao(), 1000, "observação");

        LocalDate dataAcionamento = resolverDataAcionamento(dto);
        ValidationUtils.notFuture(dataAcionamento, "data do acionamento");
        ValidationUtils.dateNotBefore(dataAcionamento, dataInicioGarantia, "data do acionamento", "data de início da garantia");
    }

    /**
     * Função: Confere as regras necessárias antes de continuar a operação validar dados encerramento.
     * Uso no sistema: evita inconsistências e mensagens de erro tardias no banco de dados.
     */
    private void validarDadosEncerramento(AcionamentoGarantiaDTO dto, LocalDate dataAcionamento) {
        if (dto == null) {
            throw new FieldValidationException("Informe os dados do encerramento da garantia.");
        }
        ValidationUtils.requireText(dto.getSolucaoAplicada(), "solução aplicada");
        ValidationUtils.maxLength(dto.getCustoAssumidoPor(), 80, "custo assumido por");
        ValidationUtils.maxLength(dto.getObservacao(), 1000, "observação final");

        LocalDate dataEncerramento = resolverDataEncerramento(dto);
        ValidationUtils.notFuture(dataEncerramento, "data do encerramento");
        ValidationUtils.dateNotBefore(dataEncerramento, dataAcionamento, "data do encerramento", "data do acionamento");
    }

    /**
     * Função: Define a data de acionamento da garantia, usando a data informada ou a data atual quando
     * não houver valor.
     * Uso no sistema: registra corretamente quando o cliente retornou com problema dentro da garantia.
     */
    private LocalDate resolverDataAcionamento(AcionamentoGarantiaDTO dto) {
        return dto != null && dto.getDataAcionamento() != null ? dto.getDataAcionamento() : LocalDate.now();
    }

    /**
     * Função: Define a data de encerramento da garantia, usando a data informada ou a data atual
     * quando não houver valor.
     * Uso no sistema: mantém histórico claro do fechamento do atendimento em garantia.
     */
    private LocalDate resolverDataEncerramento(AcionamentoGarantiaDTO dto) {
        return dto != null && dto.getDataEncerramento() != null ? dto.getDataEncerramento() : LocalDate.now();
    }

    /**
     * Função: Monta o objeto ou resposta necessária para a operação montar observacao acionamento.
     * Uso no sistema: isola a preparação dos dados e melhora a legibilidade do fluxo principal.
     */
    private String montarObservacaoAcionamento(AcionamentoGarantiaDTO dto, String titulo) {
        StringBuilder sb = new StringBuilder(titulo);
        sb.append(" Motivo: ").append(limpar(dto.getMotivoAcionamento()));
        sb.append(" Defeito relatado: ").append(limpar(dto.getDescricaoDefeito()));
        sb.append(" Responsável pela análise: ").append(limpar(dto.getResponsavelAnalise()));
        if (dto.getResponsabilidade() != null) {
            sb.append(" Responsabilidade inicial: ").append(dto.getResponsabilidade()).append(".");
        }
        if (limpar(dto.getObservacao()) != null) {
            sb.append(" Observação: ").append(limpar(dto.getObservacao()));
        }
        return sb.toString();
    }

    /**
     * Função: Monta o objeto ou resposta necessária para a operação montar observacao encerramento.
     * Uso no sistema: isola a preparação dos dados e melhora a legibilidade do fluxo principal.
     */
    private String montarObservacaoEncerramento(AcionamentoGarantiaDTO dto, String titulo) {
        StringBuilder sb = new StringBuilder(titulo);
        sb.append(" Solução aplicada: ").append(limpar(dto.getSolucaoAplicada()));
        if (limpar(dto.getCustoAssumidoPor()) != null) {
            sb.append(" Custo assumido por: ").append(limpar(dto.getCustoAssumidoPor())).append(".");
        }
        if (dto.getAtendimentoRealizado() != null) {
            sb.append(" Atendimento realizado: ").append(dto.getAtendimentoRealizado() ? "sim" : "não").append(".");
        }
        if (limpar(dto.getObservacao()) != null) {
            sb.append(" Observação final: ").append(limpar(dto.getObservacao()));
        }
        return sb.toString();
    }

    /**
     * Função: Remove espaços extras e transforma texto vazio em valor nulo.
     * Uso no sistema: evita gravar observações e descrições com sujeira ou campos aparentemente
     * preenchidos sem conteúdo real.
     */
    private String limpar(String valor) {
        return ValidationUtils.trimToNull(valor);
    }

    /**
     * Função: Junta a observação anterior com uma nova informação de garantia sem perder o histórico
     * textual.
     * Uso no sistema: preserva comentários feitos durante o acionamento e o encerramento da garantia.
     */
    private String combinarObservacao(String atual, String nova) {
        if (atual == null || atual.trim().isEmpty()) {
            return nova;
        }
        if (atual.contains(nova)) {
            return atual;
        }
        return atual + "\n" + nova;
    }
}
