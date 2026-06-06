package br.com.avcar.oficina.business.pessoa.repository;

import br.com.avcar.oficina.business.pessoa.model.PessoaJuridicaModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

@Repository
public interface IPessoaJuridicaRepository extends IGenericRepository<PessoaJuridicaModel> {

    boolean existsByCnpjAndAtivoTrue(String cnpj);

    boolean existsByCnpjAndIdNotAndAtivoTrue(String cnpj, Long id);

    @EntityGraph(attributePaths = {"cliente", "cliente.pessoa"})
    Optional<PessoaJuridicaModel> findByCnpjAndAtivoTrue(String cnpj);

    @Override
    @EntityGraph(attributePaths = {"cliente", "cliente.pessoa"})
    Optional<PessoaJuridicaModel> findByIdAndAtivoTrue(Long id);
}
