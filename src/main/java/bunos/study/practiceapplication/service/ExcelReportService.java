package bunos.study.practiceapplication.service;

import bunos.study.practiceapplication.domain.model.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ExcelReportService {
    public Workbook export(List<Log> logs) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Logs");

        Row headerRow = sheet.createRow(0);
        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue("Log ID");

        headerCell = headerRow.createCell(1);
        headerCell.setCellValue("Time");

        headerCell = headerRow.createCell(2);
        headerCell.setCellValue("Data");

        headerCell = headerRow.createCell(3);
        headerCell.setCellValue("Journal ID");

        headerCell = headerRow.createCell(4);
        headerCell.setCellValue("Status");

        int rowNum = 1;
        for (Log log : logs) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(log.getLogId());
            row.createCell(1).setCellValue(log.getTime().toString());
            row.createCell(2).setCellValue(log.getData());
            row.createCell(3).setCellValue(log.getJournal().getJournalId());
            row.createCell(4).setCellValue(log.getStatus());
        }

        return workbook;
    }
}
