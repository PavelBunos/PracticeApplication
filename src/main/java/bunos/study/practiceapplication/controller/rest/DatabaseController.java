package bunos.study.practiceapplication.controller.rest;

import bunos.study.practiceapplication.domain.dto.Response;
import bunos.study.practiceapplication.domain.model.Database;
import bunos.study.practiceapplication.domain.model.Table;
import bunos.study.practiceapplication.service.DatabaseMetadataService;
import bunos.study.practiceapplication.service.DatabaseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/databases")
@AllArgsConstructor
public class DatabaseController {

    private final DatabaseService databaseService;
    private final DatabaseMetadataService databaseMetadataService;

    @PostMapping("/add")
    public ResponseEntity<?> addDatabase(@RequestBody Database database) {
        return databaseService.add(database);
    }

    @DeleteMapping("/remove/{name}")
    public ResponseEntity<?> removeDatabase(@PathVariable String name) {
        return databaseService.remove((Database)databaseService
                .getByName(name)
                .getBody()
                .getData());
    }

    @PatchMapping("/edit")
    public ResponseEntity<?> editDatabase(@RequestBody Database database) {
        return databaseService.update(database);
    }

    @GetMapping("/database")
    public ResponseEntity<?> getDatabase(@RequestParam(name = "name") String name) {
        return databaseService.getByName(name);
    }

    @GetMapping("/database/tables/{database}")
    public ResponseEntity<?> getTables(@PathVariable String database) {
        return databaseMetadataService.getTables((Database) ((Response) databaseService.getByName(database).getBody()).getData());
    }

    @GetMapping("/database/connection-state")
    public ResponseEntity<?> getConnectionState(@RequestBody Database database) {
        return databaseService.getConnectionStatus(database);
    }

    @GetMapping("/{name}/url")
    public ResponseEntity<?> getUrl(@PathVariable String name) {
        return databaseService.getUrl((Database) databaseService
                .getByName(name)
                .getBody()
                .getData());
    }

    @GetMapping
    public ResponseEntity<?> getAllDatabases() {
        return databaseService.getAllDatabases();
    }
}
