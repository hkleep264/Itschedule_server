package com.itschedule.itschedule_server.service;

import com.itschedule.itschedule_server.repository.BoardRepository;
import com.itschedule.itschedule_server.vo.BoardVo;
import com.itschedule.itschedule_server.vo.UserVo;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
@Service
public class BoardService {

    private static final Logger logger = LoggerFactory.getLogger(BoardService.class);

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    //프로젝트 리스트 가져오기
    public List<BoardVo> getBoardList(Map<String, Object> parameter){
        return boardRepository.getBoardList(parameter);
    }

    //프로젝트 전체 리스트 갯수 져오기
    public int boardListTotalCount(Map<String, Object> parameter){
        Integer cnt = boardRepository.boardListTotalCount(parameter);
        return cnt == null ? 0 : cnt;
    }

    //프로젝트 정보 가져오기
    public BoardVo getBoardInfo(Map<String, String> parameter){
        return boardRepository.getBoardInfo(parameter);
    }

    //프로젝트 정보 추가
    public void insertBoardInfo(Map<String, Object> parameter){
        boardRepository.insertBoardInfo(parameter);
    }

    //프로젝트 정보 수정
    public void updateBoardInfo(Map<String, String> parameter){
        boardRepository.updateBoardInfo(parameter);
    }

    //전체 유저 가져오기
    public List<UserVo> getUserListAll(Map<String, String> parameter){
        return boardRepository.getUserListAll(parameter);
    }

    //프로젝트 멤버 가져오기
    public List<UserVo> getUserListForProject(Map<String, String> parameter){
        return boardRepository.getUserListForProject(parameter);
    }

    //프로젝트 멤버 교체
    public void updateProjectMemberList(int boardId, List<UserVo> memberList){
//        위에 @Transactional 걸려 있어서 실패시 자동 Rollback 처리

        //전체 멤버 클리어
        boardRepository.clearBoardMember(boardId);
        // 2) 새 멤버 없는 경우 종료
        if (memberList == null || memberList.isEmpty()) {
            return;
        }

        boardRepository.insertBoardMember(boardId, memberList);
    }

}
