package bunos.study.practiceapplication.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DumpServiceImpl implements DumpService {

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${sources.from-datasource.url}")
    private String dbUrl;

    @Value("${dump.file.path}")
    private String dumpFilePath;

    @Value("${dump.pg_dump.program.path}")
    private String pgDumpPath;

    @Override
    public void createDump() throws IOException, InterruptedException {
        Pattern pattern = Pattern.compile("jdbc:postgresql://([^:/]+):([0-9]+)/([^?]+)");
        Matcher matcher = pattern.matcher(dbUrl);
        matcher.find();

        String host = matcher.group(1);
        String port = matcher.group(2);
        String database = matcher.group(3);

        Date currentDate = new Date();
        StringBuilder builder = new StringBuilder();
        builder.append(currentDate.toString().replace(' ', '_').replace(':', '-'));
        String dumpFileName = builder.toString();

        String command =  pgDumpPath + " --dbname=postgresql://" + dbUsername + ":" + dbPassword + "@" + host + ":" + port + "/" + database + " > " + dumpFilePath + "/" + dumpFileName + ".sql";

        String cmdFilePath = dumpFilePath + "/" + dumpFileName + ".cmd";
        createCommandFile(cmdFilePath, command);

        ProcessBuilder processBuilder = new ProcessBuilder(cmdFilePath);
        Process process = processBuilder.start();
        int exitCode = process.waitFor();

        if (exitCode == 0) {
            System.out.println("Dump created successfully.");
        } else {
            System.err.println("Error creating dump. Exit code: " + exitCode);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.err.println(line);
                }
            }
        }

        if (!new java.io.File(cmdFilePath).delete()) {
            System.err.println("Error deleting command file.");
        }
    }

    private static void createCommandFile(String filePath, String command) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
