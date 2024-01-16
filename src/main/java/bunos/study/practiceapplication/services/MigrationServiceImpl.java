package bunos.study.practiceapplication.services;

import bunos.study.practiceapplication.models.migration.TestEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MigrationServiceImpl implements MigrationService {

    @PersistenceContext(unitName = "from")
    private EntityManager sourceEntityManager;

    @PersistenceContext(unitName = "to")
    private EntityManager destinationEntityManager;

    @Transactional(value = "toTransactionManager")
    public void migrate() {
        dropDestinationData();
        saveAllToSource(getAllFromSource());
    }

    @Override
    @Transactional(value = "toTransactionManager")
    @Modifying
    public void dropDestinationData() {
        destinationEntityManager.createQuery("DELETE FROM TestEntity").executeUpdate();
    }

    @Override
    public List<TestEntity> getAllFromSource() {
        return sourceEntityManager.createQuery("SELECT e FROM TestEntity e", TestEntity.class).getResultList();
    }

    @Override
    @Transactional(value = "toTransactionManager")
    public void saveAllToSource(List<TestEntity> data) {
        data.forEach(destinationEntityManager::merge);
        log.info("Data migration done!");
    }
}
