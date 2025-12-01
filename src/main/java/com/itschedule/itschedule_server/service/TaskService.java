package com.itschedule.itschedule_server.service;

import com.itschedule.itschedule_server.repository.TaskRepository;
import com.itschedule.itschedule_server.vo.BoardVo;
import com.itschedule.itschedule_server.vo.TaskVo;
import com.itschedule.itschedule_server.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    //Task 리스트 가져오기(본인 담당 Issue)
    public List<TaskVo> getTaskList(Map<String, Object> parameter){
        return taskRepository.getTaskList(parameter);
    }

    //Task 리스트에서 스티커 이동하여 상태값 빠른 업데이트
    public void taskQuickUpdate(Map<String, Object> parameter){
        taskRepository.taskQuickUpdate(parameter);
    }

    //Task 상세보기에서 업데이트
    public void taskUpdate(Map<String, Object> parameter){
        taskRepository.taskUpdate(parameter);
    }


}
