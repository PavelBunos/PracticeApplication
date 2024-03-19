package bunos.study.practiceapplication.service;

import bunos.study.practiceapplication.domain.model.Journal;
import bunos.study.practiceapplication.domain.model.Log;
import bunos.study.practiceapplication.domain.model.User;
import org.springframework.http.HttpStatus;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public interface LogService {
    List<Log> getAllLogs();
    List<Log> getUserLogs(User user);
    List<Log> getLogsByDate(LocalDate date);
    List<Log> getLogsByData(String data);
    List<Log> getLogsByJournal(Journal journal);
    void log(String info, User user, int status);
    void log(String info, User user);
}
