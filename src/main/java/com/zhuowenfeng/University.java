package com.zhuowenfeng;

import java.io.*;
import java.util.List;

/**
 * A singleton class that provides the function to get most similar university name from a property file when
 * given a actual university name. If there is no similar one, it will return null.
 *
 * @author Wenfeng Zhuo (wz2366@columbia.edu)
 */
public class University extends ColumnValueUniformer {

    private List<String> universityList;

    private University() {
        try {
            this.universityList = loadColumnFromConfigFile("/university-uniform.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getColumnValue() {
        return this.universityList;
    }

    private static final University _instance = new University();

    public static String getMostSimilarUniversity(String university) {
        return _instance.getMostSimilarResult(university, 3);
    }

}
