package bunos.study.practiceapplication.controller.rest;

import bunos.study.practiceapplication.domain.dto.Backup;
import bunos.study.practiceapplication.service.BackupService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/dump")
public class BackupController {

    private final BackupService backupService;

    @PostMapping("/create")
    public ResponseEntity backup(@RequestBody Backup backupData) {
        return backupService.createBackup(backupData.getPath(), backupData.getArgs(), backupData.getDatabase());
    }

    @PostMapping("/restore")
    public ResponseEntity<?> restore(@RequestBody Backup backupData) {
        return backupService.restore(backupData.getPath(), backupData.getDumpFileName(), backupData.getArgs(), backupData.getDatabase());
    }

    @GetMapping("/backups")
    public ResponseEntity<?> filesList(@RequestParam(name = "path") String path) {
        return backupService.getBackups(path);
    }
}
