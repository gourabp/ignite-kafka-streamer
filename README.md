# ignite-kafka-streamer
Demo project for Kafka Ignite streamer, Kafka as source and Ignite cache as sink

Step-1) Run both Zookeeper and Kafka using the docker-compose file provided in the root of the project. (go to that directory and run following command)

ignite-kafka-streamer-demo % docker compose up                                                                                                                   

verify both verify both Zookeeper and Kafka running by executing following command.

ignite-kafka-streamer-demo % docker compose ps                                                                                                                                                                             

NAME                                     COMMAND                  SERVICE             STATUS              PORTS

ignite-kafka-streamer-demo-kafka-1       "/opt/bitnami/script…"   kafka               running             0.0.0.0:9092->9092/tcp

ignite-kafka-streamer-demo-zookeeper-1   "/opt/bitnami/script…"   zookeeper           running             0.0.0.0:2181->2181/tcp



Step-2) Verify the topic exists by running following docker command.


ignite-kafka-streamer-demo % docker exec -it ignite-kafka-streamer-demo-kafka-1  kafka-topics.sh --describe --topic test-topic --bootstrap-server localhost:9092

Topic: test-topic	TopicId: jGDuAm3JS5es7l-tO1mwzQ	PartitionCount: 4	ReplicationFactor: 1	Configs: segment.bytes=1073741824

	Topic: test-topic	Partition: 0	Leader: 1	Replicas: 1	Isr: 1

	Topic: test-topic	Partition: 1	Leader: 1	Replicas: 1	Isr: 1

	Topic: test-topic	Partition: 2	Leader: 1	Replicas: 1	Isr: 1

	Topic: test-topic	Partition: 3	Leader: 1	Replicas: 1	Isr: 1



Step-3)  Bring up Ignite Server with the config provided under the cluster_config directory: 

[23:05:35] Ignite node started OK (id=6d27bba6)

[23:05:35] Topology snapshot [ver=1, locNode=6d27bba6, servers=1, clients=0, state=ACTIVE, CPUs=10, offheap=6.4GB, heap=7.1GB]

[23:05:35]   ^-- Baseline [id=0, size=1, online=1, offline=0]



Step-4) Verify the partitioned cache got created from visor 


visor> cache

Time of the snapshot: 2022-01-31 23:22:08

+==============================================================================================================================================================+

|     Name(@)      |    Mode     | Nodes | Total entries (Heap / Off-heap) | Primary entries (Heap / Off-heap) |   Hits    |  Misses   |   Reads   |  Writes   |

+==============================================================================================================================================================+

| mydemocache(@c0) | PARTITIONED | 2     | 0 (0 / 0)                       | min: 0 (0 / 0)                    | min: 0    | min: 0    | min: 0    | min: 0    |

|                  |             |       |                                 | avg: 0.00 (0.00 / 0.00)           | avg: 0.00 | avg: 0.00 | avg: 0.00 | avg: 0.00 |

|                  |             |       |                                 | max: 0 (0 / 0)                    | max: 0    | max: 0    | max: 0    | max: 0    |

+--------------------------------------------------------------------------------------------------------------------------------------------------------------+



Step-5) Make sure the Ignite Kafka Server application started which joined as a member

Run the MyKafkaStreamer.java


23:06:02] Topology snapshot [ver=2, locNode=60fed73e, servers=2, clients=0, state=ACTIVE, CPUs=10, offheap=13.0GB, heap=14.0GB]

[23:06:02]   ^-- Baseline [id=0, size=2, online=2, offline=0]
[main] INFO org.apache.kafka.clients.consumer.ConsumerConfig - ConsumerConfig values:
allow.auto.create.topics = true
auto.commit.interval.ms = 5000

Ignite server recognized additional node as well (one started in step1)

[23:05:35] Ignite node started OK (id=6d27bba6)

[23:05:35] Topology snapshot [ver=1, locNode=6d27bba6, servers=1, clients=0, state=ACTIVE, CPUs=10, offheap=6.4GB, heap=7.1GB]

[23:05:35]   ^-- Baseline [id=0, size=1, online=1, offline=0]

....

....

[23:15:40] Joining node doesn't have stored group keys [node=5e52b3ae-91f4-40aa-a0b8-361a8d32a5fd]

[23:15:41] Topology snapshot [ver=4, locNode=6d27bba6, servers=2, clients=0, state=ACTIVE, CPUs=20, offheap=13.0GB, heap=14.0GB]

[23:15:41]   ^-- Baseline [id=0, size=2, online=2, offline=0]


