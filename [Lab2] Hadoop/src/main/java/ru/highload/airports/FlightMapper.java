package ru.highload.airports;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FlightMapper extends Mapper<LongWritable, Text, JoinKeyWritableComparable, Text> {
    private static final float EPS = 1e-9f;
    private static final int DEST_AIRPORT_INDEX = 14;
    private static final int DELAY_INDEX = 18;
    private static final int CANCELLED_INDEX = 19;

    private static final String DEST_AIRPORT_COLUMN_NAME = "DEST_AIRPORT_ID";
    private static final String DELAY_COLUMN_NAME = "ARR_DELAY_NEW";

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] cols = CSVParser.makeCols(value.toString());

        String destAirport = cols[DEST_AIRPORT_INDEX];
        String delay = cols[DELAY_INDEX];
        String cancelled = cols[CANCELLED_INDEX];

        if (destAirport.equals(DEST_AIRPORT_COLUMN_NAME)) {
            return;
        }
        if (delay.equals(DELAY_COLUMN_NAME) || delay.equals("") || Math.abs(Float.parseFloat(cancelled) - 1.f) < EPS) {
            return;
        }

        int destAirportID = Integer.parseInt(destAirport);
        float delayTime = Float.parseFloat(delay);

        if (Math.abs(delayTime - 0.f) < EPS)   // delayTime == 0
            return;

        context.write(new JoinKeyWritableComparable(destAirportID, 1),
                new Text(String.valueOf(delayTime)));
    }
}
