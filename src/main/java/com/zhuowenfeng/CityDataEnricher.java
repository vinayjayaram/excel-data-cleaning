package com.zhuowenfeng;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * Add more columns in the data file
 * @author Wenfeng Zhuo (wz2366@columbia.edu)
 */
public class CityDataEnricher {

    /**
     * Add state abbreviation column in City Crime Data.
     * @param input
     * @param outputDir
     * @param columnName
     * @throws IOException
     * @throws InvalidFormatException
     */
    public void addStateAbbr(String input, String outputDir, String columnName) throws IOException, InvalidFormatException {
        File file = new File(input);
        String fileName = file.getName();

        Workbook wb = new XSSFWorkbook(file);
        Iterator<Row> rowIterator = wb.getSheetAt(0).iterator();
        int idx = 0; // The newly added column position in the excel file.
        if (rowIterator.hasNext()) {
            Row header = rowIterator.next();
            idx = header.getLastCellNum() + 1;
            Cell cell = header.createCell(idx);
            cell.setCellValue(columnName);
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Cell state = row.getCell(0);
            Cell stateabbr = row.createCell(idx);
            stateabbr.setCellValue(State.getAbbreviation(state.getStringCellValue()));
        }

        File outputFile = new File(outputDir + File.separator + fileName);
        wb.write(new FileOutputStream(outputFile));
        wb.close();
    }


}
