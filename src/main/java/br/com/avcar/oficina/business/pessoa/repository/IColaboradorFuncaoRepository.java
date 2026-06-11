package br.com.avcar.oficina.business.pessoa.repository;

import br.com.avcar.oficina.business.pessoa.model.ColaboradorFuncaoModel;
import br.com.avcar.oficina.core.repository.IGenericRepository;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

@Repository
public interface IColaboradorFuncaoRepository extends IGenericRepository<ColaboradorFuncaoModel> {

    @EntityGraph(attributePaths = {"colaborador", "funcao"})
    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    List<ColaboradorFuncaoModel> findByColaboradorIdAndAtivoTrue(Long colaboradorId);

    @EntityGraph(attributePaths = {"colaborador", "funcao"})
    /**
     * Função: Declara uma consulta que retorna apenas registros ativos, preservando a inativação
     * lógica.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    List<ColaboradorFuncaoModel> findByColaboradorIdAndAtivoTrueAndDataFimIsNull(Long colaboradorId);

    @EntityGraph(attributePaths = {"colaborador", "funcao"})
    /**
     * Função: Declara uma consulta para recuperar registros inativados, permitindo revisão e
     * reativação posterior.
     * Uso no sistema: mantém a regra de consulta no repositório e evita SQL espalhado pelas telas ou
     * serviços.
     */
    List<ColaboradorFuncaoModel> findByColaboradorIdAndAtivoFalse(Long colaboradorId);
}
