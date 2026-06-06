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

    public DatabaseStatusController(DatabaseConnectionChecker databaseConnectionChecker) {
        this.databaseConnectionChecker = databaseConnectionChecker;
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse<DatabaseStatusResult>> status() {
        DatabaseStatusResult result = databaseConnectionChecker.checkStatus();
        return ResponseEntity.ok(ApiResponse.success(result.getMensagem(), result));
    }
}
