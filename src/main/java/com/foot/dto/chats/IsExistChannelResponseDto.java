package com.foot.dto.chats;

import com.foot.dto.ProfileResponseDto;
import lombok.Getter;

import javax.swing.plaf.TableHeaderUI;

@Getter
public class IsExistChannelResponseDto {
    boolean IsExist;
    ProfileResponseDto responseDto;
    Long ChannelNum;
    Long UserLeadCnt;
    Long AdminLeadTotalCnt;
    public IsExistChannelResponseDto(boolean IsExist , ProfileResponseDto responseDto, Long channelId , Long UserLeadCnt){
        this.IsExist = IsExist;
        this.responseDto = responseDto;
        this.ChannelNum = channelId;
        this.UserLeadCnt = UserLeadCnt;
    }
    public IsExistChannelResponseDto( ProfileResponseDto responseDto , Long AdminLeadTotalCnt){
        this.responseDto = responseDto;
        this.AdminLeadTotalCnt = AdminLeadTotalCnt;
    }
    public IsExistChannelResponseDto( boolean IsExist , ProfileResponseDto responseDto){
        this.IsExist = IsExist;
        this.responseDto = responseDto;
    }
    public IsExistChannelResponseDto( Long ChannelNum , ProfileResponseDto responseDto){
        this.ChannelNum = ChannelNum;
        this.responseDto = responseDto;
    }
}
