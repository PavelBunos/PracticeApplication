package bunos.study.practiceapplication.service;

import bunos.study.practiceapplication.domain.dto.Response;
import bunos.study.practiceapplication.domain.model.Database;
import org.springframework.http.ResponseEntity;

public interface DatabaseService {
    ResponseEntity<Response> add(Database database);

    ResponseEntity<Response> remove(Database database);

    ResponseEntity<Response> getByName(String name);

    ResponseEntity<Response> update(Database database);

    ResponseEntity<Response> getAllDatabases();

    ResponseEntity<Response> getUrl(Database database);

    ResponseEntity<Response> getConnectionStatus(Database database);
}
