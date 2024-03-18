package bunos.study.practiceapplication.service;

import bunos.study.practiceapplication.domain.model.Role;
import bunos.study.practiceapplication.domain.model.User;
import bunos.study.practiceapplication.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
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
