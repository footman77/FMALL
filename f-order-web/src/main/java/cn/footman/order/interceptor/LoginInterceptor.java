package cn.footman.order.interceptor;

import cn.footman.cart.service.CartService;
import cn.footman.common.jedis.JedisClient;
import cn.footman.common.utils.CookieUtils;
import cn.footman.common.utils.FResult;
import cn.footman.common.utils.JsonUtils;
import cn.footman.pojo.TbItem;
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

    @Resource
    private CartService cartService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        //从cookie中取token，判断用户是否登陆
        String jsonUser = CookieUtils.getCookieValue(request, "token");
        if(StringUtils.isBlank(jsonUser)){
            //如果token是空，没有登陆，就返回登陆页面
            response.sendRedirect("http://localhost:8089/page/login?redirect=" + request.getRequestURL());
            //拦截
            return false;
        }
        //如果有值，去redis中取用户信息
        FResult result = tokenService.getUserByToken(jsonUser);
        if(result.getStatus() != 200){
            //如果token是空，登陆信息过期，就返回登陆页面
            response.sendRedirect("http://localhost:8089/page/login?redirect=" + request.getRequestURL());
            //拦截
            return false;
        }

        //登陆状态，把数据放入request中
        TbUser user = (TbUser) result.getData();
        request.setAttribute("user",user);

        //判断cookie中是否有购物车数据
        String cart = CookieUtils.getCookieValue(request, "cart", true);
        if(StringUtils.isNotBlank(cart)){
            //cookie中有数据，需要先合并到redis中
            cartService.mergeCart(JsonUtils.jsonToList(cart,TbItem.class),user.getId());
            //删除cookie中的数据
            CookieUtils.deleteCookie(request,response,"cart");
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {

    }
}
