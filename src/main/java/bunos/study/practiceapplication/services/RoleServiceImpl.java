package bunos.study.practiceapplication.services;

import bunos.study.practiceapplication.models.security.Role;
import bunos.study.practiceapplication.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getById(long id) {
        return roleRepository.getById(id);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getByName(String name) {
        return roleRepository
                .findAll()
                .stream()
                .filter(
                        role -> role
                                .getName()
                                .equals(name)
                )
                .findFirst()
                .get();
    }
}
