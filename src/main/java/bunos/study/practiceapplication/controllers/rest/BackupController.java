package bunos.study.practiceapplication.controllers.rest;

import bunos.study.practiceapplication.services.BackupService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/backup")
public class BackupController {
    private final BackupService backupService;

    @GetMapping("/simple/start")
    public void simpleBackup() {
        backupService.startBackup();
    }
}
