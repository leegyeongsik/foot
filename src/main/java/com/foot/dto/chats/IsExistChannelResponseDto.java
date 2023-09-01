package com.foot.dto.chats;

import com.foot.dto.ProfileResponseDto;
import lombok.Getter;

@Getter
public class IsExistChannelResponseDto {
    boolean IsExist;
    ProfileResponseDto responseDto;
    Long ChannelNum;
    public IsExistChannelResponseDto( boolean IsExist , ProfileResponseDto responseDto){
        this.IsExist = IsExist;
        this.responseDto = responseDto;
    }
    public IsExistChannelResponseDto( Long ChannelNum , ProfileResponseDto responseDto){
        this.ChannelNum = ChannelNum;
        this.responseDto = responseDto;
    }
}
