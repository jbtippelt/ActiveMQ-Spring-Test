# ActiveMQ - Spring - Request - Reply

Simple test project to show how to queue objects in ActiveMQ (no-in-memory) with Spring. Also implemented a request-reply-functionality with temp-queues.

## Getting Started

* Start ActiveMQ. The fastest way is probably as docker container:
```
docker run --name='activemq' -it --rm -P \ webcenter/activemq:latest
```
You can find the ActiveMQ-Dashboard under http://localhost:8161/admin with username and password admin.
For production usage see https://hub.docker.com/r/webcenter/activemq/#quick-start to configure ActiveMQ.

* Run the Producer- and the Consumer-Project.

* Add users with a POST-request to http://localhost:8081/add like this:
```
curl -d '{"name":"Jacob","age":28}' -H "Content-Type: application/json" -X POST http://localhost:8081/add
```
The reply should look like this:
> Produced successfully: {"name":"Jacob","age":28}

* Request an user with his index in a GET-request curl -X GET http://localhost:8081/get/{index} like this:
```
curl -X GET http://localhost:8081/get/0
```
The reply should look like this:
> Received successfully: {"name":"Jacob","age":28}


* Check the concoles of the projects for details of received and replyed messages.
