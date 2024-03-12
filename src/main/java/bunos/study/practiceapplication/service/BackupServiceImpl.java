// todo - to refactor code

package bunos.study.practiceapplication.service;

import bunos.study.practiceapplication.domain.dto.Response;
import bunos.study.practiceapplication.domain.model.Database;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Value("${dump.pg_dump.program.path}") // todo - вынести в админ-панель
    private String pgProgramPath;

    private final DatabaseService databaseService;

    @Override
    public ResponseEntity<Response> createBackup(String dumpFilesPath, String args, String databaseName) {
        Database database = (Database) databaseService.getByName(databaseName).getBody().getData();

        if (!database.getConnectionStatus()) {
            return new ResponseEntity<>(Response.builder()
                    .data("Не удалось подключиться к БД")
                    .build(),
                    HttpStatus.BAD_REQUEST);
        }

        Date currentDate = new Date();
        StringBuilder fileNameBuilder = new StringBuilder();
        fileNameBuilder.append(currentDate);
        fileNameBuilder.append("_");
        fileNameBuilder.append(database.getName());
        String backupFileName = fileNameBuilder.toString().replace(' ', '_').replace(':', '-');

        StringBuilder command = new StringBuilder();
        command.append(pgProgramPath);
        command.append("pg_dump.exe ");
        command.append(args);
        command.append(" --dbname=postgresql://");
        command.append(database.getUser());
        command.append(":");
        command.append(database.getPassword());
        command.append("@");
        command.append(database.getHostname());
        command.append(":");
        command.append(database.getPort());
        command.append("/");
        command.append(database.getName());
        command.append(" ");
        command.append(args);
        command.append(" > ");
        command.append(dumpFilesPath);
        command.append("/");
        command.append(backupFileName);
        command.append(".sql");

        File dir = new File(dumpFilesPath);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                return new ResponseEntity<>(Response.builder()
                        .data("Не удалось создать директорию " + dumpFilesPath)
                        .build(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        String cmdFilePath = dumpFilesPath + "/" + backupFileName + ".cmd";
        createCommandFile(cmdFilePath, command.toString());

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(cmdFilePath);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                log.info("Backup created successfully.");
            } else {
                log.error("Error while creating backup. Exit code: {}", exitCode);

                return new ResponseEntity<>(Response.builder()
                        .data("Произошла ошибка: " + getProcessError(process))
                        .build(),
                        HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(Response.builder()
                    .data("Произошла ошибка: " + ex.getMessage())
                    .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            deleteCommandFile(cmdFilePath);
        }

        return new ResponseEntity<>(Response.builder()
                .data("Резервная копия была успешно создана!")
                .build(),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> restore(String dumpFilesPath, String dumpFileName, String args, String databaseName) {
        Database database = (Database) databaseService.getByName(databaseName).getBody().getData();

        if (!database.getConnectionStatus()) {
            return new ResponseEntity<>(Response.builder()
                    .data("Не удалось подключиться к БД")
                    .build(),
                    HttpStatus.BAD_REQUEST);
        }

        String command = pgProgramPath + "pg_restore.exe --dbname=postgresql://" + database.getUser() + ":" + database.getPassword() + "@" + database.getHostname() + ":" + database.getPort() + "/" + database.getName() + " " + args + " " + dumpFilesPath + "/" + dumpFileName;

        String cmdFileName = dumpFileName.replace(".sql", "").concat(".cmd");
        String cmdFilePath = dumpFilesPath + "/" + cmdFileName;

        createCommandFile(cmdFilePath, command);

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(cmdFilePath);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                log.info("A database restored to {} successfully.", dumpFileName);
            } else {
                log.error("Error while restoring from a dump. Exit code: {}", exitCode);

                return new ResponseEntity<>(Response.builder()
                        .data("Произошла ошибка: " + getProcessError(process))
                        .build(),
                        HttpStatus.BAD_REQUEST);
            }

        } catch (Exception ex) {
            return new ResponseEntity<>(Response.builder()
                    .data("Произошла ошибка: " + ex.getMessage())
                    .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            deleteCommandFile(cmdFilePath);
        }

        return new ResponseEntity<>(Response.builder()
                .data("БД успешно восстановлена!")
                .build(),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> getBackups(String path) {
        List<String> files = new ArrayList<>();

        File dir = new File(path);

        if (!dir.exists()) {
            return new ResponseEntity<>(Response.builder()
                    .data("Указанная директория не существует!")
                    .build(),
                    HttpStatus.BAD_REQUEST);
        }

        if (dir.isDirectory()) {
            try {
                for (File f : dir.listFiles()) {
                    String fileName = f.getName();
                    if (fileName.matches("^.+\\.sql$")) {
                        files.add(fileName);
                    }
                }
            } catch (NullPointerException ex) {
                log.error("Error while file searching: " + ex.getMessage());
                return new ResponseEntity<>(Response.builder()
                        .data("Произошла ошибка: " + ex.getMessage())
                        .build(),
                        HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<>(Response.builder()
                .data(files)
                .build(),
                HttpStatus.OK);
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
        HashMap<String, String> urlData = new HashMap<>();

        if (matcher.find()) {
            urlData.put("host", matcher.group(1));
            urlData.put("port", matcher.group(2));
            urlData.put("database", matcher.group(3));
        }

        return urlData;
    }

    private String getProcessError(Process process) {
        StringBuilder message = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                log.error(line);
                message.append(line);
            }
        } catch (Exception ex) {
            message.append("Unable to get error message");
            log.error(message.toString());
        }

        return message.toString();
    }
}
