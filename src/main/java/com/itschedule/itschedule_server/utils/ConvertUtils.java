package com.itschedule.itschedule_server.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itschedule.itschedule_server.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;

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
}
