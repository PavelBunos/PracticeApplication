package bunos.study.practiceapplication.service;

import bunos.study.practiceapplication.domain.model.Journal;
import bunos.study.practiceapplication.domain.model.User;

import java.util.Date;
import java.util.List;

public interface JournalService {
    void createNewJournal(User user);
    Journal getJournalByUser(User user);
    List<Journal> getJournalsByDate(Date date);
    List<Journal> getAllJournals();
    void clear(User user);
}
