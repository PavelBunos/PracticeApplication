package bunos.study.practiceapplication.controller.rest;

import bunos.study.practiceapplication.domain.dto.Response;
import bunos.study.practiceapplication.domain.model.Journal;
import bunos.study.practiceapplication.service.ExcelReportService;
import bunos.study.practiceapplication.service.JournalService;
import bunos.study.practiceapplication.service.LogService;
import bunos.study.practiceapplication.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/logs")
public class LogsController {
    private final LogService logService;
    private final JournalService journalService;
    private final ExcelReportService excelReportService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllLogs() {
        return new ResponseEntity<>(Response.builder().data(logService.getAllLogs()).build(), HttpStatus.OK);
    }

    @GetMapping("/data/{data}")
    public ResponseEntity<?> getLogsByData(@PathVariable String data) {
        return new ResponseEntity<>(Response.builder().data(logService.getLogsByData(data)).build(), HttpStatus.OK);
    }

    @GetMapping("/save/{journalData}")
    public void export(HttpServletResponse response, @PathVariable String journalData) {
        Journal journal = journalService.getJournal(LocalDate.parse(journalData.substring(0, 10)),
                userService.findByUsername(journalData.substring(11, journalData.length()))
        );

        Workbook workbook = excelReportService.export(logService.getLogsByJournal(journal).stream().toList());

        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=logs.xlsx");
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }

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
