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

    @KafkaListener(id = "message-listener", topics = "kafka-chatting")
    public void receive(ChatMessageRequestDto message) throws Exception { // 토픽에 전송된 데이터를 consumer에서 가져옴
//        log.info("message='{}'", message);
        HashMap<String, String> msg = new HashMap<>();
        msg.put("timestamp", String.valueOf(message.getTimeStamp()));
        msg.put("message", message.getMessage());
        msg.put("user", message.getWriter());
        msg.put("img", message.getMessageImg());
        msg.put("channelId", Long.toString(message.getChannelId()));


        ObjectMapper mapper = new ObjectMapper();
        String messageJson = mapper.writeValueAsString(msg);
//        log.info("messageJson='{}'", messageJson);
        template.convertAndSend("/topic/api/channel/" + message.getChannelId(), messageJson); // 가져온 데이터를 해당 채널에 전송
        if(message.getIsUserRead() == 1){ // 해당 유저의 채널에 해당 유저가 들어가있지 않다면 해당 유저에게 몇개를 안읽었는지 데이터를 전송함
            HashMap<String, String> UserMsgCnt = new HashMap<>();
            UserMsgCnt.put("userCnt" , String.valueOf(message.getTotalRead()));
            ObjectMapper mapper1 = new ObjectMapper();
            String UserMsgCntJson = mapper1.writeValueAsString(UserMsgCnt);
            template.convertAndSend("/topic/api/user/channel/" + message.getChannelId(), UserMsgCntJson);

        } else if (message.getIsAdminRead() ==1 ) { // 만약 어드민이 해당 채널에 들어가있지 않다면 어드민의 채널목록에 해당 유저채널에 데이터를 몇개 안읽었는지 전송함
            msg.put("adminChannelCnt" , String.valueOf(message.getTotalRead()));
            String messageJsons = mapper.writeValueAsString(msg);
            template.convertAndSend("/topic/api/channel", messageJsons);

            HashMap<String, String> adminMsgCnt = new HashMap<>();
            adminMsgCnt.put("AdminTotalCnt" , String.valueOf(message.getAdminTotalRead())); // 그리고 어드민에게 토탈로 몇개를 안읽었는지 채팅cnt를 전송함
            ObjectMapper mapper2 = new ObjectMapper();
            String adminMsgCntJson = mapper2.writeValueAsString(adminMsgCnt);
            template.convertAndSend("/topic/api/Admin", adminMsgCntJson);
        }
    }
}