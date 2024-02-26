package bunos.study.practiceapplication.services;

import bunos.study.practiceapplication.models.databases.Database;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class BackupServiceImpl implements BackupService {
    @Value("${dump.pg_dump.program.path}")
    private String pgProgPath;

    private final DatabaseService databaseService;

    @Override
    public void createDump(String dumpFilesPath, String args, String databaseName) throws Exception {
        Database database = databaseService.getByName(databaseName);

        Date currentDate = new Date();
        StringBuilder builder = new StringBuilder();
        builder.append(currentDate.toString().replace(' ', '_').replace(':', '-'));
        String dumpFileName = builder.toString();

        StringBuilder command = new StringBuilder();
        command.append(pgProgPath + "pg_dump.exe ");
        command.append(args);
        command.append(" --dbname=postgresql://" + database.getUsername() + ":" + database.getPassword() + "@" + database.getHostname() + ":" + database.getPort() + "/" + database.getName() + " -F c > " + dumpFilesPath + "/" + dumpFileName + ".sql");

        String cmdFilePath = dumpFilesPath + "/" + dumpFileName + ".cmd";
        createCommandFile(cmdFilePath, command.toString());

        ProcessBuilder processBuilder = new ProcessBuilder(cmdFilePath);
        Process process = processBuilder.start();
        int exitCode = process.waitFor();

        if (exitCode == 0) {
            log.info("Dump created successfully.");
        } else {
            log.error("Error while creating a dump. Exit code: {}", exitCode);
            StringBuilder message = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.error(line);
                    message.append(line);
                }
            }

            throw new Exception(message.toString());
        }

        if (!new java.io.File(cmdFilePath).delete()) {
            log.error("Error while deleting a command file.");
        }
    }

    @Override
    public void restore(String dumpFilesPath, String dumpFileName, String args, String databaseName) throws Exception {
        Database database = databaseService.getByName(databaseName);

        String command = pgProgPath + "pg_restore.exe --dbname=postgresql://" + database.getUsername() + ":" + database.getPassword() + "@" + database.getHostname() + ":" + database.getPort() + "/" + database.getName() + " " + args + " " + dumpFilesPath + "/" + dumpFileName;
        System.out.println(command);

        String cmdFileName = dumpFileName.replace(".sql", "").concat(".cmd");
        String cmdFilePath = dumpFilesPath + "/" + cmdFileName;
        createCommandFile(cmdFilePath, command);

        ProcessBuilder processBuilder = new ProcessBuilder(cmdFilePath);
        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        deleteCommandFile(cmdFilePath);
        if (exitCode == 0) {
            log.info("A database restored to {} successfully.", dumpFileName);
        } else {
            log.error("Error while restoring from a dump. Exit code: {}", exitCode);
            StringBuilder message = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.error(line);
                    message.append(line);
                }
            }

            throw new Exception(message.toString());
        }
    }

    @Override
    public List<String> getBackups(String path) {
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
