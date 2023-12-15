package bunos.study.practiceapplication.repositories;

import bunos.study.practiceapplication.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> { }
