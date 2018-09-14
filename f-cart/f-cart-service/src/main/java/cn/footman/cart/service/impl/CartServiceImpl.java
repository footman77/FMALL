package cn.footman.cart.service.impl;

import cn.footman.cart.service.CartService;


import cn.footman.common.jedis.JedisClient;
import cn.footman.common.utils.CookieUtils;
import cn.footman.common.utils.FResult;
import cn.footman.common.utils.JsonUtils;
import cn.footman.mapper.TbItemMapper;
import cn.footman.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private JedisClient jedisClient;

    @Value("${REDIS_CART_PRE}")
    private String REDIS_CART_PRE;

    @Autowired
    private TbItemMapper tbItemMapper;
    @Override
    public FResult addItemToRedis(long itemId, long userId, int num) {
        //判断redis中是否已经有该商品，有了的话就数量相加
        Boolean hexists = jedisClient.hexists(REDIS_CART_PRE + ":" + userId, itemId + "");
        if(hexists){
            //在redis中已经有该商品
            //取出商品
            String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
            TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
            //数量相加
            item.setNum(item.getNum() + num);
            //写入redis
            jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "",JsonUtils.objectToJson(item));
            return FResult.ok();
        }
        //没有该商品，新添加到购物车
        //根据itemId,在数据库中查找该商品
        TbItem item = tbItemMapper.selectByPrimaryKey(itemId);
        item.setNum(num);
        if(StringUtils.isNotBlank(item.getImage())){
            item.setImage(item.getImage().split(",")[0]);
        }
        //写入redis
        jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "",JsonUtils.objectToJson(item));

        return FResult.ok();
    }

    //cookie合并到redis
    @Override
    public FResult mergeCart(List<TbItem> tbItems, long userId) {

        for(TbItem item : tbItems){
            addItemToRedis(item.getId(),userId,item.getNum());
        }

        return FResult.ok();
    }

    //从redis中获得所有的商品信息
    @Override
    public List<TbItem> getItemList(long userId) {
        List<String> jsonList = jedisClient.hvals(REDIS_CART_PRE + ":" + userId);
        ArrayList<TbItem> itemList = new ArrayList<>();
        for(String json : jsonList){
            TbItem item = JsonUtils.jsonToPojo(json,TbItem.class);
            itemList.add(item);
        }
        return itemList;


    }

    //更新数量
    @Override
    public FResult updateCart(long itemId, long userId, int num) {
        String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
        TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
        item.setNum(num);
        jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "",JsonUtils.objectToJson(item));
        return FResult.ok();
    }

    //删除一件商品
    @Override
    public FResult deleteItemInCart(long itemId, long userId) {
        jedisClient.hdel(REDIS_CART_PRE + ":" + userId, itemId + "");

        return FResult.ok();
    }

    //清空购物车
    @Override
    public FResult clearItemInCart(long userId) {
        jedisClient.del(REDIS_CART_PRE + ":" + userId);
        return FResult.ok();
    }


}
