package bunos.study.practiceapplication.service;

import bunos.study.practiceapplication.domain.model.Connection;
import bunos.study.practiceapplication.domain.model.Table;
import org.springframework.http.ResponseEntity;

public interface MigrationService {
    ResponseEntity migrate(Connection connection, String table1, String table2);
}
