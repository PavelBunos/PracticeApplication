package bunos.study.practiceapplication.services;

import bunos.study.practiceapplication.models.Role;

import java.util.List;

public interface RoleService {
    Role getById(long id);
    List<Role> getAllRoles();
    Role getByName(String name);
}
