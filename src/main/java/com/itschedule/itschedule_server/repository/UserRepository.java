package com.itschedule.itschedule_server.repository;

import com.itschedule.itschedule_server.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@Mapper
public interface UserRepository {

    UserVo getUserInfoForLogin(Map<String, String> parameter);
    UserVo getUserInfo(Map<String, String> parameter);
    void insertUser(Map<String, String> parameter);
}
