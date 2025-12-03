package com.itschedule.itschedule_server.service;

import com.itschedule.itschedule_server.repository.IssueRepository;
import com.itschedule.itschedule_server.vo.BoardVo;
import com.itschedule.itschedule_server.vo.IssueVo;
import com.itschedule.itschedule_server.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
@Service
public class IssueService {

    private static final Logger logger = LoggerFactory.getLogger(IssueService.class);

    private final IssueRepository issueRepository;

    public IssueService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    //이슈 리스트 가져오기
    public List<IssueVo> getIssueList(Map<String, Object> parameter){
        return issueRepository.getIssueList(parameter);
    }

    //이슈 전체 리스트 갯수 져오기
    public int issueListTotalCount(Map<String, Object> parameter){
        Integer cnt = issueRepository.issueListTotalCount(parameter);
        return cnt == null ? 0 : cnt;
    }

    //이슈 정보 가져오기
    public IssueVo getIssueInfo(Map<String, String> parameter){
        return issueRepository.getIssueInfo(parameter);
    }

    //프로젝트 리스트 가져오기
    public List<BoardVo> getProjectListWithMember(Map<String, Object> parameter){
        return issueRepository.getProjectListWithMember(parameter);
    }

    //이슈 정보 추가
    public void insertIssueInfo(Map<String, Object> parameter){
        issueRepository.insertIssueInfo(parameter);
    }

    //이슈 정보 수정
    public void updateIssueInfo(Map<String, Object> parameter){
        issueRepository.updateIssueInfo(parameter);
    }

    //이슈 멤버 가져오기
    public List<UserVo> getUserListForProject(Map<String, String> parameter){
        return issueRepository.getUserListForProject(parameter);
    }

    //캘린더에서 이동하여 날짜값 빠른 업데이트
    public void issueQuickUpdate(Map<String, Object> parameter){
        issueRepository.issueQuickUpdate(parameter);
    }

    //이슈 TodoList 가져오기
    public List<IssueVo> getIssueTodoList(Map<String, Object> parameter){
        return issueRepository.getIssueTodoList(parameter);
    }

    //TodoList 별 표시 변경
    public void issueImportantUpdate(Map<String, Object> parameter){
        issueRepository.issueImportantUpdate(parameter);
    }


}
