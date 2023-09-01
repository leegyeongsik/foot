package com.foot.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foot.dto.chats.ChatMessageRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j


public class MessageConsumer {
    private final SimpMessagingTemplate template;

    @KafkaListener(id = "message-listener", topics = "kafka-chatting1")
    public void receive(ChatMessageRequestDto message) throws Exception {
//        log.info("message='{}'", message);
        HashMap<String, String> msg = new HashMap<>();
        msg.put("timestamp", Long.toString(message.getTimeStamp()));
        msg.put("message", message.getMessage());
        msg.put("user", message.getWriter());
        msg.put("img", message.getMessageImg());
        msg.put("channelId", Long.toString(message.getChannelId()));
        ObjectMapper mapper = new ObjectMapper();
        String messageJson = mapper.writeValueAsString(msg);

//        log.info("messageJson='{}'", messageJson);
        template.convertAndSend("/topic/api/channel/" + message.getChannelId(), messageJson);
    }
}