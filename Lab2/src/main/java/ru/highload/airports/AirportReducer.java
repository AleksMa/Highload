package ru.highload.airports;


import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

public class AirportReducer extends Reducer<JoinKeyWritableComparable, Text, Text, Text> {

    @Override
    protected void reduce(JoinKeyWritableComparable key, Iterable<Text> values, Context context) throws
            IOException, InterruptedException {
        Iterator<Text> iter = values.iterator();

        float maxDelay = Float.MIN_VALUE, minDelay = Float.MAX_VALUE, delaySum = 0;
        int count = 0;

        Text airport = new Text(iter.next().toString() + ",");
        while (iter.hasNext()) {
            Text delayText = iter.next();
            float delay = Float.parseFloat(delayText.toString());

            if (delay > maxDelay)
                maxDelay = delay;
            if (delay < minDelay)
                minDelay = delay;

            delaySum += delay;
            count++;
        }

        if (count != 0) {
            context.write(airport,
                    new Text(String.join(", ",
                            String.valueOf(minDelay),
                            String.valueOf(maxDelay),
                            String.valueOf(delaySum / count))));
        }

    }
}