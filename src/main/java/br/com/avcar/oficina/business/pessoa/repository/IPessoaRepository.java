package br.com.avcar.oficina.business.pessoa.repository;

import br.com.avcar.oficina.business.pessoa.model.PessoaModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface IPessoaRepository extends IGenericRepository<PessoaModel> {

    Optional<PessoaModel> findByEmailIgnoreCaseAndAtivoTrue(String email);
}
