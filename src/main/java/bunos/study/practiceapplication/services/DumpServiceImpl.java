package bunos.study.practiceapplication.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class DumpServiceImpl implements DumpService {

    @PersistenceContext(unitName = "from")
    private EntityManager sourceEntityManager;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${sources.from-datasource.url}")
    private String dbUrl;

    @Value("${dump.pg_dump.program.path}")
    private String pgProgPath;

    @Override
    public void createDump(String dumpFilesPath, String args) throws IOException, InterruptedException {
        HashMap<String, String> urlData = getUrlData(dbUrl);

        Date currentDate = new Date();
        StringBuilder builder = new StringBuilder();
        builder.append(currentDate.toString().replace(' ', '_').replace(':', '-'));
        String dumpFileName = builder.toString();

        StringBuilder command = new StringBuilder();
        command.append(pgProgPath + "pg_dump.exe ");
        command.append(args);
        command.append(" --dbname=postgresql://" + dbUsername + ":" + dbPassword + "@" + urlData.get("host") + ":" + urlData.get("port") + "/" + urlData.get("database") + " -F c > " + dumpFilesPath + "/" + dumpFileName + ".sql");

        String cmdFilePath = dumpFilesPath + "/" + dumpFileName + ".cmd";
        createCommandFile(cmdFilePath, command.toString());

        ProcessBuilder processBuilder = new ProcessBuilder(cmdFilePath);
        Process process = processBuilder.start();
        int exitCode = process.waitFor();

        if (exitCode == 0) {
            log.info("Dump created successfully.");
        } else {
            log.error("Error while creating a dump. Exit code: {}", exitCode);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.error(line);
                }
            }
        }

        if (!new java.io.File(cmdFilePath).delete()) {
            log.error("Error while deleting a command file.");
        }
    }

    @Override
    @Transactional(value = "fromTransactionManager")
    public void restore(String dumpFilesPath, String dumpFileName, String args) throws IOException, InterruptedException {
        HashMap<String, String> urlData = getUrlData(dbUrl);

        String command = pgProgPath + "pg_restore.exe --dbname=postgresql://" + dbUsername + ":" + dbPassword + "@" + urlData.get("host") + ":" + urlData.get("port") + "/" + urlData.get("database") + " " + args + " " + dumpFilesPath + "/" + dumpFileName;
        System.out.println(command);

        String cmdFileName = dumpFileName.replace(".sql", "").concat(".cmd");
        String cmdFilePath = dumpFilesPath + "/" + cmdFileName;
        createCommandFile(cmdFilePath, command);

        ProcessBuilder processBuilder = new ProcessBuilder(cmdFilePath);
        Process process = processBuilder.start();
        int exitCode = process.waitFor();

        if (exitCode == 0) {
            log.info("A database restored to {} successfully.", dumpFileName);
        } else {
            log.error("Error while restoring from a dump. Exit code: {}", exitCode);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.err.println(line);
                }
            }
        }

        log.info("Restore procedure was finished");
        deleteCommandFile(cmdFilePath);
    }

    @Override
    public List<String> getBackupFilenames(String path) {
        List<String> files = new ArrayList<>();

        File dir = new File(path);
        if (dir.isDirectory()) {
            for (File f : dir.listFiles()) {
                String fileName = f.getName();
                if (fileName.matches("^.+\\.sql$")) {
                    files.add(fileName);
                }
            }
        }

        return files;
    }

    private static void createCommandFile(String filePath, String command) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void deleteCommandFile(String filePath) {
        if (!new java.io.File(filePath).delete()) {
            log.error("Error while deleting a command file.");
        }
    }

    private static HashMap<String, String> getUrlData(String url) {
        Pattern pattern = Pattern.compile("jdbc:postgresql://([^:/]+):([0-9]+)/([^?]+)");
        Matcher matcher = pattern.matcher(url);
        matcher.find();

        HashMap<String, String> urlData = new HashMap<>();
        urlData.put("host", matcher.group(1));
        urlData.put("port", matcher.group(2));
        urlData.put("database", matcher.group(3));

        return urlData;
    }
}
