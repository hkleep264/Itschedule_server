package com.itschedule.itschedule_server.vo;

import lombok.Data;

@Data
public class AlertVo {
    private int id;
    private int type;
    private String content;
    private int userId;
    private int targetId;
    private int status;
    private String updated;
    private String created;

    private int projectId;

}
