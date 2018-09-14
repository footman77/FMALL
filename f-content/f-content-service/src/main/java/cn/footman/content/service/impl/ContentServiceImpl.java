package cn.footman.content.service.impl;

import cn.footman.common.jedis.JedisClient;
import cn.footman.common.utils.FResult;
import cn.footman.common.utils.JsonUtils;
import cn.footman.content.service.ContentService;
import cn.footman.mapper.TbContentMapper;
import cn.footman.pojo.TbContent;
import cn.footman.pojo.TbContentExample;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;
    @Autowired
    private JedisClient jedisClient;

    @Value("${CONTENT_LIST}")
    private String CONTENT_LIST;

    /**
     * 添加内容
     * @param tbContent
     * @return
     */
    @Override
    public FResult addContent(TbContent tbContent) {

        tbContent.setCreated(new Date());
        tbContent.setUpdated(new Date());
        contentMapper.insert(tbContent);
        //删除缓存，使其在下次查询时再次查询数据库，同步缓存
        jedisClient.hdel(CONTENT_LIST,tbContent.getCategoryId().toString());
        return FResult.ok();

    }


    /**
     *
     * @param categoryId
     * @return
     */
    @Override
    public List<TbContent> getContentByCID(long categoryId) {

//        取缓存

        try {
            String hget = jedisClient.hget(CONTENT_LIST, categoryId + "");
            if(StringUtils.isNoneBlank(hget)){
                return JsonUtils.jsonToList(hget,TbContent.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        TbContentExample contentExample = new TbContentExample();
        TbContentExample.Criteria criteria = contentExample.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);


        List<TbContent> list = contentMapper.selectByExampleWithBLOBs(contentExample);

//        添加到redis中
        try {
            jedisClient.hset(CONTENT_LIST,categoryId + "",JsonUtils.objectToJson(list));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;



    }
}
