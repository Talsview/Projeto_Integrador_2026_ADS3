package br.com.avcar.oficina.business.pessoa.service;

import br.com.avcar.oficina.business.pessoa.designpattern.factory.ClienteCadastroFactory;
import br.com.avcar.oficina.business.pessoa.designpattern.factory.ClienteFactoryMethod;
import br.com.avcar.oficina.business.pessoa.dto.ClienteDetalheDTO;
import br.com.avcar.oficina.business.pessoa.dto.ClientePessoaFisicaDTO;
import br.com.avcar.oficina.business.pessoa.dto.ClientePessoaJuridicaDTO;
import br.com.avcar.oficina.business.pessoa.dto.ClienteResumoDTO;
import br.com.avcar.oficina.business.pessoa.enums.TipoCliente;
import br.com.avcar.oficina.business.pessoa.mapper.ClienteMapper;
import br.com.avcar.oficina.business.pessoa.model.ClienteModel;
import br.com.avcar.oficina.business.pessoa.model.PessoaFisicaModel;
import br.com.avcar.oficina.business.pessoa.model.PessoaJuridicaModel;
import br.com.avcar.oficina.business.pessoa.model.PessoaModel;
import br.com.avcar.oficina.business.pessoa.repository.IClienteRepository;
import br.com.avcar.oficina.business.pessoa.repository.IPessoaFisicaRepository;
import br.com.avcar.oficina.business.pessoa.repository.IPessoaJuridicaRepository;
import br.com.avcar.oficina.business.pessoa.repository.IPessoaRepository;
import br.com.avcar.oficina.business.pessoa.validation.ClienteValidation;
import br.com.avcar.oficina.core.exception.BusinessException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service do módulo Cliente.
 * Orquestra Repository, Validation, Mapper e Factory Method.
 */
@Service
public class ClienteService {

    private final IPessoaRepository pessoaRepository;
    private final IClienteRepository clienteRepository;
    private final IPessoaFisicaRepository pessoaFisicaRepository;
    private final IPessoaJuridicaRepository pessoaJuridicaRepository;
    private final ClienteValidation validation;
    private final ClienteMapper mapper;
    private final ClienteCadastroFactory clienteCadastroFactory;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public ClienteService(IPessoaRepository pessoaRepository,
                          IClienteRepository clienteRepository,
                          IPessoaFisicaRepository pessoaFisicaRepository,
                          IPessoaJuridicaRepository pessoaJuridicaRepository,
                          ClienteValidation validation,
                          ClienteMapper mapper,
                          ClienteCadastroFactory clienteCadastroFactory) {
        this.pessoaRepository = pessoaRepository;
        this.clienteRepository = clienteRepository;
        this.pessoaFisicaRepository = pessoaFisicaRepository;
        this.pessoaJuridicaRepository = pessoaJuridicaRepository;
        this.validation = validation;
        this.mapper = mapper;
        this.clienteCadastroFactory = clienteCadastroFactory;
    }

    @Transactional
    /**
     * Função: Valida o cadastro de cliente pessoa física, usa a fábrica correta e grava Pessoa,
     * Cliente e PessoaFisica.
     * Uso no sistema: mantém separada a regra de CPF da regra de CNPJ e respeita a especialização
     * Cliente -> PessoaFisica/PessoaJuridica.
     */
    public ClientePessoaFisicaDTO cadastrarPessoaFisica(ClientePessoaFisicaDTO dto) {
        validation.validatePessoaFisicaInsert(dto);

        ClienteFactoryMethod<ClientePessoaFisicaDTO, PessoaFisicaModel> factory =
                clienteCadastroFactory.obterFactory(TipoCliente.PESSOA_FISICA);

        PessoaModel pessoa = pessoaRepository.save(factory.criarPessoa(dto));
        ClienteModel cliente = clienteRepository.save(factory.criarCliente(pessoa));
        PessoaFisicaModel pessoaFisica = pessoaFisicaRepository.save(factory.criarEspecializacao(cliente, dto));

        return mapper.toPessoaFisicaDto(pessoaFisica);
    }

