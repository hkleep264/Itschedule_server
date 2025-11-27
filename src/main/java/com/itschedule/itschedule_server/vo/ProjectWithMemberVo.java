package com.itschedule.itschedule_server.vo;

import lombok.Data;

import java.util.List;

@Data
public class ProjectWithMemberVo {
    private int projectId;
    private String projectName;
    private List<MemberVo> members;
}
