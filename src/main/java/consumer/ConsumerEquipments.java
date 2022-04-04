package consumer;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.kafkaestudos.proto.Equipment;
import org.apache.kafka.clients.consumer.*;
import org.springframework.kafka.annotation.KafkaListener;


import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerEquipments {

    public static void main(String[] args) throws InvalidProtocolBufferException {
        Properties props = new Properties();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group1");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializer");
        props.put("schema.registry.url", "http://localhost:8085");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        String topic = "equipment";
        final Consumer<String, Message> consumer = new KafkaConsumer<String, Message>(props);
        consumer.subscribe(Arrays.asList(topic));

        try {
            while (true) {
                ConsumerRecords<String, Message> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, Message> record : records) {

                    var e  = Equipment.parseFrom(record.value().toByteArray());
                    System.out.println(e.getColor());
                }
            }
        } finally {
            consumer.close();
        }

    }

}
