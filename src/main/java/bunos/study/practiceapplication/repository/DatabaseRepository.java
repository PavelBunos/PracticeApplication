package bunos.study.practiceapplication.repository;

import bunos.study.practiceapplication.domain.model.Database;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatabaseRepository extends JpaRepository<Database, Long> {
}
