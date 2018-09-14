package cn.footman.sso.controller;

import cn.footman.common.utils.FResult;
import cn.footman.common.utils.JsonUtils;
import cn.footman.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.awt.*;

@Controller
public class TokenController {


    @Resource
    private TokenService tokenService;

    //这种方式是可行的
//    @RequestMapping(value = "/user/token/{token}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE/*"application/json;charset=utf-8"*/)
//    @ResponseBody
//    public String getUserByToken(@PathVariable String token, String callback){
//        FResult fResult = tokenService.getUserByToken(token);
//
//        //如果有callback
//        if(StringUtils.isNotBlank(callback)){
////            把结果封装成js响应
//           return callback + "(" + JsonUtils.objectToJson(fResult) + ")";
//        }
//        return JsonUtils.objectToJson(fResult);
//    }

    //spring4.1 以后，可以使用这种方式
    @RequestMapping(value = "/user/token/{token}")
    @ResponseBody
    public Object getUserByToken(@PathVariable String token, String callback){
        FResult fResult = tokenService.getUserByToken(token);

        //如果有callback
        if(StringUtils.isNotBlank(callback)){
//
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(fResult);
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
        }
        return fResult;
    }
}
