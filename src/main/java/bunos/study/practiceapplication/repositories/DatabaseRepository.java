package bunos.study.practiceapplication.repositories;

import bunos.study.practiceapplication.models.databases.Database;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatabaseRepository extends JpaRepository<Database, Long> {
}
