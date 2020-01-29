package ru.highload.airports;

public class CSVParser {
    private static final String DELIMITER = ",";
    private static final String REMOVABLE_RE = "\"";

    public static String[] makeCols(String line){
        String[] cols = line.split(DELIMITER);
        for (int i = 0; i < cols.length; i++){
            cols[i] = cols[i].replaceAll(REMOVABLE_RE, "");
        }
        return cols;
    }
}
