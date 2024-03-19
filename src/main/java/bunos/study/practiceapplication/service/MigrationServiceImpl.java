package bunos.study.practiceapplication.service;

import bunos.study.practiceapplication.domain.dto.Response;
import bunos.study.practiceapplication.domain.model.Column;
import bunos.study.practiceapplication.domain.model.Connection;
import bunos.study.practiceapplication.domain.model.Database;
import bunos.study.practiceapplication.domain.model.Table;
import bunos.study.practiceapplication.repository.ConnectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MigrationServiceImpl implements MigrationService {
    private final ConnectionRepository connectionRepository;
    private final DatabaseMetadataService databaseMetadataService;

    private Table getSourceTableByName(Connection connection, String name) {
        return (
                (List<Table>)
                        ( (Response) databaseMetadataService.getTables(connection.getFromDatabase()).getBody() ).getData()
        ).stream().filter(x -> x.getName().equals(name)).findFirst().get();
    }

    private Table getOutputTableByName(Connection connection, String name) {
        return (
                (List<Table>)
                        ( (Response) databaseMetadataService.getTables(connection.getToDatabase()).getBody() ).getData()
        ).stream().filter(x -> x.getName().equals(name)).findFirst().get();
    }

    private DataSource getDataSource(Database database) {
        DriverManagerDataSource d = new DriverManagerDataSource();
        d.setDriverClassName("org.postgresql.Driver");
        d.setUsername(database.getUser());
        d.setPassword(database.getPassword());
        d.setUrl(database.getUrl());
        return d;
    }

    @Override
    public ResponseEntity<?> migrate(Connection connection, String tablenameSource, String tablenameOutput) {
        log.info(tablenameSource + " -> " + tablenameOutput);

        Table tableSource = getSourceTableByName(connection, tablenameSource);
        Table tableOutput = getOutputTableByName(connection, tablenameOutput);

        List<Column> tableSourceColumns = tableSource.getColumns();
        List<Column> tableOutputColumns = tableSource.getColumns();

        if (tableSourceColumns.size() != tableOutputColumns.size()) {
            return new ResponseEntity<>(Response.builder().data("Таблицы не совпадают!").build(), HttpStatus.BAD_REQUEST);
        }

        try {
            java.sql.Connection sourceDbConnection = getDataSource(connection.getFromDatabase()).getConnection();
            java.sql.Connection destinationDbConnection = getDataSource(connection.getToDatabase()).getConnection();

            String selectQuery = buildSelectQuery(tableSource);

            Statement selectStatement = sourceDbConnection.createStatement();
            ResultSet resultSet = selectStatement.executeQuery(selectQuery);

            String insertQuery = buildInsertQuery(tableOutput);

            while (resultSet.next()) {
                PreparedStatement insertStatement = destinationDbConnection.prepareStatement(insertQuery);
                int columnIndex = 1;
                for (int i = 0; i < tableOutput.getColumns().size(); i++) {
                    if (!tableOutput.getColumns().get(i).isAutoincrement()) {
                        insertStatement.setObject(columnIndex++, resultSet.getObject(i + 1));
                    }
                }
                insertStatement.executeUpdate();
            }

            resultSet.close();
            selectStatement.close();
            sourceDbConnection.close();
            destinationDbConnection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(Response.builder().data("Что-то пошло не так!").build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(Response.builder().data("Миграция выполнена успешно!").build(), HttpStatus.OK);
    }

    private String buildSelectQuery(Table table) {
        StringBuilder query = new StringBuilder("SELECT ");
        List<Column> columns = table.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            query.append(columns.get(i).getName());
            if (i < columns.size() - 1) {
                query.append(", ");
            }
        }
        query.append(" FROM ").append(table.getName());
        return query.toString();
    }

    private String buildInsertQuery(Table table) {
        StringBuilder query = new StringBuilder("INSERT INTO ").append(table.getName()).append(" (");
        List<Column> columns = table.getColumns();
        List<String> columnNames = new ArrayList<>();

        for (Column column : columns) {
            if (!column.isAutoincrement()) {
                columnNames.add(column.getName());
            }
        }

        for (int i = 0; i < columnNames.size(); i++) {
            query.append(columnNames.get(i));
            if (i < columnNames.size() - 1) {
                query.append(", ");
            }
        }
        query.append(") VALUES (");
        for (int i = 0; i < columnNames.size(); i++) {
            query.append("?");
            if (i < columnNames.size() - 1) {
                query.append(", ");
            }
        }
        query.append(")");

        log.info(query.toString());

        return query.toString();
    }
}
