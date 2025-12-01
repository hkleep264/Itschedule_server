package com.itschedule.itschedule_server.controller;

import com.itschedule.itschedule_server.service.TaskService;
import com.itschedule.itschedule_server.utils.ConvertUtils;
import com.itschedule.itschedule_server.vo.BoardVo;
import com.itschedule.itschedule_server.vo.TaskVo;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*

Task 페이지에 나오는 항목
-> issue 리스트에 내가 담당으로 되어있는 항목 중에서
   진행도에 따라서 준비, 진행, 완료로 나누어서 보여줌
 */
@RestController
@Slf4j
@RequestMapping("/schedule/task")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private static TaskService taskService = null;
    private static final TaskController _instance = new TaskController(taskService);

    public TaskController(TaskService taskService) {

        TaskController.taskService = taskService;
    }

    public static TaskController getInstance() {
        return _instance;
    }

    //이슈 리스트
    @RequestMapping(method = RequestMethod.POST, value = "/list")
    public ResponseEntity<String> taskList(@RequestBody String data, HttpServletRequest request){

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

        List<TaskVo> taskList = taskService.getTaskList(parameter);

        JSONArray taskListJson = new JSONArray(taskList);

        response.put("list", taskListJson);

        logger.info("response: {}", response);

        return ResponseEntity.ok(response.toString());
    }

    //테스크 이동으로 빠른 상태값 빠른 수정
    @RequestMapping(method = RequestMethod.POST, value = "/quick_update")
    public ResponseEntity<String> taskQuickUpdate(@RequestBody String data, HttpServletRequest request){

        JSONObject response = new JSONObject();
        response.put("code","200");
        response.put("message","SUCCESS");
        response.put("msg","성공");

        JSONObject requestData = new JSONObject(data);
        logger.info("parameter: {}", data.toString());
        logger.info("parameter requestData: {}", requestData.toString());


        HttpSession session = request.getSession();
        int userId = (int) session.getAttribute("userId");

        Map<String, Object> parameter = new HashMap<>();

        parameter.put("userId", userId);

        if(requestData.has("issueId")
          && requestData.has("issueStatus")){

            int issueId = requestData.getInt("issueId");
            parameter.put("issueId", issueId);
            parameter.put("issueStatus", requestData.getInt("issueStatus"));

        }else{
            response.put("code","999");
            response.put("message","Parameter Invalid");
            response.put("msg","파라미터 누락");

            return ResponseEntity.ok(response.toString());
        }

        taskService.taskQuickUpdate(parameter);

        logger.info("response: {}", response);

        return ResponseEntity.ok(response.toString());
    }

    //테스크 Detail 에서 수정
    @RequestMapping(method = RequestMethod.POST, value = "/update")
    public ResponseEntity<String> taskUpdate(@RequestBody String data){

        JSONObject response = new JSONObject();
        response.put("code","200");
        response.put("message","SUCCESS");
        response.put("msg","성공");

        JSONObject requestData = new JSONObject(data);
        logger.info("parameter: {}", data.toString());
        logger.info("parameter requestData: {}", requestData.toString());

        Map<String, Object> parameter = new HashMap<>();

        if(requestData.has("issueId")
                && requestData.has("name")
                && requestData.has("issuePriority")
                && requestData.has("issueStatus")
                && requestData.has("content")
                && requestData.has("startDate")
                && requestData.has("endDate")){

            int issueId = requestData.getInt("issueId");

            parameter.put("issueId", issueId);
            parameter.put("name", requestData.getString("name"));
            parameter.put("issuePriority", requestData.getInt("issuePriority"));
            parameter.put("issueStatus", requestData.getInt("issueStatus"));
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

        taskService.taskUpdate(parameter);

        logger.info("response: {}", response);

        return ResponseEntity.ok(response.toString());
    }

}
