package bunos.study.practiceapplication.service;

import bunos.study.practiceapplication.domain.model.Journal;
import bunos.study.practiceapplication.domain.model.Log;
import bunos.study.practiceapplication.domain.model.User;
import bunos.study.practiceapplication.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {
    private final LogRepository logRepository;
    private final JournalService journalService;

    @Override
    public List<Log> getAllLogs() {
        return logRepository.findAll().stream().toList();
    }

    @Override
    public List<Log> getUserLogs(User user) {
        List<Log> logs = new ArrayList<>();

        List<Journal> journals = journalService.getJournalsByUser(user);
        for (Journal j : journals) {
            logs.addAll(logRepository.findAll().stream().filter(x -> x.getJournal().equals(j)).toList());
        }

        return logs;
    }

    @Override
    public List<Log> getLogsByData(String data) {
        List<Log> logs = new ArrayList<>();

        List<Journal> journals = journalService.getJournalsByDate(
                LocalDate.parse(
                        data.substring(0, 10)
                )
        ).stream().filter(x -> x
                .getUser()
                .getUsername()
                .equals(
                        data.substring(11,data.length())
                )
        ).toList();
        for (Journal j : journals) {
            logs.addAll(logRepository.findAll().stream().filter(x -> x.getJournal().equals(j)).toList());
        }

        return logs;
    }

    @Override
    public List<Log> getLogsByDate(LocalDate date) {
        List<Log> logs = new ArrayList<>();

        List<Journal> journals = journalService.getJournalsByDate(date);
        for (Journal j : journals) {
            logs.addAll(logRepository.findAll().stream().filter(x -> x.getJournal().equals(j)).toList());
        }

        return logs;
    }

    @Override
    public void log(String info, User user, int status) {
        Journal journal = null;
        try {
            journal = journalService.getJournal(LocalDate.now(), user);
        } catch (NoSuchElementException ex) {
            log.info("Нет журнала");
        }

        if (journal == null) {
            journal = Journal.builder()
                    .user(user)
                    .date(LocalDate.now())
                    .build();
            journalService.add(journal);
        }

        logRepository.save(Log.builder()
                .time(LocalTime.now())
                .data(info)
                .journal(journal)
                .status(status)
                .build());
    }

    @Override
    public void log(String info, User user) {
        Journal journal = null;
        try {
            journal = journalService.getJournal(LocalDate.now(), user);
        } catch (NoSuchElementException ex) {
            log.info("Нет журнала");
        }

        if (journal == null) {
            journal = Journal.builder()
                    .user(user)
                    .date(LocalDate.now())
                    .build();
            journalService.add(journal);
        }

        logRepository.save(Log.builder()
                .time(LocalTime.now())
                .data(info)
                .journal(journal)
                .status(0)
                .build());
    }
}
