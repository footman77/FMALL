package cn.footman.cart.interceptor;

import cn.footman.common.jedis.JedisClient;
import cn.footman.common.utils.CookieUtils;
import cn.footman.common.utils.FResult;
import cn.footman.pojo.TbUser;
import cn.footman.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //从cookie中取token，判断是否已经登陆
        String token = CookieUtils.getCookieValue(request, "token");

        //没有登陆，直接放行
        if(StringUtils.isBlank(token)){
            return true;
        }

        //使用tokenService方法获得用户
        FResult fResult = tokenService.getUserByToken(token);
        if(fResult.getStatus() == 200){
            //没有过期，取得用户信息，放入request中，传递到controller
            TbUser user = (TbUser) fResult.getData();
            request.setAttribute("user",user);
            return true;
        }
        //登陆了，查看是否过期
        //过期直接放行
        return true;




    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {

    }
}
