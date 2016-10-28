package com.zhuowenfeng;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

/**
 * The class that helps to uniform values in excel files by using the values in config files
 *
 * @author Wenfeng Zhuo (wz2366@columbia.edu)
 */
public abstract class ColumnValueUniformer {

    /**
     * Load data from excel file to create on-to-one mapping table.
     * @param input
     * @param key
     * @param value
     * @return
     * @throws IOException
     */
    public Map<String, String> loadMappingTableFromExcel(String input, int key, int value) throws IOException {
        input = ColumnValueUniformer.class.getResource(input).getFile();
        Workbook wb = new XSSFWorkbook(new FileInputStream(new File(input)));
        Sheet sheet = wb.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();
        if(rows.hasNext()) rows.next(); // skip headers
        Map<String, String> map = new HashMap<>();
        while(rows.hasNext()) {
            Row cur = rows.next();
            map.put(cur.getCell(key).getStringCellValue().toLowerCase(), cur.getCell(value).getStringCellValue());
        }
        return map;
    }

    /**
     * Load data from a single column in excel file
     * @param input
     * @param column
     * @return
     * @throws IOException
     */
    public List<String> loadSingleColumnFromExcel(String input, int column) throws IOException {
        input = ColumnValueUniformer.class.getResource(input).getFile();
        Workbook wb = new XSSFWorkbook(new FileInputStream(new File(input)));
        Sheet sheet = wb.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();
        if(rows.hasNext()) rows.next(); // skip headers
        Set<String> list = new HashSet<>();
        while(rows.hasNext()) {
            Row cur = rows.next();
            Cell cell = cur.getCell(column); // get column value
            if (StringUtils.isNotEmpty(cell.getStringCellValue())) {
                list.add(cell.getStringCellValue());
            }
        }
        return new ArrayList<>(list);
    }

    /**
     * Load property file that will be used to uniform the values in given data file
     * @param input
     * @return
     * @throws IOException
     */
    public List<String> loadColumnFromConfigFile(String input) throws IOException {
        input = ColumnValueUniformer.class.getResource(input).getFile();
        BufferedReader reader = new BufferedReader(new FileReader(input));
        List<String> list = new ArrayList<>();
        String line = null;
        while((line = reader.readLine()) != null) {
            list.add(line);
        }
        return list;
    }

    /**
     * Get a most similar result compared to data in the property file. The underline principle is
     * using Levenshtein Distance also referred as Edit Distance. If the distance is small, they are
     * more similar to each other.
     *
     * @param query
     * @param factor - the factor means how many different character is tolerant in the query string
     * @return
     */
    public String getMostSimilarResult(String query, int factor) {
        if (StringUtils.isEmpty(query)) return null;
        int curScore = Integer.MAX_VALUE;
        List<String> res = new ArrayList<>();
        for (String uni : this.getColumnValue()) {
            int distance = StringUtils.getLevenshteinDistance(uni.toLowerCase(), query.toLowerCase());
            int commonPrefix = StringUtils.length(StringUtils.getCommonPrefix(uni.toLowerCase(), query.toLowerCase()));
            if (distance < factor && commonPrefix > query.length() - factor) {
                if (distance == curScore) {
                    res.add(uni);
                } else if (distance < curScore) {
                    res.clear();
                    res.add(uni);
                    curScore = distance;
                }
            }
        }
        for (String sim : res) {
            if (sim.toLowerCase().equals(query.toLowerCase())) {
                return sim;
            }
        }
        return res.size() == 0 ? null : res.get(0);
    }

    public abstract List<String> getColumnValue();

}
