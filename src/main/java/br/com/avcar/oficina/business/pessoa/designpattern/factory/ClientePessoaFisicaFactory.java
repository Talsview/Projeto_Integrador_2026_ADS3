package br.com.avcar.oficina.business.pessoa.designpattern.factory;

import br.com.avcar.oficina.business.pessoa.dto.ClientePessoaFisicaDTO;
import br.com.avcar.oficina.business.pessoa.enums.TipoCliente;
import br.com.avcar.oficina.business.pessoa.model.ClienteModel;
import br.com.avcar.oficina.business.pessoa.model.PessoaFisicaModel;
import br.com.avcar.oficina.business.pessoa.model.PessoaModel;
import org.springframework.stereotype.Component;

@Component
public class ClientePessoaFisicaFactory implements ClienteFactoryMethod<ClientePessoaFisicaDTO, PessoaFisicaModel> {

    @Override
    public TipoCliente getTipoCliente() {
        return TipoCliente.PESSOA_FISICA;
    }

    @Override
    public PessoaModel criarPessoa(ClientePessoaFisicaDTO dto) {
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
    public PessoaFisicaModel criarEspecializacao(ClienteModel cliente, ClientePessoaFisicaDTO dto) {
        PessoaFisicaModel pessoaFisica = new PessoaFisicaModel();
        pessoaFisica.setCliente(cliente);
        pessoaFisica.setCpf(onlyDigits(dto.getCpf()));
        pessoaFisica.setRg(dto.getRg());
        pessoaFisica.setDataNascimento(dto.getDataNascimento());
        return pessoaFisica;
    }

    private String onlyDigits(String value) {
        return value == null ? null : value.replaceAll("\\D", "");
    }
}
