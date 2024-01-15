package bunos.study.practiceapplication.services;

import java.io.IOException;
import java.sql.SQLException;

public interface DumpService {
    void createDump() throws IOException, InterruptedException;
    void restore(String dumpName) throws IOException, InterruptedException;
}
