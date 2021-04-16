# NATS MESSAGING

This project is a quick and dirty test of NATS using a docker image. If you don't have access to a NATS server, run the Docker command below.

```
docker run -d --name nats-main -p 4222:4222 -p 6222:6222 -p 8222:8222 nats
```

Us Maven to run the test.

```
mvn clean test
```

The output will end after the 100th message:

```
Received event id {"id":99,"message":"fnrkEXHjrVmOJufzvYlZqfCGr"}
2021-04-16 12:01:40.540  INFO 70969 --- [s-subscriptions] c.example.demo.services.ConsumerService  : subscribeAsync() <--- {Subject=TEST_TOPIC;Reply=null;Payload=<{"eventId":"ac3d74e8-4d89-490b-8181 more bytes>}
Received event id {"id":100,"message":"xmyUJievvMTQMOiJPKyCJJRzm"}
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.607 s - in com.example.demo.DemoApplicationTests

```
