package com.foot.controller;

import com.foot.dto.chats.ChannelMessageResponseDto;
import com.foot.dto.chats.ChannelResponseDto;
import com.foot.dto.chats.IsExistChannelResponseDto;
import com.foot.security.UserDetailsImpl;
import com.foot.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChannelController {
    private final ChannelService channelService;

    @GetMapping("/channel") // 어드민은 열려있는 채널을 볼수있음
    public List<ChannelResponseDto> getChannel(){
        return channelService.getChannel();
    }
    @PostMapping("/channel") // 유저가 채널을 만들지않았다면 채널을 생성
    public void createRoom(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        channelService.createChannel(userDetails.getUser());
    }


    @GetMapping("/channel/{channelId}") // 일단 채널에 있는 기존에 있었던 메시지를 다 읽어서 보내주고 그다음에 url 로 실시간 채팅을 함
    public List<ChannelMessageResponseDto> getChannelMessage(@PathVariable String channelId) {
        return channelService.getChannelMessage(channelId);
    }

    @GetMapping("/channel/info")
    public IsExistChannelResponseDto getInfo(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return channelService.getInfo(userDetails.getUser());
    }
    @GetMapping("/channel/infos")
    public IsExistChannelResponseDto getChannelInfo(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return channelService.getChannelInfo(userDetails.getUser());
    }

}

