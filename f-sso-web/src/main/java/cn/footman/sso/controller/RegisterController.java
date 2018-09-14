package cn.footman.sso.controller;

import cn.footman.common.utils.FResult;
import cn.footman.pojo.TbUser;
import cn.footman.sso.service.RegisterService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class RegisterController {

    @Resource
    private RegisterService registerService;


    @RequestMapping("/page/register")
    public String showRegister(){
        return "register";
    }


    //校验数据
    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public FResult checkData(@PathVariable String param,@PathVariable Integer type){
        return registerService.checkData(param,type);
    }

    //提交用户数据
    @RequestMapping(value = "/user/register",method = RequestMethod.POST)
    @ResponseBody
    public FResult register(TbUser tbUser){
        return registerService.saveUser(tbUser);

    }

}
