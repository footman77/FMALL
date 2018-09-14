package cn.footman.cart.service;

import cn.footman.common.utils.FResult;
import cn.footman.pojo.TbItem;


import java.util.List;

public interface CartService {

    public FResult addItemToRedis(long itemId,long userId,int num);

    public FResult mergeCart(List<TbItem> tbItems,long userId);

    public List<TbItem> getItemList(long userId);

    FResult updateCart(long itemId,long userId,int num);

    FResult deleteItemInCart(long itemId,long userId);

    FResult clearItemInCart(long userId);
}
