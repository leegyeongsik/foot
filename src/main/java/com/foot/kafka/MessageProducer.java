package com.foot.kafka;

import com.foot.dto.chats.ChatMessageRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j

public class MessageProducer {
    private final KafkaTemplate<String, ChatMessageRequestDto> kafkaTemplate;
    public void send(String topic, ChatMessageRequestDto data) {
//        log.info("sending data='{}' to topic='{}'", data, topic);
        kafkaTemplate.send(topic, data);
    }

}
