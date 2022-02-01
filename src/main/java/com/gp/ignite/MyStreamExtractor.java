package com.gp.ignite;

import org.apache.ignite.stream.StreamSingleTupleExtractor;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.AbstractMap;
import java.util.Map;

public class MyStreamExtractor implements StreamSingleTupleExtractor<ConsumerRecord, String, String> {

    public Map.Entry<String, String> extract(ConsumerRecord record) {
        System.out.println("###############ConsumerRecord topic name:"+ record.topic()+" key :"+record.key()+" and value :"+record.value());
        return new AbstractMap.SimpleEntry<String, String>("key-"+record.key(), "val-"+record.value());
    }
}