package com.itschedule.itschedule_server.vo;

import lombok.Data;

@Data
public class UserVo {
    private int id;
    private String name;
    private String email;
    private String password;
    private String password2;
    private String isEmailAuth;
    private int isAdmin;
    private String updated;
    private String created;
}
