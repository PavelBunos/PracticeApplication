package bunos.study.practiceapplication.service;

import bunos.study.practiceapplication.domain.model.Journal;
import bunos.study.practiceapplication.domain.model.User;

import java.time.LocalDate;
import java.util.List;

public interface JournalService {
    void add(Journal journal);
    List<Journal> getAllJournals();
    List<Journal> getJournalsByUser(User user);
    List<Journal> getJournalsByDate(LocalDate date);
    Journal getJournal(LocalDate date, User user);
    void delete(User user);
}
