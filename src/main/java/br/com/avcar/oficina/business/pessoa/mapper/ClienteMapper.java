package br.com.avcar.oficina.business.pessoa.mapper;

import br.com.avcar.oficina.business.pessoa.dto.ClienteDetalheDTO;
import br.com.avcar.oficina.business.pessoa.dto.ClientePessoaFisicaDTO;
import br.com.avcar.oficina.business.pessoa.dto.ClientePessoaJuridicaDTO;
import br.com.avcar.oficina.business.pessoa.dto.ClienteResumoDTO;
import br.com.avcar.oficina.business.pessoa.enums.TipoCliente;
import br.com.avcar.oficina.business.pessoa.model.ClienteModel;
import br.com.avcar.oficina.business.pessoa.model.PessoaFisicaModel;
import br.com.avcar.oficina.business.pessoa.model.PessoaJuridicaModel;
import br.com.avcar.oficina.business.pessoa.model.PessoaModel;
import org.springframework.stereotype.Component;

/**
 * Centraliza a conversão entre Models e DTOs do módulo Cliente.
 */
@Component
public class ClienteMapper {

    public ClientePessoaFisicaDTO toPessoaFisicaDto(PessoaFisicaModel pessoaFisica) {
        if (pessoaFisica == null) {
            return null;
        }
        ClienteModel cliente = pessoaFisica.getCliente();
        PessoaModel pessoa = cliente.getPessoa();

        ClientePessoaFisicaDTO dto = new ClientePessoaFisicaDTO();
        dto.setId(cliente.getId());
        dto.setPessoaId(pessoa.getId());
        dto.setAtivo(cliente.getAtivo());
        dto.setNome(pessoa.getNome());
        dto.setTelefone(pessoa.getTelefone());
        dto.setEmail(pessoa.getEmail());
        dto.setEndereco(pessoa.getEndereco());
        dto.setCpf(pessoaFisica.getCpf());
        dto.setRg(pessoaFisica.getRg());
        dto.setDataNascimento(pessoaFisica.getDataNascimento());
        return dto;
    }

    public ClientePessoaJuridicaDTO toPessoaJuridicaDto(PessoaJuridicaModel pessoaJuridica) {
        if (pessoaJuridica == null) {
            return null;
        }
        ClienteModel cliente = pessoaJuridica.getCliente();
        PessoaModel pessoa = cliente.getPessoa();

        ClientePessoaJuridicaDTO dto = new ClientePessoaJuridicaDTO();
        dto.setId(cliente.getId());
        dto.setPessoaId(pessoa.getId());
        dto.setAtivo(cliente.getAtivo());
        dto.setNome(pessoa.getNome());
        dto.setTelefone(pessoa.getTelefone());
        dto.setEmail(pessoa.getEmail());
        dto.setEndereco(pessoa.getEndereco());
        dto.setCnpj(pessoaJuridica.getCnpj());
        dto.setRazaoSocial(pessoaJuridica.getRazaoSocial());
        dto.setNomeFantasia(pessoaJuridica.getNomeFantasia());
        dto.setInscricaoEstadual(pessoaJuridica.getInscricaoEstadual());
        return dto;
    }

    public ClienteDetalheDTO toDetalhePessoaFisica(PessoaFisicaModel pessoaFisica) {
        ClientePessoaFisicaDTO origem = toPessoaFisicaDto(pessoaFisica);
        ClienteDetalheDTO detalhe = baseDetalhe(origem.getId(), origem.getPessoaId(), origem.getAtivo(), TipoCliente.PESSOA_FISICA,
                origem.getNome(), origem.getTelefone(), origem.getEmail(), origem.getEndereco());
        detalhe.setCpf(origem.getCpf());
        detalhe.setRg(origem.getRg());
        detalhe.setDataNascimento(origem.getDataNascimento());
        return detalhe;
    }

