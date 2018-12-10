package com.onlyedu.newclasses.testModule.controller;

import com.onlyedu.newclasses.model.User;
import com.onlyedu.newclasses.util.RestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Andy
 * @date 2018/11/20 13:52
 */

@RestController
public class TestController {

    private Logger logger = LoggerFactory.getLogger(TestController.class);

    @RequestMapping("test")
    public String index(){
        return "后台项目1";
    }


    @RequestMapping("testSchedule")
    public RestResult testSchedule(@RequestBody User user, HttpServletResponse response){

        response.setHeader("Content-Type","application/json;charset=UTF-8");

        System.out.println("testSchedule"+user);
        return RestResult.success(user);
    }

    @RequestMapping("testSchedule2")
    public RestResult testSchedule2(){
        System.out.println("testSchedule2");
        return RestResult.success();
    }

    @RequestMapping("testLog")
    public RestResult testLog(){

        logger.debug("test debug");
        logger.info("test info");
        logger.warn("test warn");
        logger.error("test error");
        return RestResult.success();
    }


    @RequestMapping("getSessionAttribute")
    public RestResult getSessionAttribute(HttpServletRequest request){

        try{

            HttpSession session = request.getSession();
            Map<String,Object> attributes = new HashMap<>();
            Enumeration<String> keys = session.getAttributeNames();
            while (keys.hasMoreElements()){
                String key =  keys.nextElement();
                Object value = session.getAttribute(key);
                attributes.put(key,value);
            }

            return RestResult.success(attributes);
        }catch (Exception e){

            return RestResult.fail(e.getMessage());
        }



    }

    @RequestMapping("setSessionAttribute")
    public RestResult getSessionAttribute(String key,String value,HttpServletRequest request){

        try{
            HttpSession session = request.getSession();
            session.setAttribute(key,value);
        }catch (Exception e){
            return RestResult.fail(e.getMessage());
        }

        return RestResult.success();

    }

}
