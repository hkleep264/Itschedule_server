package com.itschedule.itschedule_server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itschedule.itschedule_server.service.BoardService;
import com.itschedule.itschedule_server.vo.BoardVo;
import com.itschedule.itschedule_server.vo.UserVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/schedule/board")
public class BoardController {

    private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

    private static BoardService boardService = null;
    private static final BoardController _instance = new BoardController(boardService);

    public BoardController(BoardService boardService) {

        BoardController.boardService = boardService;
    }

    public static BoardController getInstance() {
        return _instance;
    }

    //프로젝트 추가
    @RequestMapping(method = RequestMethod.POST, value = "/insert")
    public ResponseEntity<String> insertBoard(@RequestBody String data, HttpServletRequest request){

        JSONObject response = new JSONObject();
        response.put("code","200");
        response.put("message","SUCCESS");
        response.put("msg","성공");

        JSONObject requestData = new JSONObject(data);
        logger.info("parameter: {}", data.toString());

        Map<String, Object> parameter = new HashMap<>();

        HttpSession session = request.getSession();

        if(requestData.has("name")
                && requestData.has("content")
                && requestData.has("startDate")
                && requestData.has("endDate")){

            parameter.put("name", requestData.getString("name"));
            parameter.put("content", requestData.getString("content"));

            parameter.put("startDate", requestData.getString("startDate"));
            parameter.put("endDate", requestData.getString("endDate"));
            int userId = (int) session.getAttribute("userId");
            parameter.put("userId", String.valueOf(userId));

        }else{
            response.put("code","999");
            response.put("message","Parameter Invalid");
            response.put("msg","파라미터 누락");

            return ResponseEntity.ok(response.toString());
        }

        //유저 정보 업데이트
        ObjectMapper mapper = new ObjectMapper();

        JsonNode root = null;
        try {
            root = mapper.readTree(requestData.toString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        JsonNode arrayNode = root.get("memberList");

        List<UserVo> memberList = mapper.convertValue(
                arrayNode,
                new TypeReference<List<UserVo>>() {}
        );

        log.info("memberList: {}", memberList);

        //프로젝트 추가
        boardService.insertBoardInfo(parameter);

        if(parameter.get("id") != null){
            BigInteger boardIdTemp = (BigInteger) parameter.get("id");
            int boardId = boardIdTemp.intValue();
            log.info("boardId: {}", boardId);
            //멤버 추가
            //멤버 정보 바꾸기
            boardService.updateProjectMemberList(boardId, memberList);
        }



        logger.info("response: {}", response);

        return ResponseEntity.ok(response.toString());

    }

    @RequestMapping(method = RequestMethod.POST, value = "/list")
    public ResponseEntity<String> boardList(@RequestBody String data, HttpServletRequest request){

        JSONObject response = new JSONObject();
        response.put("code","200");
        response.put("message","SUCCESS");
        response.put("msg","성공");

        JSONObject requestData = new JSONObject(data);
        logger.info("parameter: {}", data.toString());
        logger.info("parameter requestData: {}", requestData.toString());

        Map<String, Object> parameter = new HashMap<>();

        HttpSession session = request.getSession();
        Boolean isAdminB = (Boolean) session.getAttribute("isAdmin");
        //관리자 아닐경우 그냥 리턴
        if(!isAdminB){
            logger.info("관리자가 아닙니다.");
            return ResponseEntity.ok(response.toString());
        }


        int size = 10;
        int page = 1;

        if(requestData.has("page")){
            page = requestData.getInt("page");
        }

        if(requestData.has("size")){
            size = requestData.getInt("size");
        }

        if(requestData.has("name")){
            parameter.put("name", requestData.getString("name"));
        }

        int pageSize = size <= 0 ? 10 : size;
        int pageNo = page <= 0 ? 1 : page;
        int offset = (pageNo - 1) * pageSize;

        parameter.put("size", size);
        parameter.put("offset", offset);

        List<BoardVo> boardList = boardService.getBoardList(parameter);
        int totalCount = boardService.boardListTotalCount(parameter);

        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        JSONArray boardListJson = new JSONArray(boardList);
        response.put("list", boardListJson);
        response.put("page", pageNo);
        response.put("size", pageSize);
        response.put("totalCount", totalCount);
        response.put("totalPages", totalPages);


        logger.info("response: {}", response);

        return ResponseEntity.ok(response.toString());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/info")
    public ResponseEntity<String> boardInfo(@RequestBody String data){

        JSONObject response = new JSONObject();
        response.put("code","200");
        response.put("message","SUCCESS");
        response.put("msg","성공");

        JSONObject requestData = new JSONObject(data);
        logger.info("parameter: {}", data.toString());

        Map<String, String> parameter = new HashMap<>();

        if(requestData.has("boardId")){

            parameter.put("boardId", String.valueOf(requestData.getInt("boardId")));

        }else{
            response.put("code","999");
            response.put("message","Parameter Invalid");
            response.put("msg","파라미터 누락");

            return ResponseEntity.ok(response.toString());
        }

        //프로젝트 정보 Response 추가
        BoardVo boardInfo = boardService.getBoardInfo(parameter);
        JSONObject boardInfoJson =  new JSONObject(boardInfo);
        response.put("boardInfo", boardInfoJson);

        //유저 리스트 Response 추가
        List<UserVo> userList = boardService.getUserListForProject(parameter);
        JSONArray userListJson = new JSONArray(userList);
        response.put("userList", userListJson);


        logger.info("response: {}", response);

        return ResponseEntity.ok(response.toString());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/update")
    public ResponseEntity<String> updateBoard(@RequestBody String data){

        JSONObject response = new JSONObject();
        response.put("code","200");
        response.put("message","SUCCESS");
        response.put("msg","성공");

        JSONObject requestData = new JSONObject(data);
        logger.info("parameter: {}", data.toString());

        Map<String, String> parameter = new HashMap<>();
        String boardId = "";
        if(requestData.has("boardId")
                && requestData.has("name")
                && requestData.has("content")
                && requestData.has("startDate")
                && requestData.has("endDate")){

            boardId = requestData.getString("boardId");

            parameter.put("boardId", boardId);
            parameter.put("name", requestData.getString("name"));
            parameter.put("content", requestData.getString("content"));

            String startDate = requestData.getString("startDate").substring(0, 10);
            String endDate = requestData.getString("endDate").substring(0, 10);
            startDate += " 00:00:00";
            endDate += " 23:59:59";
            parameter.put("startDate", startDate);
            parameter.put("endDate", endDate);

        }else{
            response.put("code","999");
            response.put("message","Parameter Invalid");
            response.put("msg","파라미터 누락");

            return ResponseEntity.ok(response.toString());
        }
        log.info("update parameter: {}", parameter);

        //유저 정보 업데이트
        ObjectMapper mapper = new ObjectMapper();

        JsonNode root = null;
        try {
            root = mapper.readTree(requestData.toString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        JsonNode arrayNode = root.get("memberList");

        List<UserVo> memberList = mapper.convertValue(
                arrayNode,
                new TypeReference<List<UserVo>>() {}
        );

        log.info("memberList: {}", memberList);

        //게시물 정보 바꾸기
        boardService.updateBoardInfo(parameter);
        //멤버 정보 바꾸기
        boardService.updateProjectMemberList(Integer.parseInt(boardId), memberList);

        logger.info("response: {}", response);

        return ResponseEntity.ok(response.toString());

    }

    @RequestMapping(method = RequestMethod.POST, value = "/alluser")
    public ResponseEntity<String> getAllUser(@RequestBody String data){

        JSONObject response = new JSONObject();
        response.put("code","200");
        response.put("message","SUCCESS");
        response.put("msg","성공");

        JSONObject requestData = new JSONObject(data);
        logger.info("parameter: {}", data.toString());

        Map<String, String> parameter = new HashMap<>();

        //전체 유저 리스트 Response 추가
        List<UserVo> userList = boardService.getUserListAll(parameter);
        JSONArray userListJson = new JSONArray(userList);
        response.put("userAllList", userListJson);


        logger.info("response: {}", response);

        return ResponseEntity.ok(response.toString());
    }

    //캘린더 이동으로 빠른 날짜 수정
    @RequestMapping(method = RequestMethod.POST, value = "/quick_update")
    public ResponseEntity<String> boardQuickUpdate(@RequestBody String data){

        JSONObject response = new JSONObject();
        response.put("code","200");
        response.put("message","SUCCESS");
        response.put("msg","성공");

        JSONObject requestData = new JSONObject(data);
        logger.info("parameter: {}", data.toString());
        logger.info("parameter requestData: {}", requestData.toString());

        Map<String, Object> parameter = new HashMap<>();

        if(requestData.has("boardId")
                && requestData.has("startDate")
                && requestData.has("endDate")){

            int boardId = requestData.getInt("boardId");
            parameter.put("boardId", boardId);

            String startDate = requestData.getString("startDate").substring(0, 10);
            String endDate = requestData.getString("endDate").substring(0, 10);
            startDate += " 00:00:00";
            endDate += " 23:59:59";
            parameter.put("startDate", startDate);
            parameter.put("endDate", endDate);

        }else{
            response.put("code","999");
            response.put("message","Parameter Invalid");
            response.put("msg","파라미터 누락");

            return ResponseEntity.ok(response.toString());
        }

        boardService.boardQuickUpdate(parameter);

        logger.info("response: {}", response);

        return ResponseEntity.ok(response.toString());
    }

}
