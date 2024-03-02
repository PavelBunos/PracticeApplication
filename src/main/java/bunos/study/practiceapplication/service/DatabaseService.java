package bunos.study.practiceapplication.service;

import bunos.study.practiceapplication.domain.dto.Response;
import bunos.study.practiceapplication.domain.model.Database;
import org.springframework.http.ResponseEntity;

import javax.xml.crypto.Data;
import java.util.List;

public interface DatabaseService {
    void add(Database database);

    void remove(Database database);

    ResponseEntity<Response> getByName(String name);

    ResponseEntity<Response> update(Database database);

    ResponseEntity<Response> getAllDatabases();

    ResponseEntity<Response> getUrl(Database database);

    ResponseEntity<Response> getConnectionStatus(Database database);
}
