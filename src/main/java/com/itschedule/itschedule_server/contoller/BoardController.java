package com.itschedule.itschedule_server.contoller;

import com.itschedule.itschedule_server.service.BoardService;
import com.itschedule.itschedule_server.service.UserService;
import com.itschedule.itschedule_server.utils.ConvertUtils;
import com.itschedule.itschedule_server.vo.BoardVo;
import com.itschedule.itschedule_server.vo.UserVo;
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
    public ResponseEntity<String> insertBoard(@RequestBody String data){

        JSONObject response = new JSONObject();
        response.put("code","200");
        response.put("message","SUCCESS");
        response.put("msg","성공");

        JSONObject requestData = new JSONObject(data);
        logger.info("parameter: {}", data.toString());

        Map<String, String> parameter = new HashMap<>();

        if(requestData.has("name")
                && requestData.has("content")
                && requestData.has("startDate")
                && requestData.has("endDate")){

            parameter.put("name", requestData.getString("name"));
            parameter.put("content", requestData.getString("content"));
            parameter.put("startDate", requestData.getString("startDate"));
            parameter.put("endDate", requestData.getString("endDate"));

        }else{
            response.put("code","999");
            response.put("message","Parameter Invalid");
            response.put("msg","파라미터 누락");

            return ResponseEntity.ok(response.toString());
        }

        boardService.insertBoardInfo(parameter);

        logger.info("response: {}", response);

        return ResponseEntity.ok(response.toString());

    }

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public ResponseEntity<String> boardList(){

        JSONObject response = new JSONObject();
        response.put("code","200");
        response.put("message","SUCCESS");
        response.put("msg","성공");

        Map<String, String> parameter = new HashMap<>();

        List<BoardVo> boardList = boardService.getBoardList(parameter);
        JSONArray boardListJson = new JSONArray(boardList);
        response.put("boardList", boardListJson);


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

        BoardVo boardInfo = boardService.getBoardInfo(parameter);
        JSONObject boardInfoJson =  new JSONObject(boardInfo);
        response.put("boardInfo", boardInfoJson);


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

        if(requestData.has("name")
                && requestData.has("content")
                && requestData.has("startDate")
                && requestData.has("endDate")){

            parameter.put("name", requestData.getString("name"));
            parameter.put("content", requestData.getString("content"));
            parameter.put("startDate", requestData.getString("startDate"));
            parameter.put("endDate", requestData.getString("endDate"));

        }else{
            response.put("code","999");
            response.put("message","Parameter Invalid");
            response.put("msg","파라미터 누락");

            return ResponseEntity.ok(response.toString());
        }

        boardService.updateBoardInfo(parameter);

        logger.info("response: {}", response);

        return ResponseEntity.ok(response.toString());

    }

}
