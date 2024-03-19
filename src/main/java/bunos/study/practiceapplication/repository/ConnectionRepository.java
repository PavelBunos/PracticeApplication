package bunos.study.practiceapplication.repository;

import bunos.study.practiceapplication.domain.model.Connection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {
}
