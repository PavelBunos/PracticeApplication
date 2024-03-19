package bunos.study.practiceapplication.service;

import bunos.study.practiceapplication.domain.model.Connection;
import bunos.study.practiceapplication.domain.model.Database;
import bunos.study.practiceapplication.repository.ConnectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionServiceImpl implements ConnectionService {
    private final ConnectionRepository connectionRepository;
    @Override
    public List<Connection> getAllConnections() {
        return connectionRepository.findAll().stream().toList();
    }

    @Override
    public Connection getConnectionByDatabases(String database1, String database2) {
        return connectionRepository.findAll().stream()
                .filter(x -> x.getFromDatabase().getName().equals(database1) && x.getToDatabase().getName().equals(database2))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void remove(Connection connection) {
        connectionRepository.delete(connection);
    }

    @Override
    public void add(Connection connection) {
        connectionRepository.save(connection);
    }
}
