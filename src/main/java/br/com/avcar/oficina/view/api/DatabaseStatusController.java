package br.com.avcar.oficina.view.api;

import br.com.avcar.oficina.core.database.DatabaseConnectionChecker;
import br.com.avcar.oficina.core.database.DatabaseStatusResult;
import br.com.avcar.oficina.core.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/database")
public class DatabaseStatusController {

    private final DatabaseConnectionChecker databaseConnectionChecker;

    /**
     * Função: Recebe as dependências necessárias para esta classe e as guarda em atributos finais.
     * Uso no sistema: permite que o Spring ou o Angular injete serviços, repositórios e validadores
     * sem criação manual dentro dos métodos.
     */
    public DatabaseStatusController(DatabaseConnectionChecker databaseConnectionChecker) {
        this.databaseConnectionChecker = databaseConnectionChecker;
    }

    @GetMapping("/status")
    /**
     * Função: Consulta ou altera o status operacional, registrando a evolução do processo quando
     * necessário.
     * Uso no sistema: mantém o fluxo Orçamento, Execução, Pagamento e Finalizado rastreável.
     */
    public ResponseEntity<ApiResponse<DatabaseStatusResult>> status() {
        DatabaseStatusResult result = databaseConnectionChecker.checkStatus();
        return ResponseEntity.ok(ApiResponse.success(result.getMensagem(), result));
    }
}
