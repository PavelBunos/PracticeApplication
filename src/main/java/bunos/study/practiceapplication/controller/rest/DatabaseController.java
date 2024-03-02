package bunos.study.practiceapplication.controller.rest;

import bunos.study.practiceapplication.domain.dto.Response;
import bunos.study.practiceapplication.domain.model.Database;
import bunos.study.practiceapplication.service.DatabaseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
        Response response;

        if (database.getConnectionStatus()) {
            try {
                databaseService.add(database);
                response = Response.builder()
                        .data("БД добавлена!")
                        .build();
                return ResponseEntity.ok().body(response);
            } catch (Exception ex) {
                log.error(ex.getMessage());
                response = Response.builder()
                        .data("БД уже существует!")
                        .build();
                return ResponseEntity.badRequest().body(response);
            }
        } else {
            response = Response.builder()
                    .data("Не удалось подключиться к БД!")
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/remove/{name}")
    public void removeDatabase(@PathVariable String name) {
        databaseService.remove((Database)databaseService.getByName(name).getBody().getData());
    }

    @PatchMapping("/edit")
    public ResponseEntity<?> editDatabase(@RequestBody Database database) {
        Database newDatabase = null;
        try {
            newDatabase = (Database) databaseService
                    .update(database)
                    .getBody()
                    .getData();
            return new ResponseEntity<>(newDatabase, HttpStatus.OK);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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
