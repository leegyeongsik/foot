package com.foot.config;

import com.foot.dto.AdminUserRequestDto;
import com.foot.dto.chats.ChatMessageRequestDto;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import com.google.common.collect.ImmutableMap;

import org.apache.kafka.common.serialization.IntegerSerializer;
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

//    @Bean
//    public ProducerFactory<String, AdminUserRequestDto> producerFactoryEnter() {
//        return new DefaultKafkaProducerFactory<>(producerEnterConfigs(), null, new JsonSerializer<AdminUserRequestDto>());
//    }
//
//    @Bean
//    public KafkaTemplate<String, AdminUserRequestDto> kafkaTemplateEnter() {
//        return new KafkaTemplate<>(producerFactoryEnter());
//    }

    @Bean
    public Map<String, Object> producerConfigs() {

        return ImmutableMap.<String, Object>builder()
                .put("bootstrap.servers", "localhost:9092")
                .put("key.serializer", IntegerSerializer.class)
                .put("value.serializer", JsonSerializer.class)
                .put("group.id", "spring-boot-test")
                .build();
    }
//
//    @Bean
//    public Map<String, Object> producerEnterConfigs() {
//
//        return ImmutableMap.<String, Object>builder()
//                .put("bootstrap.servers", "localhost:9092")
//                .put("key.serializer", IntegerSerializer.class)
//                .put("value.serializer", JsonSerializer.class)
//                .put("group.id" , "enter_table")
//                .build();
//    }




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

//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, AdminUserRequestDto> kafkaListenerContainerFactoryEnter() {
//        ConcurrentKafkaListenerContainerFactory<String, AdminUserRequestDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactoryEnter());
//        return factory;
//    }



//    @Bean
//    public ConsumerFactory<String, AdminUserRequestDto> consumerFactoryEnter() {
//        return new DefaultKafkaConsumerFactory<>(consumerEnterConfigs(), null, new JsonDeserializer<>(AdminUserRequestDto.class));
//    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        return ImmutableMap.<String, Object>builder()
                .put("bootstrap.servers", "localhost:9092")
                .put("key.deserializer", IntegerDeserializer.class)
                .put("value.deserializer", JsonDeserializer.class)
                .put("group.id", "spring-boot-test")
                .build();
    }

//    @Bean
//    public Map<String, Object> consumerEnterConfigs() {
//        return ImmutableMap.<String, Object>builder()
//                .put("bootstrap.servers", "localhost:9092")
//                .put("key.deserializer", IntegerDeserializer.class)
//                .put("value.deserializer", JsonDeserializer.class)
//                .put("group.id" , "enter_table")
//                .build();
//    }
}
