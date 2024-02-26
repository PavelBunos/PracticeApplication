package bunos.study.practiceapplication.services;

import bunos.study.practiceapplication.models.databases.Database;

import javax.xml.crypto.Data;
import java.util.List;

public interface DatabaseService {
    void add(Database database);

    void remove(Database database);

    Database getByName(String name);

    Database update(Database database);

    Database getById(long id);

    List<Database> getAllDatabases();
}
