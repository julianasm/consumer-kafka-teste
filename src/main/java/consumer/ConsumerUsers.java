package consumer;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.kafkaestudos.proto.Users;
import consumer.emailService.EmailService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.*;
import org.springframework.kafka.annotation.KafkaListener;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

@RequiredArgsConstructor
public class ConsumerUsers {

    private final EmailService emailService;

    public static void main(String[] args) throws InvalidProtocolBufferException {
        Properties props = new Properties();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group3");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializer");
        props.put("schema.registry.url", "http://localhost:8085");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        String topic = "new-user";
        final Consumer<String, Users> consumer = new KafkaConsumer<String, Users>(props);
        consumer.subscribe(Arrays.asList(topic));
        try {
                ConsumerRecords<String, Users> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, Users> record : records) {

                    var user  = Users.parseFrom(record.value().toByteArray());
                    System.out.println(user.getEmail());
                }
            } finally {
            consumer.close();
        }

       }

    @KafkaListener(topics = "new-user", groupId = "group3")
    void listener() throws Exception {
        emailService.sendEmail("Vinicius Silva Farias", "julianasouzamelo@live.com", "Salve Salve");
    }
}
