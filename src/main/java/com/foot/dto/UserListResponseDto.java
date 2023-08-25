package com.foot.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class UserListResponseDto {
    private List<ProfileResponseDto> userList;

    public UserListResponseDto(List<ProfileResponseDto> userList) {
        this.userList = userList;
    }
}