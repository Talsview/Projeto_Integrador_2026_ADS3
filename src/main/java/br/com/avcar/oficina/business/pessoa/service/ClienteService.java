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
    public ClienteDetalheDTO buscarDetalhado(Long id) {
        validation.validateId(id);

        return pessoaFisicaRepository.findByIdAndAtivoTrue(id)
                .map(mapper::toDetalhePessoaFisica)
                .or(() -> pessoaJuridicaRepository.findByIdAndAtivoTrue(id).map(mapper::toDetalhePessoaJuridica))
                .orElseThrow(() -> new BusinessException("Cliente não encontrado ou inativo."));
    }

    @Transactional(readOnly = true)
    public Page<ClienteResumoDTO> listar(Pageable pageable) {
        Page<ClienteModel> clientes = clienteRepository.findAllByAtivoTrue(pageable);
        return clientes.map(this::montarResumo);
    }

    @Transactional(readOnly = true)
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

    private ClienteResumoDTO montarResumo(ClienteModel cliente) {
        Long clienteId = cliente.getId();
        return pessoaFisicaRepository.findByIdAndAtivoTrue(clienteId)
                .map(mapper::toResumoPessoaFisica)
                .or(() -> pessoaJuridicaRepository.findByIdAndAtivoTrue(clienteId).map(mapper::toResumoPessoaJuridica))
                .orElseGet(() -> mapper.toResumoSemEspecializacao(cliente));
    }
}
