package bunos.study.practiceapplication.controllers.rest;

import bunos.study.practiceapplication.models.databases.Database;
import bunos.study.practiceapplication.services.DatabaseService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/databases")
@AllArgsConstructor
public class DatabaseController {

    private final DatabaseService databaseService;

    @PostMapping("/add")
    public void addDatabase(@RequestBody Database database) {
        databaseService.add(database);
    }

    @DeleteMapping("/remove/{id}")
    public void removeDatabase(@PathVariable long id) {
        databaseService.remove(databaseService.getById(id));
    }

    @GetMapping("/{name}")
    public Database getDatabase(@PathVariable String name) {
        return databaseService.getByName(name);
    }

    @GetMapping
    public List<Database> getAllDatabases() {
        return databaseService.getAllDatabases();
    }
}
