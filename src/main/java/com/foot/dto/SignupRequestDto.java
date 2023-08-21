package com.foot.dto;

import com.foot.entity.UserRoleEnum;
import lombok.Getter;

@Getter
public class SignupRequestDto {

    private String email;

    private String name;

    private String password;

    private String address;

    private String cellphone;

    private String userimage;

    private UserRoleEnum role;

}
