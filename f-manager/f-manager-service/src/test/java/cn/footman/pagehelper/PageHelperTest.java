package cn.footman.pagehelper;

import cn.footman.mapper.TbItemMapper;
import cn.footman.pojo.TbItem;
import cn.footman.pojo.TbItemExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class PageHelperTest {
    @Test
    public void testPageHelper() throws Exception{

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
        TbItemMapper itemMapper = applicationContext.getBean(TbItemMapper.class);

        PageHelper.startPage(1,10);
        TbItemExample example = new TbItemExample();

        List<TbItem> list = itemMapper.selectByExample(example);

        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        System.out.println(pageInfo.getTotal());
        System.out.println(pageInfo.getPages());
        System.out.println(list.size());


    }
}
