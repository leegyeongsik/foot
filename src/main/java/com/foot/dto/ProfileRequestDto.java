package com.foot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileRequestDto {
    private String newPassword;
    private String name;
    private String email;
    private String address;
    private String cellphone;
    //private String userImage;
}
