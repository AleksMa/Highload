package ru.highload.airports;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import scala.Tuple2;

import java.util.Map;

public class SparkAirportApp {
    private static final int AIRPORT_CODE_INDEX = 0;
    private static final int AIRPORT_DESCRIPTION_INDEX = 1;
    private static final int AIRPORT_EXTRA_DESCRIPTION_INDEX = 2;

    private static final int FLIGHT_ORIGIN_AIRPORT_INDEX = 11;
    private static final int FLIGHT_DEST_AIRPORT_INDEX = 14;
    private static final int FLIGHT_DELAY_INDEX = 18;
    private static final int FLIGHT_CANCELLED_INDEX = 19;

    private static final String AIRPORT_CODE_COLUMN_NAME = "Code";
    private static final String FLIGHT_DEST_AIRPORT_COLUMN_NAME = "DEST_AIRPORT_ID";

    private static final String FLIGHT_INPUT_FILE = "664600583_T_ONTIME_sample.csv";
    private static final String AIRPORT_INPUT_FILE = "L_AIRPORT_ID.csv";

    private static boolean isNotColumnName(String[] cols, int columnIndex, String columnName) {
        return !cols[columnIndex].equals(columnName);
    }

    private static String getFullAirportName(String[] cols) {
        if (cols.length >= 3) {
            return cols[AIRPORT_DESCRIPTION_INDEX] + cols[AIRPORT_EXTRA_DESCRIPTION_INDEX];
        } else {
            return cols[AIRPORT_DESCRIPTION_INDEX];
        }
    }

    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setAppName("Lab3");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> flightsLines = sc.textFile(FLIGHT_INPUT_FILE);
        JavaRDD<String[]> flightsLinesParsed = flightsLines
                .map(CSVParser::makeCols)
                .filter(cols -> isNotColumnName(cols, FLIGHT_DEST_AIRPORT_INDEX, FLIGHT_DEST_AIRPORT_COLUMN_NAME));

        JavaPairRDD<Tuple2<String, String>, FlightStatsValue> flightStatPairs = flightsLinesParsed
                .mapToPair(
                        cols -> new Tuple2<>(
                                new Tuple2<>(cols[FLIGHT_ORIGIN_AIRPORT_INDEX], cols[FLIGHT_DEST_AIRPORT_INDEX]),
                                new FlightStatsValue(cols[FLIGHT_DELAY_INDEX], cols[FLIGHT_CANCELLED_INDEX])
                        )
                );

        JavaPairRDD<Tuple2<String, String>, FlightStatsValue> flightsStatPairsSummarized = flightStatPairs
                .reduceByKey(FlightStatsValue::add);


        JavaRDD<String> airportsLines = sc.textFile(AIRPORT_INPUT_FILE);
        JavaRDD<String[]> airportsLinesParsed = airportsLines
                .map(CSVParser::makeCols)
                .filter(cols -> isNotColumnName(cols, AIRPORT_CODE_INDEX, AIRPORT_CODE_COLUMN_NAME));

        JavaPairRDD<String, String> airportsPairs = airportsLinesParsed.mapToPair(
                cols -> new Tuple2<>(cols[AIRPORT_CODE_INDEX],
                        getFullAirportName(cols))
        );

        Map<String, String> airportsMap = airportsPairs.collectAsMap();
        final Broadcast<Map<String, String>> airportsBroadcast =
                sc.broadcast(airportsMap);

        JavaRDD<String> statsLines = flightsStatPairsSummarized.map(
                pair -> airportsBroadcast.value().get(pair._1._1) + ", " +
                        airportsBroadcast.value().get(pair._1._2) + ", " +
                        pair._2.toString()
        );

        statsLines.saveAsTextFile(args[0]);
    }
}