package bunos.study.practiceapplication.repository;

import bunos.study.practiceapplication.domain.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
}
