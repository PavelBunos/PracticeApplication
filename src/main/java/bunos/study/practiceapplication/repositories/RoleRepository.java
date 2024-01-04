package bunos.study.practiceapplication.repositories;

import bunos.study.practiceapplication.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> { }
