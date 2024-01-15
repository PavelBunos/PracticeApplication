package bunos.study.practiceapplication.controllers.rest;

import bunos.study.practiceapplication.services.RestoreService;
import bunos.study.practiceapplication.services.MigrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/migration")
public class MigrationController {
    private final MigrationService migrationService;
    private final RestoreService restoreService;

    @GetMapping("/start")
    public void migrate() {
        migrationService.migrate();
    }

    @GetMapping("/source/restore")
    public void restore() {
        restoreService.migrationRestore();
    }
}
