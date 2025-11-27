package com.itschedule.itschedule_server.repository;

import com.itschedule.itschedule_server.vo.BoardVo;
import com.itschedule.itschedule_server.vo.IssueVo;
import com.itschedule.itschedule_server.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface IssueRepository {
    List<IssueVo> getIssueList(Map<String, Object> parameter);
    Integer issueListTotalCount(Map<String, Object> parameter);
    IssueVo getIssueInfo(Map<String, String> parameter);
    List<BoardVo> getProjectList(Map<String, Object> parameter);
    List<BoardVo> getProjectListWithMember(Map<String, Object> parameter);
    void insertIssueInfo(Map<String, Object> parameter);
    void updateIssueInfo(Map<String, String> parameter);
    List<UserVo> getUserListAll(Map<String, String> parameter);
    List<UserVo> getUserListForProject(Map<String, String> parameter);
    void clearIssueMember(@Param("issueId") int issueId);
    void insertIssueMember(@Param("issueId") int issueId,  @Param("memberList") List<UserVo> memberList);
}
