package bunos.study.practiceapplication.services;

import bunos.study.practiceapplication.models.migration.TestEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BackupServiceImpl implements BackupService {
    @PersistenceContext(unitName = "to")
    private EntityManager historicEntityManager;

    @PersistenceContext(unitName = "from")
    private EntityManager baseEntityManager;

    public List<TestEntity> getAllFromHistoric() {
        return historicEntityManager.createQuery("SELECT e FROM TestEntity e", TestEntity.class).getResultList();
    }

    @Modifying
    @Transactional(transactionManager = "fromTransactionManager")
    public void dropBaseData() {
        baseEntityManager.createQuery("DELETE FROM TestEntity");
    }

    @Modifying
    @Transactional(transactionManager = "fromTransactionManager")
    public void saveAllToBase(List<TestEntity> data) {
        data.forEach(baseEntityManager::merge);
    }

    @Override
    @Transactional(transactionManager = "fromTransactionManager")
    @Modifying
    public void startBackup() {
        List<TestEntity> data = getAllFromHistoric();
        dropBaseData();
        saveAllToBase(data);
    }
}
