package com.foot.dto.chats;

import com.foot.dto.ProfileResponseDto;
import lombok.Getter;

import javax.swing.plaf.TableHeaderUI;

@Getter
public class IsExistChannelResponseDto {
    boolean isExist;
    ProfileResponseDto responseDto;
    Long channelNum;
    Long userLeadCnt;
    Long adminLeadTotalCnt;
    public IsExistChannelResponseDto(boolean IsExist , ProfileResponseDto responseDto, Long channelId , Long UserLeadCnt){
        this.isExist = IsExist;
        this.responseDto = responseDto;
        this.channelNum = channelId;
        this.userLeadCnt = UserLeadCnt;
    }
    public IsExistChannelResponseDto( ProfileResponseDto responseDto , Long AdminLeadTotalCnt){
        this.responseDto = responseDto;
        this.adminLeadTotalCnt = AdminLeadTotalCnt;
    }
    public IsExistChannelResponseDto( boolean IsExist , ProfileResponseDto responseDto){
        this.isExist = IsExist;
        this.responseDto = responseDto;
    }
    public IsExistChannelResponseDto( Long ChannelNum , ProfileResponseDto responseDto){
        this.channelNum = ChannelNum;
        this.responseDto = responseDto;
    }
}
