package com.zhuowenfeng;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by lzhuo on 10/27/16.
 */
public class State extends ColumnValueUniformer {

    private Map<String, String> map;

    private State() {
        try {
            map = this.loadMappingTableFromExcel("/state_table.xlsx", 1, 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final State _instance = new State();

    public static String getAbbreviation(String state) {
        return _instance.map.get(state.toLowerCase());
    }

    @Override
    public List<String> getColumnValue() {
        return null;
    }

}
