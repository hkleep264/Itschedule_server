package com.itschedule.itschedule_server.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itschedule.itschedule_server.vo.UserVo;
import lombok.extern.slf4j.Slf4j;

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
}
