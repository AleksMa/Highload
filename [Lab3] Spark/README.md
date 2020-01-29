```
start-dfs.sh
start-yarn.sh

hadoop jar ~/hadoop-2.9.2/share/hadoop/mapreduce/hadoop-mapreduce-examples-2.9.2.jar pi 2 5

mvn package
spark-submit --class ru.highload.airports.SparkAirportApp --master yarn-client --num-executors 3 ./target/Lab3-1.0-SNAPSHOT.jar 664600583_T_ONTIME_sample.csv L_AIRPORT_ID.csv output3.0 

hadoop fs -copyToLocal output
```