package com.onlyedu.newclasses.modules.permission.controller;

import com.onlyedu.newclasses.model.User;
import com.onlyedu.newclasses.util.RestResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Andy
 * @date 2018/12/5 9:24
 */

@RestController
@RequestMapping("newclassAdminServer")
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @PostMapping("login")
    public RestResult login(@RequestBody @Valid User user, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            RestResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(),user.getPassWord());
        Subject subject = SecurityUtils.getSubject();

        try{
            subject.login(token);
        }catch (Exception e){
            return RestResult.fail("用户名或密码错误");
        }
        return RestResult.success();
    }

    @GetMapping("logout")
    public RestResult logout(){

        try{
            SecurityUtils.getSubject().logout();
        }catch (Exception e){

            logger.error("登出失败：{}",e.fillInStackTrace());
            return RestResult.fail("登出失败");
        }
        return RestResult.success();
    }

    @RequestMapping("unauthor")
    public RestResult unauthor(){
        return RestResult.unauthor();
    }

    @RequestMapping("unlogin")
    public RestResult unlogin(){
        return RestResult.unLogin();
    }
}
