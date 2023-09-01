package com.foot.service;

import com.foot.dto.ProfileResponseDto;
import com.foot.dto.chats.ChannelMessageResponseDto;
import com.foot.dto.chats.ChannelResponseDto;
import com.foot.dto.chats.IsExistChannelResponseDto;
import com.foot.entity.Channel;
import com.foot.entity.ChatLog;
import com.foot.entity.User;
import com.foot.repository.ChannelRepository;
import com.foot.repository.ChatLogRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

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
            channelResponseDtoArrayList.add(new ChannelResponseDto(channel));
        }
        return channelResponseDtoArrayList;
    }

    public List<ChannelMessageResponseDto> getChannelMessage(String channelId) {
        List<ChatLog> chatLogList = chatLogRepository.findAllByChannel_id(Long.valueOf(channelId));
        ArrayList<ChannelMessageResponseDto> channelMessageResponseDtos = new ArrayList<>();
        for (ChatLog chatLog : chatLogList) {
            channelMessageResponseDtos.add(new ChannelMessageResponseDto(chatLog));
        }
        return channelMessageResponseDtos;
    }

    public IsExistChannelResponseDto getInfo(User user) {
        boolean isExist;
        Optional<Channel> channel=channelRepository.findByUser_id(user.getId());
        if (!channel.isEmpty()){
            isExist = true;
        } else {
            isExist = false;
        }
        return new IsExistChannelResponseDto(isExist,new ProfileResponseDto(user));
    }

    public IsExistChannelResponseDto getChannelInfo(User user) {
        Optional<Channel> channel=channelRepository.findByUser_id(user.getId());
        return new IsExistChannelResponseDto(channel.get().getId(),new ProfileResponseDto(user));

    }
}

