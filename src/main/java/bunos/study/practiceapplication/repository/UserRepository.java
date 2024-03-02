package bunos.study.practiceapplication.repository;

import bunos.study.practiceapplication.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> { }
