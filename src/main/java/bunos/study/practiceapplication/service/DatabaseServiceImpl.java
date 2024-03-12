package bunos.study.practiceapplication.service;

import bunos.study.practiceapplication.domain.dto.Response;
import bunos.study.practiceapplication.domain.model.Database;
import bunos.study.practiceapplication.repository.DatabaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Service
@Slf4j
public class DatabaseServiceImpl implements DatabaseService {

    private final DatabaseRepository databaseRepository;

    @Override
    public ResponseEntity<Response> add(Database database) {
        try {
            databaseRepository.save(database);
            return new ResponseEntity<>(Response.builder()
                    .data("База данных сохранена!")
                    .build(),
                    HttpStatus.OK);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(Response.builder()
                    .data("Не удалось сохранить базу данных")
                    .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Response> remove(Database database) {
        try {
            databaseRepository.delete(database);
            return new ResponseEntity<>(Response.builder()
                    .data("База данных удалена!")
                    .build(),
                    HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(Response.builder()
                    .data("База данных не была удалена!")
                    .build(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Response> getByName(String name) {
        try {
            return new ResponseEntity<>(Response.builder()
                    .data(databaseRepository
                            .findAll()
                            .stream()
                            .filter(database -> database
                                    .getName()
                                    .equals(name))
                            .findFirst()
                            .get())
                    .build(),
                    HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(Response.builder()
                    .data("Не удалось найти базу данных!")
                    .build(),
                    HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<Response> update(Database database) {
        try {
            databaseRepository.save(database);
            return new ResponseEntity<>(Response.builder()
                    .data(database)
                    .build(),
                    HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(Response.builder()
                    .data("Не удалось обновить базу данных")
                    .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Response> getAllDatabases() {
        try {
            return new ResponseEntity<>(Response.builder()
                    .data(databaseRepository.findAll())
                    .build(),
                    HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(Response.builder()
                    .data("Не удалось получить список баз данных")
                    .build(),
                    HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<Response> getUrl(@RequestBody Database database) {
        try {
            return new ResponseEntity<>(Response.builder()
                    .data(database.getUrl())
                    .build(),
                    HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(Response.builder()
                    .data("Не удалось получить URL")
                    .build(),
                    HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<Response> getConnectionStatus(@RequestBody Database database) {
        try {
            return new ResponseEntity<>(Response.builder()
                    .data(database.getConnectionStatus())
                    .build(),
                    HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(Response.builder()
                    .data("Не удалось проверить соединение!")
                    .build(),
                    HttpStatus.NOT_FOUND);
        }
    }
}
