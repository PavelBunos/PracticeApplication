package bunos.study.practiceapplication.repository;

import bunos.study.practiceapplication.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> { }
