package com.foot.dto;

import com.foot.entity.User;
import lombok.Getter;

@Getter
public class ProfileResponseDto {
    private String name;
    private String email;
    private String address;
    private String cellphone;
    private String userImage;

    public ProfileResponseDto(User currentUser) {
        this.name = currentUser.getName();
        this.email = currentUser.getEmail();
        this.address = currentUser.getAddress();
        this.cellphone = currentUser.getCellphone();
        this.userImage = currentUser.getUserImage();
    }
}
