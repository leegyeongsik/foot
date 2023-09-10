package com.foot.config;

import com.foot.dto.AdminUserRequestDto;
import com.foot.dto.OrderRequestDto;
import com.foot.dto.chats.ChatMessageRequestDto;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import com.google.common.collect.ImmutableMap;

import org.apache.kafka.common.serialization.IntegerSerializer;
import org.aspectj.weaver.ast.Or;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {
    //Sender config
    @Bean
    public ProducerFactory<String, ChatMessageRequestDto> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs(), null, new JsonSerializer<ChatMessageRequestDto>());
    }



    @Bean
    public KafkaTemplate<String, ChatMessageRequestDto> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<String, OrderRequestDto> producerFactoryOrder() {
        return new DefaultKafkaProducerFactory<>(producerConfigs(), null, new JsonSerializer<OrderRequestDto>());
    }

    @Bean
    public KafkaTemplate<String, OrderRequestDto> kafkaTemplateOrder() {
        return new KafkaTemplate<>(producerFactoryOrder());
    }

    @Bean
    public Map<String, Object> producerConfigs() {

        return ImmutableMap.<String, Object>builder()
                .put("bootstrap.servers", "localhost:9092")
                .put("key.serializer", IntegerSerializer.class)
                .put("value.serializer", JsonSerializer.class)
                .put("group.id", "spring-boot")
                .build();
    }





    //Receiver config
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ChatMessageRequestDto> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ChatMessageRequestDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }



    @Bean
    public ConsumerFactory<String, ChatMessageRequestDto> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(), null, new JsonDeserializer<>(ChatMessageRequestDto.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderRequestDto> kafkaListenerContainerFactoryOrder() {
        ConcurrentKafkaListenerContainerFactory<String, OrderRequestDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryOrder());
        return factory;
    }



    @Bean
    public ConsumerFactory<String, OrderRequestDto> consumerFactoryOrder() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(), null, new JsonDeserializer<>(OrderRequestDto.class));
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        return ImmutableMap.<String, Object>builder()
                .put("bootstrap.servers", "localhost:9092")
                .put("key.deserializer", IntegerDeserializer.class)
                .put("value.deserializer", JsonDeserializer.class)
                .put("group.id", "spring-boot")
                .build();
    }


}
