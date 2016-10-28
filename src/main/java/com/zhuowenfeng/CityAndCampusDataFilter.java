package com.zhuowenfeng;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * Filter City Crime Data and Campus Crime Data
 *
 * @author Wenfeng Zhuo (wz2366@columbia.edu)
 */
public class CityAndCampusDataFilter {

    /**
     * Filter the input file and write it filtered file into output directory.
     * @param input
     * @param outputDir
     * @param cityIdx - the city column position in input excel file. If no city column, make is as -1.
     * @param uniIdx - the university column position in input excel file. If no university column, leave it as -1.
     * @throws IOException
     * @throws InvalidFormatException
     */
    public void filter(String input, String outputDir, int cityIdx, int uniIdx) throws IOException,
                                                                                InvalidFormatException {
        File file = new File(input);
        String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));

        Workbook wbFiltered = new XSSFWorkbook();
        Sheet sheetFiltered = wbFiltered.createSheet();
        int rowId = 0;

        Workbook wbToFilter = new XSSFWorkbook(file);
        Iterator<Row> rowIterator = wbToFilter.getSheetAt(0).iterator();
        if (rowIterator.hasNext()) copyRow(sheetFiltered, rowIterator.next(), rowId++); // skip header

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            String originalCity = null;
            String originalUniveristy = null;
            if (cityIdx != -1) {
                Cell cityCell = row.getCell(cityIdx);
                originalCity = cityCell.getStringCellValue();
                String standardCityName = City.getMostSimilarCity(cityCell.getStringCellValue());
                if (standardCityName == null) {
                    System.out.println(cityCell + " is filtered out due to city missing");
                    continue;
                } else {
                    cityCell.setCellValue(standardCityName); // Convert the value to standard version
                }
            }
            if (uniIdx != -1) {
                Cell uniCell = row.getCell(uniIdx);
                originalUniveristy = uniCell.getStringCellValue();
                String standardUniName = University.getMostSimilarUniversity(uniCell.getStringCellValue());
                if (standardUniName == null) {
                    System.out.println(uniCell + " is filtered out due to university missing");
                    continue;
                } else {
                    uniCell.setCellValue(standardUniName); // Convert the value to standard version
                }
            }
            copyRow(sheetFiltered, row, rowId++);
            // Reverse the data back in the original file
            if (originalCity != null) row.getCell(cityIdx).setCellValue(originalCity);
            if (originalUniveristy != null) row.getCell(uniIdx).setCellValue(originalUniveristy);
        }

        File filteredFile = new File(outputDir + File.separator + fileName + "-filtered.xlsx");
        wbFiltered.write(new FileOutputStream(filteredFile));
        wbFiltered.close();
        wbToFilter.close();
    }

    /**
     * Copy a row from original excel file to a new file
     * @param container
     * @param from
     * @param i
     */
    public void copyRow(Sheet container, Row from, int i) {
        Row to = container.createRow(i);
        int c = 0;
        Iterator<Cell> iterator = from.iterator();
        while (iterator.hasNext()) {
            Cell oldCell = iterator.next();
            Cell newCell = to.createCell(c++);
            switch(oldCell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    newCell.setCellValue(oldCell.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    newCell.setCellValue(oldCell.getNumericCellValue());
                    break;
                default:
                    break;
            }
        }
    }

}
