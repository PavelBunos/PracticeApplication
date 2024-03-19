package bunos.study.practiceapplication.controller.rest;

import bunos.study.practiceapplication.domain.dto.MigrationData;
import bunos.study.practiceapplication.service.MigrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/migration")
public class MigrationController {
    private final MigrationService migrationService;

    @PostMapping("/start")
    public ResponseEntity<?> migration(@RequestBody MigrationData migrationData) {
        log.info("Migration requested");

        return migrationService.migrate(
                migrationData.getConnection(),
                migrationData.getTablenameSource(),
                migrationData.getTablenameOutput()
        );
    }

}
