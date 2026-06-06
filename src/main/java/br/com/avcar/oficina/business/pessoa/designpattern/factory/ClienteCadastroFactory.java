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
@Component
public class ClienteCadastroFactory {

    private final Map<TipoCliente, ClienteFactoryMethod<?, ?>> factories = new EnumMap<>(TipoCliente.class);

    public ClienteCadastroFactory(List<ClienteFactoryMethod<?, ?>> factoryMethods) {
        for (ClienteFactoryMethod<?, ?> factoryMethod : factoryMethods) {
            factories.put(factoryMethod.getTipoCliente(), factoryMethod);
        }
    }

    @SuppressWarnings("unchecked")
    public <F extends ClienteFactoryMethod<?, ?>> F obterFactory(TipoCliente tipoCliente) {
        ClienteFactoryMethod<?, ?> factory = factories.get(tipoCliente);
        if (factory == null) {
            throw new BusinessException("Tipo de cliente não suportado para cadastro.");
        }
        return (F) factory;
    }
}
