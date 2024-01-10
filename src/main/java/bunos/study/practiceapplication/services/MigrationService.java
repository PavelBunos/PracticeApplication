package bunos.study.practiceapplication.services;

import bunos.study.practiceapplication.models.migration.TestEntity;

import javax.sql.DataSource;
import java.util.List;

public interface MigrationService {

    void migrate();

    void dropDestinationData();

    List<TestEntity> getAllFromSource();

    void saveAllToSource(List<TestEntity> data);
}
