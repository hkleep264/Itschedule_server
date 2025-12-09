package com.itschedule.itschedule_server.service;

import com.itschedule.itschedule_server.repository.UserRepository;
import com.itschedule.itschedule_server.vo.AlertVo;
import com.itschedule.itschedule_server.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //유저정보가져오기 로그인용
    public UserVo getUserInfoForLogin(Map<String, String> parameter){
        return userRepository.getUserInfoForLogin(parameter);
    }

    //유저 있는지 체크
    public UserVo getUserInfo(Map<String, String> parameter){
        return userRepository.getUserInfo(parameter);
    }

    //유저 추가
    public void insertUser(Map<String, String> parameter){
        userRepository.insertUser(parameter);
    }

    //유저 리스트 가져오기
    public List<UserVo> getUserList(Map<String, Object> parameter){
        return userRepository.getUserList(parameter);
    }

    //유저 전체 리스트 갯수 져오기
    public int userListTotalCount(Map<String, Object> parameter){
        Integer cnt = userRepository.userListTotalCount(parameter);
        return cnt == null ? 0 : cnt;
    }

    //유저 인증 정보 업데이트
    public void userAuthUpdate(Map<String, Object> parameter){
        userRepository.userAuthUpdate(parameter);
    }

    //유저 관리자 정보 업데이트
    public void userAdminUpdate(Map<String, Object> parameter){
        userRepository.userAdminUpdate(parameter);
    }

    //이벤트 알림 추가
    public void insertUserEvent(Map<String, Object> parameter){
        userRepository.insertUserEvent(parameter);
    }

    //이벤트 알림 수정
    public void updateUserEvent(Map<String, Object> parameter){
        userRepository.updateUserEvent(parameter);
    }

    //이벤트 알림 리스트 가져오기
    public List<AlertVo> getAlertList(Map<String, Object> parameter){
        return userRepository.getAlertList(parameter);
    }


}
