package com.foot.dto;

import lombok.Getter;

@Getter
public class ProfileRequestDto {
    private String password;
    private String newPassword;
    private String name;
    private String email;
    private String address;
    private String cellphone;
    private String userImage;
}
