package br.com.avcar.oficina.business.veiculo.repository;

import br.com.avcar.oficina.business.veiculo.model.MarcaModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface IMarcaRepository extends IGenericRepository<MarcaModel> {

    boolean existsByNomeMarcaIgnoreCaseAndAtivoTrue(String nomeMarca);

    boolean existsByNomeMarcaIgnoreCaseAndIdNotAndAtivoTrue(String nomeMarca, Long id);

    Page<MarcaModel> findByNomeMarcaContainingIgnoreCaseAndAtivoTrue(String termo, Pageable pageable);
}
