package com.itschedule.itschedule_server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itschedule.itschedule_server.enums.AlertMessages;
import com.itschedule.itschedule_server.service.IssueService;
import com.itschedule.itschedule_server.service.UserService;
import com.itschedule.itschedule_server.utils.ConvertUtils;
import com.itschedule.itschedule_server.vo.BoardVo;
import com.itschedule.itschedule_server.vo.IssueVo;
import com.itschedule.itschedule_server.vo.ProjectWithMemberVo;
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
@RequestMapping("/schedule/issue")
public class IssueController {

    private static final Logger logger = LoggerFactory.getLogger(IssueController.class);

    private static IssueService issueService = null;
    private static UserService userService = null;

    private static final IssueController _instance = new IssueController(issueService, userService);

    public IssueController(IssueService issueService, UserService userService) {

        IssueController.issueService = issueService;
        IssueController.userService = userService;
    }

    public static IssueController getInstance() {
        return _instance;
    }

    //이슈 리스트
    @RequestMapping(method = RequestMethod.POST, value = "/list")
    public ResponseEntity<String> issueList(@RequestBody String data, HttpServletRequest request){

        JSONObject response = new JSONObject();
        response.put("code","200");
        response.put("message","SUCCESS");
        response.put("msg","성공");

        JSONObject requestData = new JSONObject(data);
        logger.info("parameter: {}", data.toString());
        logger.info("parameter requestData: {}", requestData.toString());


        HttpSession session = request.getSession();
        int userId = (int) session.getAttribute("userId");
        Boolean isAdminB = (Boolean) session.getAttribute("isAdmin");
        int isAdmin = isAdminB ? 1 : 0;

        Map<String, Object> parameter = new HashMap<>();

        parameter.put("userId", userId);
        parameter.put("isAdmin", isAdmin);

        int size = 10;
        int page = 1;

        if(requestData.has("page")){
            page = requestData.getInt("page");
        }

        if(requestData.has("size")){
            size = requestData.getInt("size");
        }

        if(requestData.has("projectName")){
            parameter.put("projectName", requestData.getString("projectName"));
        }

        int pageSize = size <= 0 ? 10 : size;
        int pageNo = page <= 0 ? 1 : page;
        int offset = (pageNo - 1) * pageSize;

        parameter.put("size", size);
        parameter.put("offset", offset);

        List<IssueVo> issueList = issueService.getIssueList(parameter);
        int totalCount = issueService.issueListTotalCount(parameter);

        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        JSONArray issueListJson = new JSONArray(issueList);
        response.put("list", issueListJson);
        response.put("page", pageNo);
        response.put("size", pageSize);
        response.put("totalCount", totalCount);
        response.put("totalPages", totalPages);


        logger.info("response: {}", response);

        return ResponseEntity.ok(response.toString());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/info")
    public ResponseEntity<String> issueInfo(@RequestBody String data){

        JSONObject response = new JSONObject();
        response.put("code","200");
        response.put("message","SUCCESS");
        response.put("msg","성공");

        JSONObject requestData = new JSONObject(data);
        logger.info("parameter: {}", data.toString());

        Map<String, Object> parameter = new HashMap<>();

        if(requestData.has("issueId")){

            parameter.put("issueId", String.valueOf(requestData.getInt("issueId")));

        }else{
            response.put("code","999");
            response.put("message","Parameter Invalid");
            response.put("msg","파라미터 누락");

            return ResponseEntity.ok(response.toString());
        }

        //프로젝트 정보 Response 추가
        IssueVo issueInfo = issueService.getIssueInfo(parameter);
        JSONObject issueInfoJson =  new JSONObject(issueInfo);
        response.put("issueInfo", issueInfoJson);

        //유저 리스트 Response 추가
        List<UserVo> userList = issueService.getUserListForProject(parameter);
        JSONArray userListJson = new JSONArray(userList);
        response.put("userList", userListJson);


        logger.info("response: {}", response);

        return ResponseEntity.ok(response.toString());
    }

    //프로젝트 리스트
    @RequestMapping(method = RequestMethod.POST, value = "/projectList")
    public ResponseEntity<String> projectList(@RequestBody String data, HttpServletRequest request){

        JSONObject response = new JSONObject();
        response.put("code","200");
        response.put("message","SUCCESS");
        response.put("msg","성공");

        JSONObject requestData = new JSONObject(data);
        logger.info("parameter: {}", data.toString());

        HttpSession session = request.getSession();
        int userId = (int) session.getAttribute("userId");
        Boolean isAdminB = (Boolean) session.getAttribute("isAdmin");
        int isAdmin = isAdminB ? 1 : 0;

        Map<String, Object> parameter = new HashMap<>();

        parameter.put("userId", userId);
        parameter.put("isAdmin", isAdmin);

        List<BoardVo> projectList = issueService.getProjectListWithMember(parameter);

        List<ProjectWithMemberVo> resultList = ConvertUtils.convertProjectForMember(projectList);

        JSONArray projectListJson = new JSONArray(resultList);
        response.put("projectList", projectListJson);

        logger.info("response: {}", response);

        return ResponseEntity.ok(response.toString());
    }

    //멤버 리스트
    @RequestMapping(method = RequestMethod.POST, value = "/memberList")
    public ResponseEntity<String> memberList(@RequestBody String data, HttpServletRequest request){

        JSONObject response = new JSONObject();
        response.put("code","200");
        response.put("message","SUCCESS");
        response.put("msg","성공");

        JSONObject requestData = new JSONObject(data);
        logger.info("parameter: {}", data.toString());
        logger.info("parameter requestData: {}", requestData.toString());


        HttpSession session = request.getSession();
        int userId = (int) session.getAttribute("userId");
        Boolean isAdminB = (Boolean) session.getAttribute("isAdmin");
        int isAdmin = isAdminB ? 1 : 0;

        Map<String, Object> parameter = new HashMap<>();

        if(requestData.has("projectId")){
            int projectId = requestData.getInt("projectId");
            parameter.put("projectId", String.valueOf(projectId));
        }else{
            response.put("code","999");
            response.put("message","Parameter Invalid");
            response.put("msg","파라미터 누락");

            return ResponseEntity.ok(response.toString());
        }

        //프로젝트에 대한 멤버 리스트 가져와야함 (해당 부분 아직 처리안됨)
        List<UserVo> userList = issueService.getUserListForProject(parameter);
        JSONArray userListJson = new JSONArray(userList);
        response.put("memberList", userListJson);

        logger.info("response: {}", response);

        return ResponseEntity.ok(response.toString());
    }

    //프로젝트 추가
    @RequestMapping(method = RequestMethod.POST, value = "/insert")
    public ResponseEntity<String> insertIssue(@RequestBody String data, HttpServletRequest request){

        JSONObject response = new JSONObject();
        response.put("code","200");
        response.put("message","SUCCESS");
        response.put("msg","성공");

        JSONObject requestData = new JSONObject(data);
        logger.info("parameter: {}", data.toString());

        Map<String, Object> parameter = new HashMap<>();

        HttpSession session = request.getSession();

        if(requestData.has("name")
                && requestData.has("projectId")
                && requestData.has("issueType")
                && requestData.has("priority")
                && requestData.has("content")
                && requestData.has("assigneeId")
                && requestData.has("startDate")
                && requestData.has("endDate")){

            parameter.put("name", requestData.getString("name"));
            parameter.put("projectId", requestData.getInt("projectId"));
            parameter.put("issueType", requestData.getInt("issueType"));
            parameter.put("priority", requestData.getInt("priority"));
            parameter.put("content", requestData.getString("content"));
            parameter.put("assigneeId", requestData.getString("assigneeId"));
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

        //이슈 추가
        issueService.insertIssueInfo(parameter);

        BigInteger boardIdTemp = (BigInteger) parameter.get("id");
        int issueId = boardIdTemp.intValue();
        log.info("issueId: {}", issueId);

        //유저 알림 추가
        parameter.put("userId", requestData.getString("assigneeId"));
        parameter.put("content", AlertMessages.ISSUE_ADD.getSentence());
        parameter.put("type", AlertMessages.ISSUE_ADD.getMsgType());
        parameter.put("targetId", issueId);
        userService.insertUserEvent(parameter);

        logger.info("response: {}", response);

        return ResponseEntity.ok(response.toString());

    }

    @RequestMapping(method = RequestMethod.POST, value = "/update")
    public ResponseEntity<String> updateIssue(@RequestBody String data){

        JSONObject response = new JSONObject();
        response.put("code","200");
        response.put("message","SUCCESS");
        response.put("msg","성공");

        JSONObject requestData = new JSONObject(data);
        logger.info("parameter: {}", data.toString());

        Map<String, Object> parameter = new HashMap<>();

        if(requestData.has("issueId")
            && requestData.has("name")
            && requestData.has("issueType")
            && requestData.has("issuePriority")
            && requestData.has("issueStatus")
            && requestData.has("content")
            && requestData.has("assigneeId")
            && requestData.has("startDate")
            && requestData.has("endDate")){

            int issueId = requestData.getInt("issueId");

            parameter.put("issueId", issueId);
            parameter.put("name", requestData.getString("name"));
            parameter.put("issueType", requestData.getInt("issueType"));
            parameter.put("issuePriority", requestData.getInt("issuePriority"));
            parameter.put("issueStatus", requestData.getInt("issueStatus"));
            parameter.put("content", requestData.getString("content"));
            parameter.put("assigneeId", requestData.getInt("assigneeId"));

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

        //게시물 정보 바꾸기
        issueService.updateIssueInfo(parameter);

        logger.info("response: {}", response);

        return ResponseEntity.ok(response.toString());

    }

    //캘린더 이동으로 빠른 날짜 수정
    @RequestMapping(method = RequestMethod.POST, value = "/quick_update")
    public ResponseEntity<String> issueQuickUpdate(@RequestBody String data){

        log.info("issue quick_update");
        JSONObject response = new JSONObject();
        response.put("code","200");
        response.put("message","SUCCESS");
        response.put("msg","성공");

        JSONObject requestData = new JSONObject(data);
        logger.info("parameter: {}", data.toString());
        logger.info("parameter requestData: {}", requestData.toString());

        Map<String, Object> parameter = new HashMap<>();

        if(requestData.has("issueId")
                && requestData.has("startDate")
                && requestData.has("endDate")){

            int issueId = requestData.getInt("issueId");
            parameter.put("issueId", issueId);

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

        issueService.issueQuickUpdate(parameter);

        IssueVo issueInfo = issueService.getIssueInfo(parameter);

        //유저 알림 추가
        parameter.put("userId", issueInfo.getManagerUserId());
        parameter.put("content", AlertMessages.CHANGE_ISSUE_DATE.getSentence());
        parameter.put("type", AlertMessages.CHANGE_ISSUE_DATE.getMsgType());
        parameter.put("targetId", requestData.getInt("issueId"));
        userService.insertUserEvent(parameter);

        logger.info("response: {}", response);

        return ResponseEntity.ok(response.toString());
    }

    //Todo 리스트
    @RequestMapping(method = RequestMethod.POST, value = "/todo_list")
    public ResponseEntity<String> issueTodoList(@RequestBody String data, HttpServletRequest request){

        JSONObject response = new JSONObject();
        response.put("code","200");
        response.put("message","SUCCESS");
        response.put("msg","성공");

        JSONObject requestData = new JSONObject(data);
        logger.info("parameter: {}", data.toString());
        logger.info("parameter requestData: {}", requestData.toString());


        HttpSession session = request.getSession();
        int userId = (int) session.getAttribute("userId");
        Boolean isAdminB = (Boolean) session.getAttribute("isAdmin");
        int isAdmin = isAdminB ? 1 : 0;

        Map<String, Object> parameter = new HashMap<>();

        parameter.put("userId", userId);
        parameter.put("isAdmin", isAdmin);

        List<IssueVo> issueList = issueService.getIssueTodoList(parameter);

        JSONArray issueListJson = new JSONArray(issueList);
        response.put("list", issueListJson);

        logger.info("response: {}", response);

        return ResponseEntity.ok(response.toString());
    }

    //TodoList 별 표시 변경
    @RequestMapping(method = RequestMethod.POST, value = "/important_update")
    public ResponseEntity<String> issueImportantUpdate(@RequestBody String data){

        JSONObject response = new JSONObject();
        response.put("code","200");
        response.put("message","SUCCESS");
        response.put("msg","성공");

        JSONObject requestData = new JSONObject(data);
        logger.info("parameter: {}", data.toString());
        logger.info("parameter requestData: {}", requestData.toString());

        Map<String, Object> parameter = new HashMap<>();

        if(requestData.has("issueId")
                && requestData.has("isImportant")){

            int issueId = requestData.getInt("issueId");
            parameter.put("issueId", issueId);

            int isImportant = requestData.getInt("isImportant");
            parameter.put("isImportant", isImportant);

        }else{
            response.put("code","999");
            response.put("message","Parameter Invalid");
            response.put("msg","파라미터 누락");

            return ResponseEntity.ok(response.toString());
        }

        issueService.issueImportantUpdate(parameter);

        logger.info("response: {}", response);

        return ResponseEntity.ok(response.toString());
    }


}
