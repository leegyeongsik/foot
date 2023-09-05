package com.foot.repository.chat;

import com.foot.entity.ChatLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface ChatLogRepository extends JpaRepository<ChatLog, Long> , ChatLogRepositoryCustom {
    List<ChatLog> findAllByChannel_id(Long channelId);
}
