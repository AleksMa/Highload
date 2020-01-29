## Zookeeper

Start:
```
mvn clean package
mvn exec:java -Dexec.mainCls="ru.highload.anonymizer.server.AnonymizerApp" -Dexec.args="127.0.0.1 8082"
```

Request:  
`http://127.0.0.1:8081/?url=http://rambler.ru&count=10`