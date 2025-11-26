package com.itschedule.itschedule_server.contoller;

import com.itschedule.itschedule_server.service.BoardService;
import com.itschedule.itschedule_server.service.UserService;
import com.itschedule.itschedule_server.utils.ConvertUtils;
import com.itschedule.itschedule_server.vo.UserVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/schedule")
public class ApiController {
    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    private static UserService userService = null;
    private static final ApiController _instance = new ApiController(userService);

    public ApiController(UserService userService) {

        ApiController.userService = userService;
    }

    public static ApiController getInstance() {
        return _instance;
    }

    //Login
    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity<String> getUserInfoForLogin(@RequestBody String data,
                                                      HttpServletRequest request){
        logger.info("test");

        JSONObject response = new JSONObject();
        response.put("code","200");
        response.put("message","SUCCESS");
        response.put("msg","성공");
        response.put("data","");

        JSONObject requestData = new JSONObject(data);
        logger.info("parameter: {}", data.toString());

        Map<String, String> parameter = new HashMap<>();
        String passwordPlain = "";

        if(requestData.has("email") && requestData.has("password")){

            parameter.put("email", requestData.getString("email"));
//            parameter.put("password", requestData.getString("password"));
            passwordPlain = requestData.getString("password");

        }else{
            response.put("code","999");
            response.put("message","Parameter Invalid");
            response.put("msg","파라미터 누락");

            return ResponseEntity.ok(response.toString());
        }

        UserVo userInfo = userService.getUserInfoForLogin(parameter);
        String userInfoString = "";

        if(userInfo != null) {
            if(ConvertUtils.checkPassword(passwordPlain, userInfo.getPassword())) {
                userInfoString = ConvertUtils.userVoToJson(userInfo);
                //로그인 성공
                //세션 처리
                HttpSession session = request.getSession();
                session.setAttribute("email", userInfo.getEmail());
                boolean isAdmin = userInfo.getIsAdmin() > 0;
                session.setAttribute("isAdmin", isAdmin);
            }else{
                response.put("code","202");
                response.put("message","Invalid password");
                response.put("msg","로그인 실패 비밀번호 틀림");
            }
        }else{
            response.put("code","201");
            response.put("message","Invalid Information");
            response.put("msg","로그인 실패");
        }

        response.put("data",userInfoString);

        logger.info("response: {}", response);

        return ResponseEntity.ok(response.toString());
    }

    //Logout
    @RequestMapping(method = RequestMethod.POST, value = "/logout")
    public void logout(HttpServletRequest request){

        //세션 만료 처리
        HttpSession session = request.getSession();
        session.invalidate();
        log.info("session invalid");
    }

    //react -> spring session 체크
    @RequestMapping(method = RequestMethod.GET, value = "/me")
    public ResponseEntity<?> me(HttpServletRequest request) {
        log.info("me check");
        HttpSession session = request.getSession(false);
        if (session == null) {
            return ResponseEntity.status(401).body(Map.of("authenticated", false));
        }


        String email = (String) session.getAttribute("email");
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");

        return ResponseEntity.ok(Map.of(
                "authenticated", true,
                "email", email,
                "isAdmin", isAdmin != null && isAdmin
        ));
    }

    //signup
    @RequestMapping(method = RequestMethod.POST, value = "/signup")
    public ResponseEntity<String> signup(@RequestBody String data){

        JSONObject response = new JSONObject();
        response.put("code","200");
        response.put("message","SUCCESS");
        response.put("msg","성공");

        JSONObject requestData = new JSONObject(data);
        logger.info("parameter: {}", data.toString());

        Map<String, String> parameter = new HashMap<>();

        if(requestData.has("name")
            && requestData.has("email")
            && requestData.has("password")){

            String hashPassword = ConvertUtils.hashPassword(requestData.getString("password"));

            parameter.put("name", requestData.getString("name"));
            parameter.put("email", requestData.getString("email"));
            parameter.put("password", hashPassword);

        }else{
            response.put("code","999");
            response.put("message","Parameter Invalid");
            response.put("msg","파라미터 누락");

            return ResponseEntity.ok(response.toString());
        }

        UserVo userInfo = userService.getUserInfo(parameter);

        //유저 정보가 없다면 Insert 진행
        if(userInfo == null){
            userService.insertUser(parameter);
        }else{
            response.put("code","201");
            response.put("message","Invalid Information");
            response.put("msg","이미 존재하는 ID 입니다.");
        }

        logger.info("response: {}", response);

        return ResponseEntity.ok(response.toString());

    }

}
