package bunos.study.practiceapplication.services;

import java.util.List;

public interface BackupService {

    void createDump(String path, String args, String databaseName) throws Exception;

    void restore(String path, String dumpFileName, String args, String databaseName) throws Exception;

    List<String> getBackups(String path);
}
