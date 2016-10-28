package com.zhuowenfeng;

import java.io.IOException;
import java.util.List;

/**
 * A singleton class that provides the function to get most similar city name from a property file when
 * given a actual name. If there is no similar city, it will return null.
 *
 * @author Wenfeng Zhuo (wz2366@columbia.edu)
 */
public class City extends ColumnValueUniformer {

    List<String> cityList;

    private City() {
        try {
            cityList = loadColumnFromConfigFile("/city-uniform.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getColumnValue() {
        return cityList;
    }

    private static final City _instance = new City();

    public static String getMostSimilarCity(String city) {
        return _instance.getMostSimilarResult(city, 2);
    }


}
