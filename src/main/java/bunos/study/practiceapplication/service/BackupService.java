package bunos.study.practiceapplication.service;

import bunos.study.practiceapplication.domain.dto.Response;
import org.springframework.http.ResponseEntity;

public interface BackupService {

    ResponseEntity<Response> createBackup(String path, String args, String databaseName);

    ResponseEntity<Response> restore(String path, String dumpFileName, String args, String databaseName);

    ResponseEntity<Response> getBackups(String path);
}
