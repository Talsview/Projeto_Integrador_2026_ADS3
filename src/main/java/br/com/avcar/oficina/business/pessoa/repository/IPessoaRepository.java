package br.com.avcar.oficina.business.pessoa.repository;

import br.com.avcar.oficina.business.pessoa.model.PessoaModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface IPessoaRepository extends IGenericRepository<PessoaModel> {

    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    Optional<PessoaModel> findByEmailIgnoreCaseAndAtivoTrue(String email);
}
