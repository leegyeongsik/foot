package com.foot.dto.chats;

import com.foot.entity.ChatLog;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChannelMessageResponseDto {
    String message;
    String messageUser;
    LocalDateTime datetime;
    String messageImg;

    public ChannelMessageResponseDto(ChatLog chatLog){
        this.message = chatLog.getMessage();
        this.messageUser = chatLog.getUser().getName();
        this.datetime = chatLog.getCreatedAt();
        this.messageImg = chatLog.getMessageImg();
    }
}
