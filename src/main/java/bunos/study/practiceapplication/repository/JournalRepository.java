package bunos.study.practiceapplication.repository;

import bunos.study.practiceapplication.domain.model.Journal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalRepository extends JpaRepository<Journal, Long> {
}
