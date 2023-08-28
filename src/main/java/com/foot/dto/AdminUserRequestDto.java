package com.foot.dto;

import com.foot.entity.UserRoleEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUserRequestDto {
    private Long userId;
    private String newPassword;
    private String name;
    private String email;
    private String address;
    private String cellphone;
    private String role;
}
