package com.foot.dto.chats;

import com.foot.entity.ChatLog;
import lombok.Getter;

@Getter
public class ChannelMessageResponseDto {
    String message;
    String messageUser;
    Long datetime;
    String messageImg;

    public ChannelMessageResponseDto(ChatLog chatLog){
        this.message = chatLog.getMessage();
        this.messageUser = chatLog.getUser().getName();
        this.datetime = chatLog.getDatetime();
        this.messageImg = chatLog.getMessageImg();
    }
}
