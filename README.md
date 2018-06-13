# ActiveMQ, Spring, Object Example

Simple Project to show how to queue Objects in ActiveMQ (no-in-memory) with Spring.

## Getting Started

Start ActiveMQ. The fastest way is probably as docker container:
```
docker run --name='activemq' -it --rm -P \ webcenter/activemq:latest
```
You can find the ActiveMQ-Dashboard under http://localhost:8161/admin with username and password admin.
For production usage see https://hub.docker.com/r/webcenter/activemq/#quick-start to configure ActiveMQ.

Run the Producer- and the Consumer-Project.

Go to http://localhost:8081/produce/YourName.
In the Consumer-Project Console you will see the received data.
