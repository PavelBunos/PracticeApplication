package bunos.study.practiceapplication.services;

import bunos.study.practiceapplication.models.migration.TestEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class MigrationServiceImpl implements MigrationService {

    @PersistenceContext(unitName = "from")
    private EntityManager sourceEntityManager;

    @PersistenceContext(unitName = "to")
    private EntityManager destinationEntityManager;

    @Transactional(value = "toTransactionManager")
    public void migrate() {
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
    }
}
