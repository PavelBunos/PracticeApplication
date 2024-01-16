package bunos.study.practiceapplication.services;

import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

public interface DumpService {

    void createDump(String path, String args) throws IOException, InterruptedException;

    @Transactional
    void restore(String path, String dumpFilesName, String args) throws IOException, InterruptedException;

    List<String> getBackupFilenames(String path);
}
