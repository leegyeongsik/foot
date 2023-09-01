package com.foot.dto.chats;

import com.foot.entity.Channel;
import lombok.Getter;

@Getter

public class ChannelResponseDto {
    Long channelId;
    String channelOwner;
    // Long yetMessageCnt;
    // String latestMsg

    public ChannelResponseDto(Channel channel) {
        this.channelId = channel.getId();
        this.channelOwner = channel.getUser().getName();
    }
}
