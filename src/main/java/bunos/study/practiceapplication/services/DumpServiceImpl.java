package bunos.study.practiceapplication.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
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

    @Value("${dump.files.path}")
    private String dumpFilesPath;

    @Value("${dump.pg_dump.program.path}")
    private String pgDumpPath;

    @Override
    public void createDump() throws IOException, InterruptedException {
        HashMap<String, String> urlData = getUrlData(dbUrl);

        Date currentDate = new Date();
        StringBuilder builder = new StringBuilder();
        builder.append(currentDate.toString().replace(' ', '_').replace(':', '-'));
        String dumpFileName = builder.toString();

        String command = pgDumpPath + "pg_dump.exe --dbname=postgresql://" + dbUsername + ":" + dbPassword + "@" + urlData.get("host") + ":" + urlData.get("port") + "/" + urlData.get("database") + " -F c > " + dumpFilesPath + "/" + dumpFileName + ".sql";

        String cmdFilePath = dumpFilesPath + "/" + dumpFileName + ".cmd";
        createCommandFile(cmdFilePath, command);

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
    @Transactional
    public void restore(String dumpName, String args) throws IOException, InterruptedException {
        HashMap<String, String> urlData = getUrlData(dbUrl);

        StringBuilder command = new StringBuilder();

        command.append(pgDumpPath + "pg_restore.exe --dbname=postgresql://" + dbUsername + ":" + dbPassword + "@" + urlData.get("host") + ":" + urlData.get("port") + "/" + urlData.get("database") + " ");
        command.append(args);
        command.append(" " + dumpFilesPath + "/" + dumpName + ".sql");

        String cmdFilePath = dumpFilesPath + "/" + dumpName + ".cmd";
        createCommandFile(cmdFilePath, command.toString());

        System.out.println(command);

        sourceEntityManager.createNativeQuery("DROP TABLE IF EXISTS t_test_entity").executeUpdate();
        sourceEntityManager.createNativeQuery("DROP SEQUENCE IF EXISTS t_test_entity_seq").executeUpdate();
        sourceEntityManager.flush();

        ProcessBuilder processBuilder = new ProcessBuilder(cmdFilePath);
        Process process = processBuilder.start();
        int exitCode = process.waitFor();

        if (exitCode == 0) {
            log.info("A database restored to {} successfully.", dumpName);
        } else {
            log.error("Error while restoring from a dump. Exit code: {}", exitCode);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.err.println(line);
                }
            }
        }

        if (!new java.io.File(cmdFilePath).delete()) {
            log.error("Error while deleting a command file.");
        }
    }

    private static void createCommandFile(String filePath, String command) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println(command);
        } catch (IOException e) {
            e.printStackTrace();
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
