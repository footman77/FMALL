package cn.footman.service.impl;

import cn.footman.common.jedis.JedisClient;
import cn.footman.common.pojo.EasyUIDataGridResult;
import cn.footman.common.utils.FResult;
import cn.footman.common.utils.IDUtils;
import cn.footman.common.utils.JsonUtils;
import cn.footman.mapper.TbItemDescMapper;
import cn.footman.mapper.TbItemMapper;
import cn.footman.pojo.TbItem;
import cn.footman.pojo.TbItemDesc;
import cn.footman.pojo.TbItemDescExample;
import cn.footman.pojo.TbItemExample;
import cn.footman.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.List;

/**
 * 商品管理Service
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private TbItemDescMapper tbItemDescMapper;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Resource
    private Destination topicDestination;

    @Autowired
    private JedisClient jedisClient;

    @Value("${REDIS_ITEM_PRE}")
    private String REDIS_ITEM_PRE;

    @Value("${ITEM_INFO_EXPIRED}")
    private Integer ITEM_INFO_EXPIRED;


    @Override
    public TbItem getItemById(long itemId) {
//        return tbItemMapper.selectByPrimaryKey(itemId);

        //查询缓存
        try{
            String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":BASE");
            if(StringUtils.isNotBlank(json)){
                TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
                return tbItem;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

//        设置条件
        TbItemExample tbItemExample = new TbItemExample();
        TbItemExample.Criteria criteria = tbItemExample.createCriteria();
        criteria.andIdEqualTo(itemId);

        List<TbItem> list = tbItemMapper.selectByExample(tbItemExample);
        if(list != null && list.size() > 0){

            try {
                //添加到缓存
                jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":BASE",JsonUtils.objectToJson(list.get(0)));
                jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":BASE",ITEM_INFO_EXPIRED);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return list.get(0);
        }
        return null;
    }

    @Override
    public EasyUIDataGridResult getItemList(int page, int rows) {
        PageHelper.startPage(page,rows);
//        设置分页信息
        TbItemExample e = new TbItemExample();
        //        执行查询
        List<TbItem> tbItems = tbItemMapper.selectByExample(e);
        EasyUIDataGridResult easyUIDataGridResult = new EasyUIDataGridResult();
        easyUIDataGridResult.setRows(tbItems);
        PageInfo<TbItem> tbItemPageInfo = new PageInfo<>(tbItems);
        long total = tbItemPageInfo.getTotal();
        easyUIDataGridResult.setTotal(total);
//        返回
        return easyUIDataGridResult;
    }

    @Override
    public FResult addItem(TbItem item, String desc) {
        //生成商品id
        long itemId = IDUtils.genItemId();
//        补全item属性
        item.setId(itemId);
        item.setStatus((byte) 1);
        item.setCreated(new Date());
        item.setUpdated(new Date());
//        插入商品表
        tbItemMapper.insert(item);
//        创建描述对象
        TbItemDesc tbItemDesc = new TbItemDesc();
//        补全信息
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(new Date());
        tbItemDesc.setUpdated(new Date());

        tbItemDescMapper.insert(tbItemDesc);

        jmsTemplate.send(topicDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(itemId + "");

            }
        });
        return FResult.ok();
    }

    @Override
    public TbItemDesc getItemDescById(long itemId) {


        //查询缓存
        try{
            String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":DESC");
            if(StringUtils.isNotBlank(json)){
                TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
                return itemDesc;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        //查询数据库
        TbItemDesc itemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
        try {
            //添加到缓存
            jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":DESC",JsonUtils.objectToJson(itemDesc));
            jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":DESC",ITEM_INFO_EXPIRED);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemDesc;
    }
}
