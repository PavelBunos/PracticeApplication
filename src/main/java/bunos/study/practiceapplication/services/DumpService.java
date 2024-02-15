package bunos.study.practiceapplication.services;

import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

public interface DumpService {

    void createDump(String path, String args) throws Exception;

    @Transactional
    void restore(String path, String dumpFilesName, String args) throws Exception;

    List<String> getBackupFilenames(String path);
}
