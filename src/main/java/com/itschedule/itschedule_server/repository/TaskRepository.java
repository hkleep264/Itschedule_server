package com.itschedule.itschedule_server.repository;

import com.itschedule.itschedule_server.vo.BoardVo;
import com.itschedule.itschedule_server.vo.TaskVo;
import com.itschedule.itschedule_server.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface TaskRepository {
    List<TaskVo> getTaskList(Map<String, Object> parameter);
    void taskQuickUpdate(Map<String, Object> parameter);
    void taskUpdate(Map<String, Object> parameter);
}
