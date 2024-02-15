package bunos.study.practiceapplication.controllers.rest;

import bunos.study.practiceapplication.dtos.DumpRequest;
import bunos.study.practiceapplication.dtos.exceptions.NotificationResponce;
import bunos.study.practiceapplication.services.DumpService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/dump")
public class DumpController {

    private final DumpService dumpService;

    @PostMapping("/create")
    public ResponseEntity<NotificationResponce> dump(@RequestBody DumpRequest dumpData) {
        try {
            dumpService.createDump(dumpData.getPath(), dumpData.getArgs());
        } catch (Exception e) {
            log.error("Dump process failure: ", e);
            return ResponseEntity.ok().body(new NotificationResponce("Внимание!", "Копирование завершилось с ошибкой!", true));
        }
        return ResponseEntity.ok().body(new NotificationResponce("Успешно!", "Копирование выполнено успешно!", false));
    }

    @PostMapping("/restore/to")
    public ResponseEntity<NotificationResponce> restoreFromDump(@RequestBody DumpRequest dumpData) {
        try {
            dumpService.restore(dumpData.getPath(), dumpData.getDumpFileName(), dumpData.getArgs());
        } catch (Exception e) {
            log.error("Restore from dump process failure", e);
            return ResponseEntity.ok().body(new NotificationResponce("Внимание!", "Восстановление завершилось с ошибкой!", true));
        }
        return ResponseEntity.ok().body(new NotificationResponce("Успешно!", "Восстановление выполнено успешно!", false));
    }

    @GetMapping("/backups")
    public ResponseEntity<?> filesList(@RequestParam(name = "path") String path) {
        return ResponseEntity.ok(dumpService.getBackupFilenames(path));
    }
}
