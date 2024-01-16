package bunos.study.practiceapplication.services;

import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

public interface DumpService {
    void createDump() throws IOException, InterruptedException;

    @Transactional
    void restore(String dumpName, String args) throws IOException, InterruptedException;
}
