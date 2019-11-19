package ru.highload.airports;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class AirportMapper extends Mapper<LongWritable, Text, JoinKeyWritableComparable, Text> {
    private static final String AIRPORT_ID_COLUMN_NAME = "Code";
    private static final String AIRPORT_NAME_COLUMN_NAME = "Description";


    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        String[] cols = CSVParser.makeCols(value.toString());
        if (cols.length != 2 && cols.length != 3) {
            return;
        }
        if (cols[0].equals(AIRPORT_ID_COLUMN_NAME)) {
            return;
        }
        if (cols[1].equals(AIRPORT_NAME_COLUMN_NAME)) {
            return;
        }

        int airportID = Integer.parseInt(cols[0]);
        String airportName = cols[1];

        if (cols.length > 2) {
            airportName += cols[2];
        }

        context.write(new JoinKeyWritableComparable(airportID, 0),
                      new Text(airportName));

    }
}
