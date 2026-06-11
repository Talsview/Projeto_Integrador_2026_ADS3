package br.com.avcar.oficina.business.pessoa.designpattern.factory;

import br.com.avcar.oficina.business.pessoa.enums.TipoCliente;
import br.com.avcar.oficina.core.exception.BusinessException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Catálogo das fábricas concretas de Cliente.
 * Cada fábrica concreta implementa o Factory Method para uma especialização.
 */
/**
 * PADRÃO DE PROJETO: FACTORY METHOD.
 *
 * Função no sistema: centraliza a criação de clientes Pessoa Física e Pessoa Jurídica, evitando lógica condicional repetida no serviço de cadastro.
 * Justificativa funcional: este código participa de uma funcionalidade real do sistema e
 * evita que o padrão seja usado apenas como exemplo sem utilidade prática.
 */
@Component
public class ClienteCadastroFactory {

    private final Map<TipoCliente, ClienteFactoryMethod<?, ?>> factories = new EnumMap<>(TipoCliente.class);

    /**
     * Função: Seleciona a fábrica correta conforme o tipo de cliente informado no cadastro.
     * Padrão aplicado: FACTORY METHOD.
     * Justificativa: centraliza a decisão entre Pessoa Física e Pessoa Jurídica e evita condicionais
     * repetidas no ClienteService.
     * Uso no sistema: torna o cadastro de clientes PF/PJ mais claro, validável e aderente à modelagem
     * conceitual.
     */
    public ClienteCadastroFactory(List<ClienteFactoryMethod<?, ?>> factoryMethods) {
        for (ClienteFactoryMethod<?, ?> factoryMethod : factoryMethods) {
            factories.put(factoryMethod.getTipoCliente(), factoryMethod);
        }
    }

    @SuppressWarnings("unchecked")
    /**
     * Função: Seleciona a fábrica correta conforme o tipo de cliente informado no cadastro.
     * Padrão aplicado: FACTORY METHOD.
     * Justificativa: centraliza a decisão entre Pessoa Física e Pessoa Jurídica e evita condicionais
     * repetidas no ClienteService.
     * Uso no sistema: torna o cadastro de clientes PF/PJ mais claro, validável e aderente à modelagem
     * conceitual.
     */
    public <F extends ClienteFactoryMethod<?, ?>> F obterFactory(TipoCliente tipoCliente) {
        ClienteFactoryMethod<?, ?> factory = factories.get(tipoCliente);
        if (factory == null) {
            throw new BusinessException("Tipo de cliente não suportado para cadastro.");
        }
        return (F) factory;
    }
}