You should see Following the Kafka consumer config in the startup log of MyKafkaStreamer.
.....
[main] INFO org.apache.kafka.clients.consumer.ConsumerConfig - ConsumerConfig values:
allow.auto.create.topics = true
auto.commit.interval.ms = 5000
auto.offset.reset = earliest
bootstrap.servers = [localhost:9092]
check.crcs = true
client.dns.lookup = use_all_dns_ips
client.id = consumer-gpdev1-1
client.rack =
connections.max.idle.ms = 540000
default.api.timeout.ms = 60000
enable.auto.commit = true
exclude.internal.topics = true
fetch.max.bytes = 52428800
fetch.max.wait.ms = 500
fetch.min.bytes = 1
group.id = gpdev1
group.instance.id = null
heartbeat.interval.ms = 3000
interceptor.classes = []
internal.leave.group.on.close = true
internal.throw.on.fetch.stable.offset.unsupported = false
isolation.level = read_uncommitted
key.deserializer = class org.apache.kafka.common.serialization.StringDeserializer
max.partition.fetch.bytes = 1048576
max.poll.interval.ms = 300000
max.poll.records = 500
metadata.max.age.ms = 300000
metric.reporters = []
metrics.num.samples = 2
metrics.recording.level = INFO
metrics.sample.window.ms = 30000
partition.assignment.strategy = [class org.apache.kafka.clients.consumer.RangeAssignor, class org.apache.kafka.clients.consumer.CooperativeStickyAssignor]
receive.buffer.bytes = 65536
reconnect.backoff.max.ms = 1000
reconnect.backoff.ms = 50
request.timeout.ms = 30000
retry.backoff.ms = 100
sasl.client.callback.handler.class = null
sasl.jaas.config = null
sasl.kerberos.kinit.cmd = /usr/bin/kinit
sasl.kerberos.min.time.before.relogin = 60000
sasl.kerberos.service.name = null
sasl.kerberos.ticket.renew.jitter = 0.05
sasl.kerberos.ticket.renew.window.factor = 0.8
sasl.login.callback.handler.class = null
sasl.login.class = null
sasl.login.connect.timeout.ms = null
sasl.login.read.timeout.ms = null
sasl.login.refresh.buffer.seconds = 300
sasl.login.refresh.min.period.seconds = 60
sasl.login.refresh.window.factor = 0.8
sasl.login.refresh.window.jitter = 0.05
sasl.login.retry.backoff.max.ms = 10000
sasl.login.retry.backoff.ms = 100
sasl.mechanism = GSSAPI
sasl.oauthbearer.clock.skew.seconds = 30
sasl.oauthbearer.expected.audience = null
sasl.oauthbearer.expected.issuer = null
sasl.oauthbearer.jwks.endpoint.refresh.ms = 3600000
sasl.oauthbearer.jwks.endpoint.retry.backoff.max.ms = 10000
sasl.oauthbearer.jwks.endpoint.retry.backoff.ms = 100
sasl.oauthbearer.jwks.endpoint.url = null
sasl.oauthbearer.scope.claim.name = scope
sasl.oauthbearer.sub.claim.name = sub
sasl.oauthbearer.token.endpoint.url = null
security.protocol = PLAINTEXT
security.providers = null
send.buffer.bytes = 131072
session.timeout.ms = 45000
socket.connection.setup.timeout.max.ms = 30000
socket.connection.setup.timeout.ms = 10000
ssl.cipher.suites = null
ssl.enabled.protocols = [TLSv1.2]
ssl.endpoint.identification.algorithm = https
ssl.engine.factory.class = null
ssl.key.password = null
ssl.keymanager.algorithm = SunX509
ssl.keystore.certificate.chain = null
ssl.keystore.key = null
ssl.keystore.location = null
ssl.keystore.password = null
ssl.keystore.type = JKS
ssl.protocol = TLSv1.2
ssl.provider = null
ssl.secure.random.implementation = null
ssl.trustmanager.algorithm = PKIX
ssl.truststore.certificates = null
ssl.truststore.location = null
ssl.truststore.password = null
ssl.truststore.type = JKS
value.deserializer = class org.apache.kafka.common.serialization.StringDeserializer
[main] INFO org.apache.kafka.common.utils.AppInfoParser - Kafka version: 3.1.0

Step-6)  Ignite Streamer app with id gpdev1 joined as a consumer (logs from KAFKA console)

: Stabilized group gpdev1 generation 2 (__consumer_offsets-23) with 4 members (kafka.coordinator.group.GroupCoordinator)

ignite-kafka-streamer-demo-kafka-1      | [2022-02-01 04:35:55,636] INFO [GroupCoordinator 1]: Assignment received from leader consumer-gpdev1-3-f1d6b278-f016-4e99-a204-5db4c401d522 for group gpdev1 for generation 2. The group has 4 members, 0 of which are static. (kafka.coordinator.group.GroupCoordinator)

ignite-kafka-streamer-demo-kafka-1      | [2022-02-01 04:36:51,342] INFO [GroupCoordinator 1]: Dynamic member with unknown member id joins group gpdev1 in Stable state. Created a new member id consumer-gpdev1-3-5de0d9d2-0997-4617-b4df-45ad40ba654b and request the 



Step-7 : Start a kafka console producer for topic topic "test-topic" and published some messages as follows :

gourabpattanayak@Gourabs-MBP ignite-kafka-streamer-demo % docker exec -it ignite-kafka-streamer-demo-kafka-1 kafka-console-producer.sh --topic test-topic --property "parse.key=true" --property "key.separator=:"  --bootstrap-server localhost:9092


>demokey1:demovalue1


Step-8 : I can see the Ignite Kafka Streamer got the message and the MyStreamExtractor class also able to read the ConsumerRecord and able to print the message as follows:



###############ConsumerRecord topic name:test-topic key :demokey1 and value :demovalue1


Step-9 At this point expectation is the cache "mydemocache" should have entries for the key/value pairs published to Kafka Topic , so verify from the visor tool.

visor> cache

Time of the snapshot: 2022-02-01 01:04:05

+==============================================================================================================================================================+

|     Name(@)      |    Mode     | Nodes | Total entries (Heap / Off-heap) | Primary entries (Heap / Off-heap) |   Hits    |  Misses   |   Reads   |  Writes   |

+==============================================================================================================================================================+

| mydemocache(@c0) | PARTITIONED | 2     | 1 (0 / 1)                       | min: 0 (0 / 0)                    | min: 0    | min: 0    | min: 0    | min: 0    |

|                  |             |       |                                 | avg: 0.50 (0.00 / 0.50)           | avg: 0.00 | avg: 0.00 | avg: 0.00 | avg: 0.00 |

|                  |             |       |                                 | max: 1 (0 / 1)                    | max: 0    | max: 0    | max: 0    | max: 0    |

+--------------------------------------------------------------------------------------------------------------------------------------------------------------+