    public ClienteDetalheDTO toDetalhePessoaJuridica(PessoaJuridicaModel pessoaJuridica) {
        ClientePessoaJuridicaDTO origem = toPessoaJuridicaDto(pessoaJuridica);
        ClienteDetalheDTO detalhe = baseDetalhe(origem.getId(), origem.getPessoaId(), origem.getAtivo(), TipoCliente.PESSOA_JURIDICA,
                origem.getNome(), origem.getTelefone(), origem.getEmail(), origem.getEndereco());
        detalhe.setCnpj(origem.getCnpj());
        detalhe.setRazaoSocial(origem.getRazaoSocial());
        detalhe.setNomeFantasia(origem.getNomeFantasia());
        detalhe.setInscricaoEstadual(origem.getInscricaoEstadual());
        return detalhe;
    }

    public ClienteResumoDTO toResumoPessoaFisica(PessoaFisicaModel pessoaFisica) {
        ClienteModel cliente = pessoaFisica.getCliente();
        PessoaModel pessoa = cliente.getPessoa();
        ClienteResumoDTO dto = baseResumo(cliente, pessoa, TipoCliente.PESSOA_FISICA);
        dto.setDocumento(pessoaFisica.getCpf());
        return dto;
    }

    public ClienteResumoDTO toResumoPessoaJuridica(PessoaJuridicaModel pessoaJuridica) {
        ClienteModel cliente = pessoaJuridica.getCliente();
        PessoaModel pessoa = cliente.getPessoa();
        ClienteResumoDTO dto = baseResumo(cliente, pessoa, TipoCliente.PESSOA_JURIDICA);
        dto.setDocumento(pessoaJuridica.getCnpj());
        return dto;
    }

    public ClienteResumoDTO toResumoSemEspecializacao(ClienteModel cliente) {
        PessoaModel pessoa = cliente.getPessoa();
        return baseResumo(cliente, pessoa, null);
    }

    public void atualizarPessoa(PessoaModel pessoa, ClientePessoaFisicaDTO dto) {
        pessoa.setNome(dto.getNome());
        pessoa.setTelefone(dto.getTelefone());
        pessoa.setEmail(dto.getEmail());
        pessoa.setEndereco(dto.getEndereco());
    }

    public void atualizarPessoa(PessoaModel pessoa, ClientePessoaJuridicaDTO dto) {
        pessoa.setNome(dto.getNome());
        pessoa.setTelefone(dto.getTelefone());
        pessoa.setEmail(dto.getEmail());
        pessoa.setEndereco(dto.getEndereco());
    }

    public void atualizarPessoaFisica(PessoaFisicaModel pessoaFisica, ClientePessoaFisicaDTO dto) {
        pessoaFisica.setCpf(onlyDigits(dto.getCpf()));
        pessoaFisica.setRg(dto.getRg());
        pessoaFisica.setDataNascimento(dto.getDataNascimento());
    }

    public void atualizarPessoaJuridica(PessoaJuridicaModel pessoaJuridica, ClientePessoaJuridicaDTO dto) {
        pessoaJuridica.setCnpj(onlyDigits(dto.getCnpj()));
        pessoaJuridica.setRazaoSocial(dto.getRazaoSocial());
        pessoaJuridica.setNomeFantasia(dto.getNomeFantasia());
        pessoaJuridica.setInscricaoEstadual(dto.getInscricaoEstadual());
    }

    private ClienteResumoDTO baseResumo(ClienteModel cliente, PessoaModel pessoa, TipoCliente tipoCliente) {
        ClienteResumoDTO dto = new ClienteResumoDTO();
        dto.setId(cliente.getId());
        dto.setPessoaId(pessoa.getId());
        dto.setAtivo(cliente.getAtivo());
        dto.setTipoCliente(tipoCliente);
        dto.setNome(pessoa.getNome());
        dto.setTelefone(pessoa.getTelefone());
        dto.setEmail(pessoa.getEmail());
        return dto;
    }

    private ClienteDetalheDTO baseDetalhe(Long id, Long pessoaId, Boolean ativo, TipoCliente tipoCliente,
                                          String nome, String telefone, String email, String endereco) {
        ClienteDetalheDTO dto = new ClienteDetalheDTO();
        dto.setId(id);
        dto.setPessoaId(pessoaId);
        dto.setAtivo(ativo);
        dto.setTipoCliente(tipoCliente);
        dto.setNome(nome);
        dto.setTelefone(telefone);
        dto.setEmail(email);
        dto.setEndereco(endereco);
        return dto;
    }

    private String onlyDigits(String value) {
        return value == null ? null : value.replaceAll("\\D", "");
    }
}
