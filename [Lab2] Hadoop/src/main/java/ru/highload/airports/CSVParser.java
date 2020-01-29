package ru.highload.airports;

public class CSVParser {
    public static String[] makeCols(String line){
        String[] cols = line.split(",");
        for (int i = 0; i < cols.length; i++){
            cols[i] = cols[i].replaceAll("\"", "");
        }
        return cols;
    }
}
