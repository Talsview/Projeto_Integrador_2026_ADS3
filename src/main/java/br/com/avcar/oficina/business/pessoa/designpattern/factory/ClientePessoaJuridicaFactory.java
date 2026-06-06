package br.com.avcar.oficina.business.pessoa.designpattern.factory;

import br.com.avcar.oficina.business.pessoa.dto.ClientePessoaJuridicaDTO;
import br.com.avcar.oficina.business.pessoa.enums.TipoCliente;
import br.com.avcar.oficina.business.pessoa.model.ClienteModel;
import br.com.avcar.oficina.business.pessoa.model.PessoaJuridicaModel;
import br.com.avcar.oficina.business.pessoa.model.PessoaModel;
import org.springframework.stereotype.Component;

@Component
public class ClientePessoaJuridicaFactory implements ClienteFactoryMethod<ClientePessoaJuridicaDTO, PessoaJuridicaModel> {

    @Override
    public TipoCliente getTipoCliente() {
        return TipoCliente.PESSOA_JURIDICA;
    }

    @Override
    public PessoaModel criarPessoa(ClientePessoaJuridicaDTO dto) {
        PessoaModel pessoa = new PessoaModel();
        pessoa.setNome(dto.getNome());
        pessoa.setTelefone(dto.getTelefone());
        pessoa.setEmail(dto.getEmail());
        pessoa.setEndereco(dto.getEndereco());
        return pessoa;
    }

    @Override
    public ClienteModel criarCliente(PessoaModel pessoa) {
        ClienteModel cliente = new ClienteModel();
        cliente.setPessoa(pessoa);
        return cliente;
    }

    @Override
    public PessoaJuridicaModel criarEspecializacao(ClienteModel cliente, ClientePessoaJuridicaDTO dto) {
        PessoaJuridicaModel pessoaJuridica = new PessoaJuridicaModel();
        pessoaJuridica.setCliente(cliente);
        pessoaJuridica.setCnpj(onlyDigits(dto.getCnpj()));
        pessoaJuridica.setRazaoSocial(dto.getRazaoSocial());
        pessoaJuridica.setNomeFantasia(dto.getNomeFantasia());
        pessoaJuridica.setInscricaoEstadual(dto.getInscricaoEstadual());
        return pessoaJuridica;
    }

    private String onlyDigits(String value) {
        return value == null ? null : value.replaceAll("\\D", "");
    }
}
