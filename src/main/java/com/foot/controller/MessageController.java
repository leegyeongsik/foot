package com.foot.controller;

import com.foot.dto.chats.ChatMessageRequestDto;
import com.foot.kafka.MessageProducer;
import com.foot.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MessageController {
    private final MessageProducer messageProducer;
    private static String BOOT_TOPIC = "kafka-chatting1";
    private final MessageService messageService;

    @MessageMapping("/chat/message")
    public void message(ChatMessageRequestDto message) throws IOException {
        log.info(String.valueOf(message));
        message.setTimeStamp(System.currentTimeMillis());
        if(message.getMessageImg() !=null){
            ChatMessageRequestDto messageRequestDto = messageService.messageImg(message);
            messageProducer.send(BOOT_TOPIC, messageRequestDto);
        } else {
            messageService.save(message);
            messageProducer.send(BOOT_TOPIC, message);
        }
    }
}
