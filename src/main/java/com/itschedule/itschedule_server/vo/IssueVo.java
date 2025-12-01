package com.itschedule.itschedule_server.vo;

import lombok.Data;

//프로젝트 계시물
@Data
public class IssueVo {
    private int id;
    private int projectId;
    private int issueId;
    private String name;
    private String content;
    private String issueType;
    private int issuePriority;
    private int issueStatus;
    private int managerUserId;
    private int writeUserId;
    private String startDate;
    private String endDate;
    private String updated;
    private String created;

    private String projectName;
    private String managerName;
    private String writerName;


}
