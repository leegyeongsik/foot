package com.foot.kafka;

import com.foot.dto.OrderRequestDto;
import com.foot.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderConsumer {
    private final ProductService productService;
    @KafkaListener(groupId = "order-listener1", topics = "kafka-order", containerFactory = "kafkaListenerContainerFactoryOrder")
    public void orderReceive(OrderRequestDto orderRequestDto) { // 해당 토픽에서 들어온 데이터를 받음
        log.info("message='{}'", orderRequestDto);
        productService.OrderProduct(orderRequestDto);
    }
}
