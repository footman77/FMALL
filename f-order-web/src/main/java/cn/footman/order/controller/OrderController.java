package cn.footman.order.controller;

import cn.footman.cart.service.CartService;

import cn.footman.common.utils.CookieUtils;
import cn.footman.common.utils.FResult;
import cn.footman.order.pojo.OrderInfo;
import cn.footman.order.service.OrderService;
import cn.footman.pojo.TbItem;
import cn.footman.pojo.TbUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class OrderController {

    @Resource
    private CartService cartService;


    @Resource
    private OrderService orderService;

    @RequestMapping("/order/order-cart")
    public String showOrder(HttpServletRequest request){

//        //取用户
//        String jsonUser = CookieUtils.getCookieValue(request, "token");
//        //如果取不到用户，返回登陆

        TbUser user = (TbUser) request.getAttribute("user");


        //取支付方式
        //取地址


        //取购物车列表
        List<TbItem> cartList = cartService.getItemList(user.getId());
        //传递给页面
        request.setAttribute("cartList",cartList);

        return "order-cart";
    }




    @RequestMapping(value = "/order/create",method = RequestMethod.POST)
    public String createOrder(OrderInfo orderInfo,HttpServletRequest request){

        //取用户信息
        TbUser user = (TbUser) request.getAttribute("user");
        orderInfo.setUserId(user.getId());
        orderInfo.setBuyerNick(user.getUsername());
        //调用服务
        FResult order = orderService.createOrder(orderInfo);
        //清空购物车
        cartService.clearItemInCart(user.getId());


        String orderId = (String) order.getData();
        request.setAttribute("orderId",orderId);
        request.setAttribute("payment",orderInfo.getPayment());
        //插入数据库
        return "success";
    }
}
