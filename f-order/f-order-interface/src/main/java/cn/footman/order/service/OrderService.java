package cn.footman.order.service;

import cn.footman.common.utils.FResult;
import cn.footman.order.pojo.OrderInfo;

public interface OrderService {

    FResult createOrder(OrderInfo orderInfo);
}
