package bunos.study.practiceapplication.services;

import bunos.study.practiceapplication.models.User;

import java.util.List;

public interface UserService {
    void add(User user);
    void remove(User user);

    User getById(long id);
    User update(User user);

    List<User> getAllUsers();
}
