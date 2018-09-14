package cn.footman.sso.controller;

import cn.footman.common.utils.CookieUtils;
import cn.footman.common.utils.FResult;
import cn.footman.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户登陆处理
 */
@Controller
public class LoginController {


    @Resource
    private LoginService loginService;

    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;

    @RequestMapping("/page/login")
    public String showLogin(String redirect,Model model){
        model.addAttribute("redirect",redirect);
        return "login";
    }

    @RequestMapping(value = "/user/login",method = RequestMethod.POST)
    @ResponseBody
    public FResult login(String username, String password, HttpServletRequest request, HttpServletResponse response){
        FResult fResult = loginService.login(username, password);
        //判断是否登陆成功
        if(fResult.getStatus() == 200){
            String token = fResult.getData().toString();
            //将token写入cookie
            CookieUtils.setCookie(request,response,TOKEN_KEY,token);
        }
        return fResult;

    }

}
