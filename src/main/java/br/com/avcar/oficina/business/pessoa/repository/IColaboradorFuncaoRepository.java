package br.com.avcar.oficina.business.pessoa.repository;

import br.com.avcar.oficina.business.pessoa.model.ColaboradorFuncaoModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

@Repository
public interface IColaboradorFuncaoRepository extends IGenericRepository<ColaboradorFuncaoModel> {

    @EntityGraph(attributePaths = {"colaborador", "funcao"})
    List<ColaboradorFuncaoModel> findByColaboradorIdAndAtivoTrue(Long colaboradorId);

    @EntityGraph(attributePaths = {"colaborador", "funcao"})
    List<ColaboradorFuncaoModel> findByColaboradorIdAndAtivoTrueAndDataFimIsNull(Long colaboradorId);
}
