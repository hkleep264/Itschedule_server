package com.itschedule.itschedule_server.repository;

import com.itschedule.itschedule_server.vo.BoardVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface BoardRepository {
    List<BoardVo> getBoardList(Map<String, Object> parameter);
    Integer boardListTotalCount(Map<String, Object> parameter);
    BoardVo getBoardInfo(Map<String, String> parameter);
    void insertBoardInfo(Map<String, String> parameter);
    void updateBoardInfo(Map<String, String> parameter);
}
