package com.itschedule.itschedule_server.repository;

import com.itschedule.itschedule_server.vo.BoardVo;
import com.itschedule.itschedule_server.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface BoardRepository {
    List<BoardVo> getBoardList(Map<String, Object> parameter);
    Integer boardListTotalCount(Map<String, Object> parameter);
    BoardVo getBoardInfo(Map<String, String> parameter);
    void insertBoardInfo(Map<String, Object> parameter);
    void updateBoardInfo(Map<String, String> parameter);
    List<UserVo> getUserListAll(Map<String, String> parameter);
    List<UserVo> getUserListForProject(Map<String, String> parameter);
    void clearBoardMember(@Param("boardId") int boardId);
    void insertBoardMember(@Param("boardId") int boardId,  @Param("memberList") List<UserVo> memberList);
    void boardQuickUpdate(Map<String, Object> parameter);
}
