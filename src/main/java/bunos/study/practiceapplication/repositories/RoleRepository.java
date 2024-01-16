package bunos.study.practiceapplication.repositories;

import bunos.study.practiceapplication.models.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> { }
