package br.com.avcar.oficina.business.pessoa.designpattern.factory;

import br.com.avcar.oficina.business.pessoa.dto.ClientePessoaJuridicaDTO;
import br.com.avcar.oficina.business.pessoa.enums.TipoCliente;
import br.com.avcar.oficina.business.pessoa.model.ClienteModel;
import br.com.avcar.oficina.business.pessoa.model.PessoaJuridicaModel;
import br.com.avcar.oficina.business.pessoa.model.PessoaModel;
import org.springframework.stereotype.Component;

/**
 * PADRÃO DE PROJETO: FACTORY METHOD.
 *
 * Função no sistema: centraliza a criação de clientes Pessoa Física e Pessoa Jurídica, evitando lógica condicional repetida no serviço de cadastro.
 * Justificativa funcional: este código participa de uma funcionalidade real do sistema e
 * evita que o padrão seja usado apenas como exemplo sem utilidade prática.
 */
@Component
public class ClientePessoaJuridicaFactory implements ClienteFactoryMethod<ClientePessoaJuridicaDTO, PessoaJuridicaModel> {

    @Override
    /**
     * Função: Participa da criação padronizada de cliente no fluxo get tipo cliente.
     * Padrão aplicado: FACTORY METHOD.
     * Justificativa: organiza a instanciação das entidades da hierarquia Pessoa, Cliente e
     * especializações.
     * Uso no sistema: torna o cadastro de clientes PF/PJ mais claro, validável e aderente à modelagem
     * conceitual.
     */
    public TipoCliente getTipoCliente() {
        return TipoCliente.PESSOA_JURIDICA;
    }

    @Override
    /**
     * Função: Monta a entidade Pessoa com os dados comuns do cliente antes da especialização PF ou PJ.
     * Padrão aplicado: FACTORY METHOD.
     * Justificativa: separa a criação dos dados comuns da criação da especialização, respeitando a
     * hierarquia do modelo.
     * Uso no sistema: torna o cadastro de clientes PF/PJ mais claro, validável e aderente à modelagem
     * conceitual.
     */
    public PessoaModel criarPessoa(ClientePessoaJuridicaDTO dto) {
        PessoaModel pessoa = new PessoaModel();
        pessoa.setNome(dto.getNome());
        pessoa.setTelefone(dto.getTelefone());
        pessoa.setEmail(dto.getEmail());
        pessoa.setEndereco(dto.getEndereco());
        return pessoa;
    }

    @Override
    /**
     * Função: Cria a entidade Cliente vinculada à Pessoa recém-cadastrada.
     * Padrão aplicado: FACTORY METHOD.
     * Justificativa: mantém a criação do cliente padronizada para os dois tipos de cadastro.
     * Uso no sistema: torna o cadastro de clientes PF/PJ mais claro, validável e aderente à modelagem
     * conceitual.
     */
    public ClienteModel criarCliente(PessoaModel pessoa) {
        ClienteModel cliente = new ClienteModel();
        cliente.setPessoa(pessoa);
        return cliente;
    }

    @Override
    /**
     * Função: Cria a especialização PessoaFisica ou PessoaJuridica com os dados específicos do tipo
     * selecionado.
     * Padrão aplicado: FACTORY METHOD.
     * Justificativa: garante que cada tipo de cliente tenha sua própria regra de montagem sem misturar
     * os campos de CPF e CNPJ.
     * Uso no sistema: torna o cadastro de clientes PF/PJ mais claro, validável e aderente à modelagem
     * conceitual.
     */
    public PessoaJuridicaModel criarEspecializacao(ClienteModel cliente, ClientePessoaJuridicaDTO dto) {
        PessoaJuridicaModel pessoaJuridica = new PessoaJuridicaModel();
        pessoaJuridica.setCliente(cliente);
        pessoaJuridica.setCnpj(onlyDigits(dto.getCnpj()));
        pessoaJuridica.setRazaoSocial(dto.getRazaoSocial());
        pessoaJuridica.setNomeFantasia(dto.getNomeFantasia());
        pessoaJuridica.setInscricaoEstadual(dto.getInscricaoEstadual());
        return pessoaJuridica;
    }

    /**
     * Função: Participa da criação padronizada de cliente no fluxo only digits.
     * Padrão aplicado: FACTORY METHOD.
     * Justificativa: organiza a instanciação das entidades da hierarquia Pessoa, Cliente e
     * especializações.
     * Uso no sistema: torna o cadastro de clientes PF/PJ mais claro, validável e aderente à modelagem
     * conceitual.
     */
    private String onlyDigits(String value) {
        return value == null ? null : value.replaceAll("\\D", "");
    }
}
