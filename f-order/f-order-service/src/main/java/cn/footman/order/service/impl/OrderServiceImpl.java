package cn.footman.order.service.impl;

import cn.footman.common.jedis.JedisClient;
import cn.footman.common.utils.FResult;
import cn.footman.mapper.TbOrderItemMapper;
import cn.footman.mapper.TbOrderMapper;
import cn.footman.mapper.TbOrderShippingMapper;
import cn.footman.order.pojo.OrderInfo;
import cn.footman.order.service.OrderService;

import cn.footman.pojo.TbOrderItem;
import cn.footman.pojo.TbOrderShipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {


    @Autowired
    private TbOrderMapper orderMapper;

    @Autowired
    private TbOrderItemMapper orderItemMapper;

    @Autowired
    private TbOrderShippingMapper orderShippingMapper;

    @Autowired
    private JedisClient jedisClient;

    //order自增长key
    @Value("${ORDER_INCR_KEY}")
    private String ORDER_INCR_KEY;
    //oeder自增长value
    @Value("${ORDER_INCR_VALUE}")
    private String ORDER_INCR_VALUE;
    //orderItem自增长key
    @Value("${ORDERITEM_INCR_KEY}")
    private String ORDERITEM_INCR_KEY;

    @Override
    public FResult createOrder(OrderInfo orderInfo) {
        //将获得数据插入数据库

        //插入order，补全信息
        //orderId使用jedis的自增长
        if(!jedisClient.exists(ORDER_INCR_KEY)){
            jedisClient.set(ORDER_INCR_KEY,ORDER_INCR_VALUE);
        }
        //获得订单id
        String orderId = jedisClient.incr(ORDER_INCR_KEY).toString();
        orderInfo.setOrderId(orderId);
        //状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭'
        orderInfo.setStatus(1);
        orderInfo.setCreateTime(new Date());
        orderInfo.setUpdateTime(new Date());
        //插入订单表中
        orderMapper.insert(orderInfo);

        //获得订单中的商品详情，插入数据表orderItem
        List<TbOrderItem> orderItems = orderInfo.getOrderItems();
        for(TbOrderItem orderItem : orderItems){
            //补全信息
            orderItem.setId(jedisClient.incr(ORDERITEM_INCR_KEY).toString());
            orderItem.setOrderId(orderId);

            orderItemMapper.insert(orderItem);

        }

        //插入物流信息表
        TbOrderShipping orderShipping = orderInfo.getOrderShipping();

        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(new Date());
        orderShipping.setUpdated(new Date());
        orderShippingMapper.insert(orderShipping);

        //返回订单号
        return FResult.ok(orderId);
    }
}
