package br.com.avcar.oficina.view.api;

import br.com.avcar.oficina.core.database.DatabaseConnectionChecker;
import br.com.avcar.oficina.core.response.ApiResponse;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/database")
public class DatabaseStatusController {

    private final DatabaseConnectionChecker databaseConnectionChecker;

    public DatabaseStatusController(DatabaseConnectionChecker databaseConnectionChecker) {
        this.databaseConnectionChecker = databaseConnectionChecker;
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> status() {
        boolean available = databaseConnectionChecker.isDatabaseAvailable();
        return ResponseEntity.ok(ApiResponse.success(
                "Status da conexão local com o banco de dados verificado com sucesso.",
                Map.of("available", available)
        ));
    }
}
