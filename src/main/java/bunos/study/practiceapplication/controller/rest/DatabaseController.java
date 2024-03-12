package bunos.study.practiceapplication.controller.rest;

import bunos.study.practiceapplication.domain.dto.Response;
import bunos.study.practiceapplication.domain.model.Database;
import bunos.study.practiceapplication.service.DatabaseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/databases")
@AllArgsConstructor
public class DatabaseController {

    private final DatabaseService databaseService;

    @PostMapping("/add")
    public ResponseEntity<Response> addDatabase(@RequestBody Database database) {
        return databaseService.add(database);
    }

    @DeleteMapping("/remove/{name}")
    public ResponseEntity<Response> removeDatabase(@PathVariable String name) {
        return databaseService.remove((Database)databaseService
                .getByName(name)
                .getBody()
                .getData());
    }

    @PatchMapping("/edit")
    public ResponseEntity<Response> editDatabase(@RequestBody Database database) {
        return databaseService.update(database);
    }

    @GetMapping("/database")
    public ResponseEntity<Response> getDatabase(@RequestParam(name = "name") String name) {
        return databaseService.getByName(name);
    }

    @GetMapping("/database/connection-state")
    public ResponseEntity<Response> getConnectionState(@RequestBody Database database) {
        return databaseService.getConnectionStatus(database);
    }

    @GetMapping("/{name}/url")
    public ResponseEntity<Response> getUrl(@PathVariable String name) {
        return databaseService.getUrl((Database) databaseService
                .getByName(name)
                .getBody()
                .getData());
    }

    @GetMapping
    public ResponseEntity<Response> getAllDatabases() {
        return databaseService.getAllDatabases();
    }
}
