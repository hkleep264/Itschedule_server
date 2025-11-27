package com.itschedule.itschedule_server.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itschedule.itschedule_server.vo.BoardVo;
import com.itschedule.itschedule_server.vo.MemberVo;
import com.itschedule.itschedule_server.vo.ProjectWithMemberVo;
import com.itschedule.itschedule_server.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ConvertUtils {
    public static String userVoToJson(UserVo userVo){
        String result = "";

        ObjectMapper mapper = new ObjectMapper();

        try {
            result = mapper.writeValueAsString(userVo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("User Json Convert Error: {}", e.getMessage());
        }

        return result;
    }

    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // 비밀번호 비교
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    public static List<ProjectWithMemberVo> convertProjectForMember(List<BoardVo> rows) {

        // projectId -> ProjectVo 매핑
        Map<Integer, ProjectWithMemberVo> map = new LinkedHashMap<>();

        for (BoardVo row : rows) {
            int pid = row.getProjectId();


            // 프로젝트가 처음 나온 경우 DTO 생성
            ProjectWithMemberVo project = map.get(pid);
            if (project == null) {
                project = new ProjectWithMemberVo();
                project.setProjectId(pid);
                project.setProjectName(row.getProjectName());
                project.setMembers(new ArrayList<>());
                map.put(pid, project);
            }

            if (row.getEmail() != null) {
                MemberVo m = new MemberVo();
                m.setUserId(String.valueOf(row.getUserId()));
                m.setUserName(row.getUserName());
                m.setEmail(row.getEmail());
                project.getMembers().add(m);
            }
        }

        return new ArrayList<>(map.values());
    }
}