    @Transactional
    /**
     * Função: Valida o cadastro de cliente pessoa jurídica, usa a fábrica correta e grava Pessoa,
     * Cliente e PessoaJuridica.
     * Uso no sistema: mantém os dados de empresa organizados e separados dos dados de pessoa física.
     */
    public ClientePessoaJuridicaDTO cadastrarPessoaJuridica(ClientePessoaJuridicaDTO dto) {
        validation.validatePessoaJuridicaInsert(dto);

        ClienteFactoryMethod<ClientePessoaJuridicaDTO, PessoaJuridicaModel> factory =
                clienteCadastroFactory.obterFactory(TipoCliente.PESSOA_JURIDICA);

        PessoaModel pessoa = pessoaRepository.save(factory.criarPessoa(dto));
        ClienteModel cliente = clienteRepository.save(factory.criarCliente(pessoa));
        PessoaJuridicaModel pessoaJuridica = pessoaJuridicaRepository.save(factory.criarEspecializacao(cliente, dto));

        return mapper.toPessoaJuridicaDto(pessoaJuridica);
    }

    @Transactional
    /**
     * Função: Busca o registro ativo, aplica as alterações permitidas e salva a atualização de
     * cliente.
     * Uso no sistema: garante que alterações passem por validação e não quebrem vínculos já existentes
     * no sistema.
     */
    public ClientePessoaFisicaDTO atualizarPessoaFisica(Long id, ClientePessoaFisicaDTO dto) {
        validation.validatePessoaFisicaUpdate(id, dto);

        PessoaFisicaModel pessoaFisica = pessoaFisicaRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Cliente pessoa física não encontrado ou inativo."));

        PessoaModel pessoa = pessoaFisica.getCliente().getPessoa();
        mapper.atualizarPessoa(pessoa, dto);
        mapper.atualizarPessoaFisica(pessoaFisica, dto);

        pessoaRepository.save(pessoa);
        PessoaFisicaModel saved = pessoaFisicaRepository.save(pessoaFisica);
        return mapper.toPessoaFisicaDto(saved);
    }

    @Transactional
    /**
     * Função: Busca o registro ativo, aplica as alterações permitidas e salva a atualização de
     * cliente.
     * Uso no sistema: garante que alterações passem por validação e não quebrem vínculos já existentes
     * no sistema.
     */
    public ClientePessoaJuridicaDTO atualizarPessoaJuridica(Long id, ClientePessoaJuridicaDTO dto) {
        validation.validatePessoaJuridicaUpdate(id, dto);

        PessoaJuridicaModel pessoaJuridica = pessoaJuridicaRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Cliente pessoa jurídica não encontrado ou inativo."));

        PessoaModel pessoa = pessoaJuridica.getCliente().getPessoa();
        mapper.atualizarPessoa(pessoa, dto);
        mapper.atualizarPessoaJuridica(pessoaJuridica, dto);

        pessoaRepository.save(pessoa);
        PessoaJuridicaModel saved = pessoaJuridicaRepository.save(pessoaJuridica);
        return mapper.toPessoaJuridicaDto(saved);
    }

