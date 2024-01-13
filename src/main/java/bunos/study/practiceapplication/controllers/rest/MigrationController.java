package bunos.study.practiceapplication.controllers.rest;

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

    @GetMapping("/start")
    public void migrate() {
        migrationService.migrate();
    }
}
