package br.com.avcar.oficina.business.pessoa.repository;

import br.com.avcar.oficina.business.pessoa.model.PessoaFisicaModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

@Repository
public interface IPessoaFisicaRepository extends IGenericRepository<PessoaFisicaModel> {

    boolean existsByCpfAndAtivoTrue(String cpf);

    boolean existsByCpfAndIdNotAndAtivoTrue(String cpf, Long id);

    @EntityGraph(attributePaths = {"cliente", "cliente.pessoa"})
    Optional<PessoaFisicaModel> findByCpfAndAtivoTrue(String cpf);

    @Override
    @EntityGraph(attributePaths = {"cliente", "cliente.pessoa"})
    Optional<PessoaFisicaModel> findByIdAndAtivoTrue(Long id);
}
