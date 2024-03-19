package bunos.study.practiceapplication.controller.rest;

import bunos.study.practiceapplication.domain.dto.ConnectionData;
import bunos.study.practiceapplication.domain.dto.Response;
import bunos.study.practiceapplication.domain.model.Connection;
import bunos.study.practiceapplication.domain.model.Database;
import bunos.study.practiceapplication.service.ConnectionService;
import bunos.study.practiceapplication.service.DatabaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/connections")
public class ConnectionController {
    private final ConnectionService connectionService;
    private final DatabaseService databaseService;

    @GetMapping
    public List<Connection> getAllConnections() {
        return connectionService.getAllConnections();
    }

    @GetMapping("/connection")
    public Connection getConnection(@RequestBody ConnectionData connectionData) {
        return connectionService.getConnectionByDatabases(connectionData.getDatabaseSource(), connectionData.getDatabaseOutput());
    }

    @PostMapping("/remove")
    public void remove(@RequestBody ConnectionData connectionData) {
        connectionService.remove(
            connectionService.getConnectionByDatabases(connectionData.getDatabaseSource(), connectionData.getDatabaseOutput())
        );
    }

    @PostMapping("/add")
    public void add(@RequestBody ConnectionData connectionData) {
        Database databaseSource = (Database) ((Response) databaseService.getByName(connectionData.getDatabaseSource()).getBody()).getData();
        Database databaseOutput = (Database) ((Response) databaseService.getByName(connectionData.getDatabaseOutput()).getBody()).getData();

        if (connectionService.getConnectionByDatabases(databaseSource.getName(), databaseOutput.getName()) != null) {
            return; // todo
        }

        Connection connection = Connection.builder()
                .fromDatabase(databaseSource)
                .toDatabase(databaseOutput)
                .build();
        connectionService.add(connection);
    }
}
