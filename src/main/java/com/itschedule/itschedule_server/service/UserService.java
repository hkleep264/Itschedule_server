package com.itschedule.itschedule_server.service;

import com.itschedule.itschedule_server.repository.UserRepository;
import com.itschedule.itschedule_server.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


}
