#Overview
At a high level the project has 3 parts:

1. An "ingest" API which accepts JSON via HTTP POST and pushes it to kafka queue. Completely async and really fast.

2. A kafka consumer (a separate process) which starts polling the data from the other end of the queue and constructs daily metrics in mongodb.

3. A dashboard which reads directly from mongodb and shows metrics.


zeus is a simple event processing system which takes events via a HTTP endpoint (ingest) and pushes it to a kafka queue. A kafka consumer polls data from the other end of the queue and builds metrics collection in mongodb. This implementation will run on a single machine but nothing stops the user from deploying it in a distributed environment, the kafka broker will take care of message distribution for multiple kafka consumers.
The metrics can be viewed from a "dashboard". The API and dashboard are built using Spring 4.x controller, ideally in production the ingest API and dashboard should run on separate processes for high availability.


#Setup
1. Install mongodb and run on default settings (can use brew, macports for mac)

2. `./bin/grid bootstrap`  
This will setup kafka and zookeeper locally in `deploy` directory. grid is inspired from samza's bootstrap script.

3. Create kafka topic:  
`./deploy/kafka/bin/kafka-topics.sh --create --topic event-counts --replication-factor 1 --zookeeper 127.0.0.1:2181 --partition 1`  
Now, we are keeping things simple here, in production env, we would have a replication factor of atleast 3 and a higher partion value

#Run
1. Start the kafka consumer (by improting the project in eclipse and manually running it, an executable jar can also be created):  
`RunKafkaConsumer.java`
Ideally this should be in a distributed environment or atleast running on a different process and not part of the app server. So this the high availability guarantees of the webserver can be achieved.

2. Start the web server: `mvn tomcat:run`

3. Start sending data to http://127.0.0.1:8080/zeus/ingest  
Raw json: `{ "name":"TestEvent1", "timeInMs":"1622179845" }`  
Header: `Content-Type=application/json`  
OR just run this class which will start pushing test data to kafka queue
`RunKafkaDummyProducer.java`




#View Metrics
1. Home: http://127.0.0.1:8080/zeus/
2. Hourly metrics: http://127.0.0.1:8080/zeus/dashboard?eventName=TestEvent1&bucket=hourly&value=5
where "value" is the hour of the day.
3. Daily metrics: http://127.0.0.1:8080/zeus/dashboard?eventName=TestEvent1&bucket=daily


#Stop everything
1. Stop tomcat: `mvn tomcat:run`
2. `./bin/grid stop` - Stops kafka and zookeeper. Make sure zk is killed: `grep ps ax | grep java`
3. You can delete the `deploy` dir and any bootstrap by kafka and zk will be cleaned.


#Misc

1. Delete topic:
`./deploy/kafka/bin/kafka-run-class.sh kafka.admin.DeleteTopicCommand --zookeeper 127.0.0.1:2181 --topic event-counts`

2. List topic:
`./deploy/kafka/bin/kafka-topics.sh --list --zookeeper 127.0.0.1:2181`

3. Test event ingestion:
`./deploy/kafka/bin/kafka-console-consumer.sh --zookeeper 127.0.0.1:2181 --topic event-counts --from-beginning`
