package com.foot.dto;

import com.foot.entity.UserRoleEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    private String email;

    private String name;

    private String password;

    private String address;

    private String cellphone;

    private boolean admin = false;

    private String adminToken = "";

   //private String userImage;

    //private UserRoleEnum role;

}
