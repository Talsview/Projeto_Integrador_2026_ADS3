package br.com.avcar.oficina.core.controller;

import br.com.avcar.oficina.core.dto.BaseDTO;
import br.com.avcar.oficina.core.model.BaseModel;
import br.com.avcar.oficina.core.service.IGenericService;

/**
 * Classe base genérica para Controllers REST do sistema.
 *
 * <p>O objetivo desta classe não é substituir os endpoints específicos de cada módulo,
 * pois o sistema possui regras próprias para clientes, veículos, ordens de serviço,
 * peças, pagamentos, garantias e histórico. A função dela é deixar explícita a
 * aplicação de Generics também na camada Controller, mantendo um contrato comum
 * para os controllers que trabalham com entidades persistentes.</p>
 *
 * <p>E = entidade persistida no banco, sempre derivada de {@link BaseModel}.</p>
 * <p>D = DTO utilizado pela API, sempre derivado de {@link BaseDTO}.</p>
 * <p>S = service do módulo, sempre compatível com {@link IGenericService}.</p>
 */
public abstract class GenericController<
        E extends BaseModel,
        D extends BaseDTO,
        S extends IGenericService<E>> {

    protected final S genericService;

    /**
     * Função: recebe o service genérico associado ao controller concreto.
     * Uso no sistema: permite que a camada Controller também evidencie a herança genérica,
     * sem remover os endpoints específicos exigidos pelas regras da oficina.
     */
    protected GenericController(S genericService) {
        this.genericService = genericService;
    }

    /**
     * Função: retorna o service genérico do controller concreto.
     * Uso no sistema: serve como ponto comum para documentação, testes e manutenção da
     * arquitetura em camadas com Generics.
     */
    protected S getGenericService() {
        return genericService;
    }
}
