package com.foot.repository.chat;

import com.foot.entity.*;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor

public class ChatLogRepositoryImpl implements ChatLogRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
    QChannel channel =QChannel.channel;

    QChatLog chatLog=QChatLog.chatLog;

    @Override
    public Long getMessageLeadCount(Long channelId) {
        return  jpaQueryFactory.select(chatLog.count())
                .from(chatLog)
                .leftJoin(chatLog.channel)
                .where(chatLog.adminRead.eq(1).and(chatLog.channel.id.eq(channelId)))
                .fetchFirst();
    }

    @Override
    public Optional<ChatLog> getChannelLastMsg(Long channelId) {
        OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(Order.DESC, chatLog.createdAt);
        return Optional.ofNullable(jpaQueryFactory.select(chatLog)
                .from(chatLog)
                .leftJoin(chatLog.channel)
                .where(chatLog.channel.id.eq(channelId))
                .orderBy(orderSpecifier)
                .fetchFirst());
    }

    @Override
    public Long getMessageUserLeadCount(Long channelId) {
        return  jpaQueryFactory.select(chatLog.count())
                .from(chatLog)
                .leftJoin(chatLog.channel)
                .where(chatLog.userRead.eq(1).and(chatLog.channel.id.eq(channelId)))
                .fetchFirst();
    }

    @Override
    public Long getMessageTotalLeadCount() {
        return jpaQueryFactory.select(chatLog.count())
        .from(chatLog)
        .where(chatLog.adminRead.eq(1))
        .fetchFirst();
    }

    @Override
    public List<ChatLog> getChannelAdminMsg(Long channelId) {
        return  jpaQueryFactory.select(chatLog)
                .from(chatLog)
                .leftJoin(chatLog.channel)
                .where(chatLog.adminRead.eq(1).and(chatLog.channel.id.eq(channelId)))
                .fetch();
    }

    @Override
    public List<ChatLog> getChannelUserMsg(Long channelId) {
        return  jpaQueryFactory.select(chatLog)
                .from(chatLog)
                .leftJoin(chatLog.channel)
                .where(chatLog.userRead.eq(1).and(chatLog.channel.id.eq(channelId)))
                .fetch();
    }
}