    @Transactional(readOnly = true)
    /**
     * Função: Busca um registro específico de cliente com dados completos para visualização ou edição.
     * Uso no sistema: fornece à tela uma visão detalhada sem expor diretamente as entidades internas
     * do banco.
     */
    public ClienteDetalheDTO buscarDetalhado(Long id) {
        validation.validateId(id);

        return pessoaFisicaRepository.findByIdAndAtivoTrue(id)
                .map(mapper::toDetalhePessoaFisica)
                .or(() -> pessoaJuridicaRepository.findByIdAndAtivoTrue(id).map(mapper::toDetalhePessoaJuridica))
                .orElseThrow(() -> new BusinessException("Cliente não encontrado ou inativo."));
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de cliente aplicando filtros, paginação ou critérios de busca quando
     * informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<ClienteResumoDTO> listar(Pageable pageable) {
        Page<ClienteModel> clientes = clienteRepository.findAllByAtivoTrue(pageable);
        return clientes.map(this::montarResumo);
    }

    @Transactional(readOnly = true)
    /**
     * Função: Consulta registros de cliente aplicando filtros, paginação ou critérios de busca quando
     * informados.
     * Uso no sistema: permite que as telas exibam dados organizados sem carregar informações
     * desnecessárias.
     */
    public Page<ClienteResumoDTO> pesquisar(String termo, Pageable pageable) {
        if (termo == null || termo.isBlank()) {
            return listar(pageable);
        }

        Page<ClienteModel> porPessoa = clienteRepository.searchByPessoa(termo.trim(), pageable);
        List<ClienteResumoDTO> encontrados = new ArrayList<>(porPessoa.map(this::montarResumo).getContent());

        pessoaFisicaRepository.findByCpfAndAtivoTrue(validation.onlyDigits(termo))
                .map(mapper::toResumoPessoaFisica)
                .ifPresent(encontrados::add);

        pessoaJuridicaRepository.findByCnpjAndAtivoTrue(validation.onlyDigits(termo))
                .map(mapper::toResumoPessoaJuridica)
                .ifPresent(encontrados::add);

        List<ClienteResumoDTO> semDuplicidade = encontrados.stream()
                .filter(dto -> dto.getId() != null)
                .collect(ArrayList::new, (lista, item) -> {
                    boolean exists = lista.stream().anyMatch(existing -> existing.getId().equals(item.getId()));
                    if (!exists) {
                        lista.add(item);
                    }
                }, ArrayList::addAll);

        return new PageImpl<>(semDuplicidade, pageable, semDuplicidade.size());
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

        ClienteModel cliente = clienteRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado ou inativo."));

        pessoaFisicaRepository.findByIdAndAtivoTrue(id).ifPresent(pessoaFisica -> {
            pessoaFisica.setAtivo(Boolean.FALSE);
            pessoaFisicaRepository.save(pessoaFisica);
        });

        pessoaJuridicaRepository.findByIdAndAtivoTrue(id).ifPresent(pessoaJuridica -> {
            pessoaJuridica.setAtivo(Boolean.FALSE);
            pessoaJuridicaRepository.save(pessoaJuridica);
        });

        cliente.setAtivo(Boolean.FALSE);
        clienteRepository.save(cliente);
    }



    @Transactional(readOnly = true)
    /**
     * Função: Lista cadastros inativados para que o usuário possa localizar e reativar registros sem
     * recriá-los.
     * Uso no sistema: reforça a rastreabilidade, pois registros antigos continuam no banco e podem
     * voltar a ficar ativos.
     */
    public Page<ClienteResumoDTO> listarInativos(Pageable pageable) {
        Page<ClienteModel> clientes = clienteRepository.findAllByAtivoFalse(pageable);
        return clientes.map(this::montarResumoInativo);
    }

    @Transactional
    /**
     * Função: Localiza um registro inativado, altera seu campo ativo para verdadeiro e salva a
     * reativação.
     * Uso no sistema: permite recuperar cadastros feitos anteriormente sem duplicar clientes,
     * veículos, peças ou serviços.
     */
    public ClienteResumoDTO ativar(Long id) {
        validation.validateId(id);

        ClienteModel cliente = clienteRepository.findByIdAndAtivoFalse(id)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado entre os inativos."));

        if (cliente.getPessoa() != null) {
            cliente.getPessoa().setAtivo(Boolean.TRUE);
            pessoaRepository.save(cliente.getPessoa());
        }

        pessoaFisicaRepository.findByIdAndAtivoFalse(id).ifPresent(pessoaFisica -> {
            pessoaFisica.setAtivo(Boolean.TRUE);
            pessoaFisicaRepository.save(pessoaFisica);
        });

        pessoaJuridicaRepository.findByIdAndAtivoFalse(id).ifPresent(pessoaJuridica -> {
            pessoaJuridica.setAtivo(Boolean.TRUE);
            pessoaJuridicaRepository.save(pessoaJuridica);
        });

        cliente.setAtivo(Boolean.TRUE);
        ClienteModel saved = clienteRepository.save(cliente);
        return montarResumoInativo(saved);
    }

    /**
     * Função: Monta o objeto ou resposta necessária para a operação montar resumo.
     * Uso no sistema: isola a preparação dos dados e melhora a legibilidade do fluxo principal.
     */
    private ClienteResumoDTO montarResumo(ClienteModel cliente) {
        Long clienteId = cliente.getId();
        return pessoaFisicaRepository.findByIdAndAtivoTrue(clienteId)
                .map(mapper::toResumoPessoaFisica)
                .or(() -> pessoaJuridicaRepository.findByIdAndAtivoTrue(clienteId).map(mapper::toResumoPessoaJuridica))
                .orElseGet(() -> mapper.toResumoSemEspecializacao(cliente));
    }

    /**
     * Função: Monta o objeto ou resposta necessária para a operação montar resumo inativo.
     * Uso no sistema: isola a preparação dos dados e melhora a legibilidade do fluxo principal.
     */
    private ClienteResumoDTO montarResumoInativo(ClienteModel cliente) {
        Long clienteId = cliente.getId();
        return pessoaFisicaRepository.findByIdAndAtivoFalse(clienteId)
                .map(mapper::toResumoPessoaFisica)
                .or(() -> pessoaJuridicaRepository.findByIdAndAtivoFalse(clienteId).map(mapper::toResumoPessoaJuridica))
                .orElseGet(() -> mapper.toResumoSemEspecializacao(cliente));
    }

}
