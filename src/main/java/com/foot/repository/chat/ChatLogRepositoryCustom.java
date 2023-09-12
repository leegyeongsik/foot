package com.foot.repository.chat;

import com.foot.entity.Channel;
import com.foot.entity.ChatLog;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatLogRepositoryCustom {
    Long getMessageLeadCount(Long channelId);
    Optional<ChatLog>  getChannelLastMsg(Long channelId);
    Long getMessageUserLeadCount(Long channelId);

    Long getMessageTotalLeadCount();

    List<ChatLog>  getChannelAdminMsg(Long channelId);
    List<ChatLog> getChannelUserMsg(Long channelId);

}
