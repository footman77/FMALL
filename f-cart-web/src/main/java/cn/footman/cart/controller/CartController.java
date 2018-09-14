package cn.footman.cart.controller;

import cn.footman.cart.service.CartService;
import cn.footman.common.utils.CookieUtils;
import cn.footman.common.utils.FResult;
import cn.footman.common.utils.JsonUtils;
import cn.footman.pojo.TbItem;
import cn.footman.pojo.TbUser;
import cn.footman.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {


    @Resource
    private ItemService itemService;


    @Value("${COOKIE_ITEM_AGE}")
    private Integer COOKIE_ITEM_AGE;


    @Resource
    private CartService cartService;

    /**
     * 添加购物车
     * @param itemId
     * @return
     */
    @RequestMapping("/cart/add/{itemId}")
    public String addItemToCart(@PathVariable Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response){
        //从拦截器中获得user
        TbUser user = (TbUser) request.getAttribute("user");
        if(user != null){
            //登陆状态下，将数据存到redis中
            cartService.addItemToRedis(itemId,user.getId(),num);
            return "cartSuccess";
        }

        //查询cookie中是否有该商品，有的话数量相加，没有就添加
        List<TbItem> itemList = getItemListFromCookie(request);
        //标志是否进入循环中的if
        boolean flag = false;
        if(itemList.size() > 0){
            for(TbItem item : itemList){
                if(item.getId() == itemId.longValue()){
                    item.setNum(item.getNum() + num);
                    flag = true;//在商品列表中有这次添加的商品，把数量相加
                    break;
                }
            }

        }
        //商品列表中没有这次添加的商品
        if(!flag){
            //从数据库中根据id查找商品数据
            TbItem item = itemService.getItemById(itemId);
            item.setNum(num);
            String image = item.getImage();
            if(StringUtils.isNotBlank(image)){
                item.setImage(image.split(",")[0]);
            }

            //添加到商品列表中
            itemList.add(item);
        }
        //在把商品添加完之后，写入到cookie中
        CookieUtils.setCookie(request,response,"cart",JsonUtils.objectToJson(itemList),COOKIE_ITEM_AGE,true);

        return "cartSuccess";
    }






    //展示购物车列表
    @RequestMapping("/cart/cart")
    public String showCart(HttpServletRequest request,HttpServletResponse response){
        //从cookie中查找购物车列表
        List<TbItem> cartList = getItemListFromCookie(request);
        //判断是否已经登陆
        TbUser user = (TbUser) request.getAttribute("user");

        if(user != null){
            //已经登陆
            //从cookie中获得商品列表

            //从redis中取出商品，对比
            // 与redis中的商品合并
            cartService.mergeCart(cartList,user.getId());
//            将cookie中的商品删除
            CookieUtils.deleteCookie(request,response,"cart");
            //从redis中取出商品列表

            cartList = cartService.getItemList(user.getId());


        }

        //没有登陆

        //将数据回显
        request.setAttribute("cartList",cartList);
        return "cart";
    }


    //调整商品的数量
    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public FResult updateCartItemNum(@PathVariable Long itemId,@PathVariable Integer num,
                                     HttpServletRequest request,HttpServletResponse response){

        //从拦截器中获得user
        TbUser user = (TbUser) request.getAttribute("user");
        if(user != null){
          //已经登陆
            cartService.updateCart(itemId, user.getId(), num);
            return FResult.ok();
        }

        //从cookie中得到商品列表
        List<TbItem> cartList = getItemListFromCookie(request);
        for(TbItem item : cartList){
            if(item.getId() == itemId.longValue()){
                item.setNum(num);
                break;
            }
        }
        //重新写入cookie
        CookieUtils.setCookie(request,response,"cart",JsonUtils.objectToJson(cartList),COOKIE_ITEM_AGE,true);
        return FResult.ok();
    }


    //删除购物车中的商品
    @RequestMapping("/cart/delete/{itemId}")

    public String deleteItemByItemId(@PathVariable Long itemId,HttpServletRequest request,HttpServletResponse response){

        //从拦截器中获得user
        TbUser user = (TbUser) request.getAttribute("user");
        if(user != null){
            //已经登陆
            cartService.deleteItemInCart(itemId, user.getId());
            return "redirect:/cart/cart.html";

        }

        //从cookie中得到商品列表
        List<TbItem> cartList = getItemListFromCookie(request);
        for(TbItem item : cartList){
            if(item.getId() == itemId.longValue()){
               cartList.remove(item);
               break;
            }
        }
        //重新写入cookie
        CookieUtils.setCookie(request,response,"cart",JsonUtils.objectToJson(cartList),COOKIE_ITEM_AGE,true);
        return "redirect:/cart/cart.html";

    }




















    /**
     * 获得cookie中商品列表的方法
     */
    private List<TbItem> getItemListFromCookie(HttpServletRequest request){
        String cart = CookieUtils.getCookieValue(request, "cart", true);
        if(StringUtils.isBlank(cart)){
            //如果在cookie中没有找到商品信息返回一个空的数组
            return  new ArrayList<>();
        }
        //找到了，则转换成商品list
        List<TbItem> tbItems = JsonUtils.jsonToList(cart, TbItem.class);
        return tbItems;
    }

}
