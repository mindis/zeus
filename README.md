Setup:
`./bin/grid bootstrap`
This will setup kafka and zookeeper locally in `deploy` directory. Inspired from samza's bootstrap script.

Create kafka topic:
`./deploy/kafka/bin/kafka-topics.sh --create --topic event-counts --replication-factor 1 --zookeeper 127.0.0.1:2181 --partition 1`
Now, we are keeping things single here, in production env, we would have a replication factor of atleast 3 and a higher partion value

Run:
1. Start posting events to `/zeus/ingest` where event is:
`{ "name" : "Event Name", "timeInMs" : "1422179845" }`
or just run this class which will start pushing test data to kafka queue.
`RunKafkaDummyProducer.java`

3. Start the kafka consumer:
`RunKafkaConsumer.java`
Ideally this should be in a distributed environment or atleast running on a different process and not part of the app server. So this the high availability guarantees of the webserver can be achieved.

2. Start the web server: `mvn tomcat:run`

Home: http://127.0.0.1:8080/zeus/
See results:
http://127.0.0.1:8080/zeus/dashboard?eventName=Test%20event&bucket=hourly&value=19
where value is the hour.
http://127.0.0.1:8080/zeus/dashboard?eventName=Test%20event&bucket=daily

Stop everything:
1. kill `mvn tomcat:run`
2. `./bin/grid stop` - Stops kafka and zookeeper


Kafka helpers:

Delete topic:
`./deploy/kafka/bin/kafka-run-class.sh kafka.admin.DeleteTopicCommand --zookeeper 127.0.0.1:2181 --topic event-counts`

List topic:
`./deploy/kafka/bin/kafka-topics.sh --list --zookeeper 127.0.0.1:2181`

Test event ingestion:
`./deploy/kafka/bin/kafka-console-consumer.sh --zookeeper 127.0.0.1:2181 --topic event-counts --from-beginning`
