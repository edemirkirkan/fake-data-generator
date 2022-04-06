package com.cs4221.database;

import java.io.IOException;
import java.util.ArrayList;

import com.cs4221.mockaroo.*;

public class DataCount {
    private static int rowCount = 0;

    public DataCount(int rowCount, ArrayList<Table> tables, Database db) throws IOException, NoSuchMethodException {
        this.rowCount = rowCount;
        Schema r = new Schema(rowCount, tables, db);

        // r.generateData();
    }

    public static int getRowCount() {
        return rowCount;
    }


}
