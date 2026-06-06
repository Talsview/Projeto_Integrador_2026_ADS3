package br.com.avcar.oficina.business.pessoa.repository;

import br.com.avcar.oficina.business.pessoa.model.FuncaoModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface IFuncaoRepository extends IGenericRepository<FuncaoModel> {

    boolean existsByNomeFuncaoIgnoreCaseAndAtivoTrue(String nomeFuncao);

    boolean existsByNomeFuncaoIgnoreCaseAndIdNotAndAtivoTrue(String nomeFuncao, Long id);

    Optional<FuncaoModel> findByNomeFuncaoIgnoreCaseAndAtivoTrue(String nomeFuncao);

    Page<FuncaoModel> findByNomeFuncaoContainingIgnoreCaseAndAtivoTrue(String termo, Pageable pageable);
}
