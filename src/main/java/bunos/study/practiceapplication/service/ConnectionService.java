package bunos.study.practiceapplication.service;

import bunos.study.practiceapplication.domain.model.Connection;

import java.util.List;

public interface ConnectionService {
    List<Connection> getAllConnections();
    Connection getConnectionByDatabases(String database1, String database2);

    void remove(Connection connection);
    void add(Connection connection);
}
