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

    TipoCliente getTipoCliente();

    PessoaModel criarPessoa(D dto);

    ClienteModel criarCliente(PessoaModel pessoa);

    E criarEspecializacao(ClienteModel cliente, D dto);
}
