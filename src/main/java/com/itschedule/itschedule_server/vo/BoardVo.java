package com.itschedule.itschedule_server.vo;

import lombok.Data;

//프로젝트 계시물
@Data
public class BoardVo {
    private int id;
    private String name;
    private String content;
    private String startDate;
    private String endDate;
    private String updated;
    private String created;
}
