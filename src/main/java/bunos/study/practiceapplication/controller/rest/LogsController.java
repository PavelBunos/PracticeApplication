package bunos.study.practiceapplication.controller.rest;

import bunos.study.practiceapplication.domain.dto.Response;
import bunos.study.practiceapplication.service.JournalService;
import bunos.study.practiceapplication.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/logs")
public class LogsController {
    private final LogService logService;
    private final JournalService journalService;

    @GetMapping
    public ResponseEntity<?> getAllLogs() {
        return new ResponseEntity<>(Response.builder().data(logService.getAllLogs()).build(), HttpStatus.OK);
    }

    @GetMapping("/data/{data}")
    public ResponseEntity<?> getLogsByData(@PathVariable String data) {
        return new ResponseEntity<>(Response.builder().data(logService.getLogsByData(data)).build(), HttpStatus.OK);
    }

    @GetMapping("/journals")
    public ResponseEntity<?> getAllJournals() {
        return new ResponseEntity<>(Response.builder().data(journalService.getAllJournals()).build(), HttpStatus.OK);
    }

    @GetMapping("/journals/user/{username}")
    public ResponseEntity<?> getAllJournalsByUsername(@PathVariable String username) {
        return new ResponseEntity<>(Response.builder().data(
                journalService.getAllJournals().stream().filter(x -> x.getUser().getUsername().equals(username)) // todo - refactor
        ).build(), HttpStatus.OK);
    }
}
