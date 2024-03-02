package bunos.study.practiceapplication.service;

import bunos.study.practiceapplication.domain.model.Role;

import java.util.List;

public interface RoleService {
    Role getById(long id);
    List<Role> getAllRoles();
    Role getByName(String name);
}
