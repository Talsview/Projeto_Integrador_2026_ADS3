package br.com.avcar.oficina.business.pessoa.designpattern.factory;

import br.com.avcar.oficina.business.pessoa.enums.TipoCliente;
import br.com.avcar.oficina.business.pessoa.model.ClienteModel;
import br.com.avcar.oficina.business.pessoa.model.PessoaModel;
import br.com.avcar.oficina.core.dto.BaseDTO;
import br.com.avcar.oficina.core.model.BaseModel;

/**
 * PADRÃO DE PROJETO: FACTORY METHOD
 *
 * Aplicação: define métodos de criação para Pessoa, Cliente e sua especialização
 * concreta, sem que a Controller conheça diretamente as classes de entidade que
 * precisam ser instanciadas.
 *
 * Justificativa: o Cliente possui especialização exclusiva e total entre Pessoa
 * Física e Pessoa Jurídica. O padrão permite criar corretamente cada família de
 * objetos mantendo o fluxo de cadastro organizado e extensível.
 */
public interface ClienteFactoryMethod<D extends BaseDTO, E extends BaseModel> {

    /**
     * Função: Participa da criação padronizada de cliente no fluxo get tipo cliente.
     * Padrão aplicado: FACTORY METHOD.
     * Justificativa: organiza a instanciação das entidades da hierarquia Pessoa, Cliente e
     * especializações.
     * Uso no sistema: torna o cadastro de clientes PF/PJ mais claro, validável e aderente à modelagem
     * conceitual.
     */
    TipoCliente getTipoCliente();

    /**
     * Função: Monta a entidade Pessoa com os dados comuns do cliente antes da especialização PF ou PJ.
     * Padrão aplicado: FACTORY METHOD.
     * Justificativa: separa a criação dos dados comuns da criação da especialização, respeitando a
     * hierarquia do modelo.
     * Uso no sistema: torna o cadastro de clientes PF/PJ mais claro, validável e aderente à modelagem
     * conceitual.
     */
    PessoaModel criarPessoa(D dto);

    /**
     * Função: Cria a entidade Cliente vinculada à Pessoa recém-cadastrada.
     * Padrão aplicado: FACTORY METHOD.
     * Justificativa: mantém a criação do cliente padronizada para os dois tipos de cadastro.
     * Uso no sistema: torna o cadastro de clientes PF/PJ mais claro, validável e aderente à modelagem
     * conceitual.
     */
    ClienteModel criarCliente(PessoaModel pessoa);

    /**
     * Função: Cria a especialização PessoaFisica ou PessoaJuridica com os dados específicos do tipo
     * selecionado.
     * Padrão aplicado: FACTORY METHOD.
     * Justificativa: garante que cada tipo de cliente tenha sua própria regra de montagem sem misturar
     * os campos de CPF e CNPJ.
     * Uso no sistema: torna o cadastro de clientes PF/PJ mais claro, validável e aderente à modelagem
     * conceitual.
     */
    E criarEspecializacao(ClienteModel cliente, D dto);
}
