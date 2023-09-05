package com.foot.dto.chats;

import com.foot.entity.Channel;
import com.foot.entity.ChatLog;
import lombok.Getter;

@Getter

public class ChannelResponseDto {
    Long channelId;
    String channelOwner;
    Long yetMessageCnt;
    String latestMsg;

    public ChannelResponseDto(Channel channel , Long yetMessageCnt , ChatLog chatLog) {
        this.channelId = channel.getId();
        this.channelOwner = channel.getUser().getName();
        this.latestMsg = chatLog.getMessage();
        this.yetMessageCnt = yetMessageCnt;
    }
    public ChannelResponseDto(Channel channel , String none) {
        this.channelId = channel.getId();
        this.channelOwner = channel.getUser().getName();
        this.latestMsg = none;
        this.yetMessageCnt = 0L;
    }
}
