package com.itschedule.itschedule_server.repository;

import com.itschedule.itschedule_server.vo.AlertVo;
import com.itschedule.itschedule_server.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface UserRepository {

    UserVo getUserInfoForLogin(Map<String, String> parameter);
    UserVo getUserInfo(Map<String, String> parameter);
    void insertUser(Map<String, String> parameter);
    List<UserVo> getUserList(Map<String, Object> parameter);
    int userListTotalCount(Map<String, Object> parameter);
    void userAuthUpdate(Map<String, Object> parameter);
    void userAdminUpdate(Map<String, Object> parameter);
    void insertUserEvent(Map<String, Object> parameter);
    void updateUserEvent(Map<String, Object> parameter);
    List<AlertVo> getAlertList(Map<String, Object> parameter);
}
