package com.gp.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.internal.IgnitionEx;
import org.apache.ignite.stream.kafka.KafkaStreamer;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Arrays;
import java.util.Properties;

public class MyKafkaStreamer{


    public static final Properties config = new Properties();
    static{
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "gpdev1");
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");
    }

    public static void main(String[] args) throws Exception{

        IgniteConfiguration cfg = IgnitionEx.loadConfiguration("/Users/gourabpattanayak/ignite-kafka-streamer-demo/cluster_config/ignite-cluster-1.xml").get1();
        Ignite ignite = Ignition.start(cfg);

        IgniteDataStreamer<String, String> stmr = ignite.dataStreamer("mydemocache");
        stmr.autoFlushFrequency(1);
        stmr.allowOverwrite(true);
        //stmr.receiver(new MyStreamReceiver());
        KafkaStreamer<String, String> kafkaStreamer = new KafkaStreamer<>();
        kafkaStreamer.setIgnite(ignite);
        kafkaStreamer.setStreamer(stmr);
        kafkaStreamer.setTopic(Arrays.asList("test-topic"));
        kafkaStreamer.setThreads(4);
        //kafkaStreamer.setTimeout(10000);
        kafkaStreamer.setConsumerConfig(config);
        // set extractor
        kafkaStreamer.setSingleTupleExtractor(new MyStreamExtractor());
        kafkaStreamer.start();
    }
}
