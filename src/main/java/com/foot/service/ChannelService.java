package com.foot.service;

import com.foot.dto.ProfileResponseDto;
import com.foot.dto.chats.ChannelMessageResponseDto;
import com.foot.dto.chats.ChannelResponseDto;
import com.foot.dto.chats.IsExistChannelResponseDto;
import com.foot.entity.Channel;
import com.foot.entity.ChatLog;
import com.foot.entity.User;
import com.foot.entity.UserRoleEnum;
import com.foot.repository.ChannelRepository;
import com.foot.repository.chat.ChatLogRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChannelService {
    private final ChannelRepository channelRepository;
    private final ChatLogRepository chatLogRepository;

    public void createChannel(User user) {
        Channel channel = Channel.builder().user(user).build();
        channelRepository.save(channel);
    }

    public List<ChannelResponseDto> getChannel() {
        List<Channel> channelList = channelRepository.findAll();
        ArrayList<ChannelResponseDto> channelResponseDtoArrayList = new ArrayList<>();
        for (Channel channel : channelList) {
            Long readCnt=chatLogRepository.getMessageLeadCount(channel.getId());
            Optional<ChatLog>  chatLog= chatLogRepository.getChannelLastMsg(channel.getId());
            if(chatLog.isEmpty()){
                String none = "";
                channelResponseDtoArrayList.add(new ChannelResponseDto(channel ,none));
            } else {
                channelResponseDtoArrayList.add(new ChannelResponseDto(channel,readCnt , chatLog.get()));

            }
        }
        return channelResponseDtoArrayList;
    }

    public List<ChannelMessageResponseDto> getChannelMessage(String channelId) {
        List<ChatLog> chatLogList = chatLogRepository.findAllByChannelId(Long.valueOf(channelId));
        ArrayList<ChannelMessageResponseDto> channelMessageResponseDtos = new ArrayList<>();
        for (ChatLog chatLog : chatLogList) {
            channelMessageResponseDtos.add(new ChannelMessageResponseDto(chatLog));
        }
        return channelMessageResponseDtos;
    }

    public IsExistChannelResponseDto getInfo(User user) {
        Optional<Channel> channel=channelRepository.findByUser_id(user.getId());
        if (channel.isPresent() && user.getRole().equals(UserRoleEnum.USER)){
            Long userLeadCount=chatLogRepository.getMessageUserLeadCount(channel.get().getId());
            return new IsExistChannelResponseDto(true,new ProfileResponseDto(user) , channel.get().getId() ,userLeadCount);

        } else if (!channel.isPresent() && user.getRole().equals(UserRoleEnum.USER)) {

            return new IsExistChannelResponseDto(false,new ProfileResponseDto(user));
        } else {
            Long AdminLeadTotalCnt=chatLogRepository.getMessageTotalLeadCount();
            return new IsExistChannelResponseDto(new ProfileResponseDto(user) ,AdminLeadTotalCnt);
        }
    }

    public IsExistChannelResponseDto getChannelInfo(User user) {
        Optional<Channel> channel=channelRepository.findByUser_id(user.getId());
        return new IsExistChannelResponseDto(channel.get().getId(),new ProfileResponseDto(user));

    }
}

