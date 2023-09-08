package com.foot.kafka;

import com.foot.dto.OrderRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j

public class OrderProducer {
    private final KafkaTemplate<String, OrderRequestDto> kafkaTemplate;

    public void send(String topic, OrderRequestDto data) { // 주문이 들어오면 주문내용을(OrderRequestDto) 토픽에 전달함
//        log.info("sending data='{}' to topic='{}'", data, topic);
        kafkaTemplate.send(topic, data);
    }
}
