package bunos.study.practiceapplication.controllers.rest;

import bunos.study.practiceapplication.dtos.DumpRequest;
import bunos.study.practiceapplication.dtos.exceptions.NotificationResponce;
import bunos.study.practiceapplication.services.BackupService;
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
    public ResponseEntity<NotificationResponce> dump(@RequestBody DumpRequest backupData) {
        try {
            backupService.createDump(backupData.getPath(), backupData.getArgs(), backupData.getDatabase());
        }
        catch (Exception e) {
            return ResponseEntity.ok().body(new NotificationResponce("Ошибка!", "Копирование завершено с ошибкой!", true));
        }
        return ResponseEntity.ok().body(new NotificationResponce("Успешно!", "Копирование выполнено успешно!", false));
    }

    @PostMapping("/restore/to")
    public ResponseEntity<NotificationResponce> restoreFromDump(@RequestBody DumpRequest backupData) {
        try {
            backupService.restore(backupData.getPath(), backupData.getDumpFileName(), backupData.getArgs(), backupData.getDatabase());
        }
        catch (Exception e) {
            return ResponseEntity.ok().body(new NotificationResponce("Ошибка!", "Восстановление завершено с ошибкой!", true));
        }
        return ResponseEntity.ok().body(new NotificationResponce("Успешно!", "Восстановление выполнено успешно!", false));
    }

    @GetMapping("/backups")
    public ResponseEntity<?> filesList(@RequestParam(name = "path") String path) {
        return ResponseEntity.ok(backupService.getBackups(path));
    }
}
