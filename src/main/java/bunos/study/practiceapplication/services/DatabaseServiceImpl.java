package bunos.study.practiceapplication.services;

import bunos.study.practiceapplication.models.databases.Database;
import bunos.study.practiceapplication.repositories.DatabaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DatabaseServiceImpl implements DatabaseService {

    private final DatabaseRepository databaseRepository;

    @Override
    public void add(Database database) {
        databaseRepository.save(database);
    }

    @Override
    public void remove(Database database) {
        databaseRepository.delete(database);
    }

    @Override
    public Database getByName(String name) {
        return databaseRepository
                .findAll()
                .stream()
                .filter(
                        database -> database
                                .getName()
                                .equals(name)
                )
                .findFirst()
                .get();
    }

    @Override
    public Database update(Database database) {
        databaseRepository.save(database);
        return database;
    }

    @Override
    public Database getById(long id) {
        return databaseRepository.findById(id).get();
    }

    @Override
    public List<Database> getAllDatabases() {
        return databaseRepository.findAll();
    }
}
