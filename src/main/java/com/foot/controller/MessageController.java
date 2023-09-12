package com.foot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foot.dto.chats.ChatMessageRequestDto;
import com.foot.dto.chats.EnterExitMessageRequestDto;
import com.foot.entity.Channel;
import com.foot.entity.ChatLog;
import com.foot.kafka.MessageProducer;
import com.foot.repository.ChannelRepository;
import com.foot.repository.chat.ChatLogRepository;
import com.foot.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MessageController {
    private final MessageProducer messageProducer;
    private static String BOOT_TOPIC = "kafka-chatting1";
    private final MessageService messageService;
    private final ChannelRepository repository;
    private final ChatLogRepository chatLogRepository;
    private final SimpMessagingTemplate template;

    @MessageMapping("/chat/message")
    public void message(ChatMessageRequestDto message) throws Exception {
        log.info(String.valueOf(message));
        if(message.getMessageImg() !=null){
            ChatMessageRequestDto messageRequestDto = messageService.messageImg(message);
            messageProducer.send(BOOT_TOPIC, messageRequestDto);
        } else {
            ChatLog chatLog =messageService.save(message);

            messageProducer.send(BOOT_TOPIC, messageService.setMessage(chatLog , message));
        }
    }
    @MessageMapping("/chat/enter")
    public void channelEnter(@RequestBody EnterExitMessageRequestDto enterExitMessageRequestDto) throws IOException {
        Channel channel=repository.findById(enterExitMessageRequestDto.getChannelIDs()).get();
        if(enterExitMessageRequestDto.getUserRole().equals("ADMIN")) {
            channel.setEnterAdmin(channel.getEnterAdmin()+1);
            repository.save(channel);
            List<ChatLog> chatLogList=chatLogRepository.getChannelAdminMsg(channel.getId());
            for (ChatLog chatLog : chatLogList) {
                chatLog.setAdminRead(0);
                chatLogRepository.save(chatLog);
            }
            HashMap<String, String> adminMsg = new HashMap<>();
            adminMsg.put("adminChannelCnt" , String.valueOf(chatLogRepository.getMessageLeadCount(channel.getId())));
            ObjectMapper mapper = new ObjectMapper();
            String messageJsons = mapper.writeValueAsString(adminMsg);
            template.convertAndSend("/topic/api/channel", messageJsons);

            HashMap<String, String> adminMsgCnt = new HashMap<>();
            adminMsgCnt.put("AdminTotalCnt" , String.valueOf(chatLogRepository.getMessageTotalLeadCount()));
            ObjectMapper mapper2 = new ObjectMapper();
            String adminMsgCntJson = mapper2.writeValueAsString(adminMsgCnt);
            template.convertAndSend("/topic/api/Admin", adminMsgCntJson);

        } else {
            channel.setEnterUser(channel.getEnterUser()+1);
            repository.save(channel);
            List<ChatLog> chatLogList=chatLogRepository.getChannelUserMsg(channel.getId());
            for (ChatLog chatLog : chatLogList) {
                chatLog.setUserRead(0);
                chatLogRepository.save(chatLog);
            }

            HashMap<String, String> msg = new HashMap<>();
            msg.put("userCnt" , String.valueOf(chatLogRepository.getMessageUserLeadCount(channel.getId())));
            ObjectMapper mapper1 = new ObjectMapper();
            String UserMsgCntJson = mapper1.writeValueAsString(msg);
            template.convertAndSend("/topic/api/user/channel/" + channel.getId(),msg );

        }
    }
    @MessageMapping("/chat/exit")
    public void channelExit(@RequestBody EnterExitMessageRequestDto enterExitMessageRequestDto) throws IOException {
        log.info(enterExitMessageRequestDto.toString());
        Channel channel=repository.findById(enterExitMessageRequestDto.getChannelIDs()).get();
        if(enterExitMessageRequestDto.getUserRole().equals("ADMIN")) {
            channel.setEnterAdmin(channel.getEnterAdmin()-1);
            repository.save(channel);
        } else {
            channel.setEnterUser(channel.getEnterUser()-1);
            repository.save(channel);
        }
    }


}
