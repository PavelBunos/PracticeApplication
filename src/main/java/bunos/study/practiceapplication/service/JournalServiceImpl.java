package bunos.study.practiceapplication.service;

import bunos.study.practiceapplication.domain.model.Journal;
import bunos.study.practiceapplication.domain.model.User;
import bunos.study.practiceapplication.repository.JournalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class JournalServiceImpl implements JournalService {
    private final JournalRepository journalRepository;

    @Override
    public void add(Journal journal) {
        journalRepository.save(journal);
    }

    @Override
    public List<Journal> getAllJournals() {
        return journalRepository.findAll().stream().toList();
    }

    @Override
    public List<Journal> getJournalsByUser(User user) {
        return journalRepository.findAll().stream().filter(x -> x.getUser().equals(user)).toList();
    }

    @Override
    public List<Journal> getJournalsByDate(LocalDate date) {
        return journalRepository.findAll().stream().filter(x -> x.getDate().equals(date)).toList();
    }

    @Override
    public Journal getJournal(LocalDate date, User user) {
        return journalRepository.findAll().stream().filter(x -> x.getDate().equals(date) && x.getUser().equals(user)).findFirst().get();
    }

    @Override
    public void delete(User user) {
        Journal journal = journalRepository.findAll().stream().filter(x -> x.getUser().equals(user)).findFirst().get();
        journalRepository.delete(journal);
    }
}
