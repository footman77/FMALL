package cn.footman.item.listener;

import cn.footman.item.pojo.Item;
import cn.footman.pojo.TbItem;
import cn.footman.pojo.TbItemDesc;
import cn.footman.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class HtmlGenListener implements MessageListener {

    @Resource
    private ItemService itemService;


    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;


    @Value("${FREEMARKER_LOCAL}")
    private String FREEMARKER_LOCAL;

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;

            String text = textMessage.getText();

            long itemId = Long.parseLong(text);
//            防止添加未完成，直接查询出错，等待事务的提交
            Thread.sleep(1000);
            //创建一个map存放数据
            Map data = new HashMap<>();

            TbItem tbItem = itemService.getItemById(itemId);
            Item item = new Item(tbItem);

            data.put("item",item);

            TbItemDesc itemDesc = itemService.getItemDescById(itemId);

            data.put("itemDesc",itemDesc);

            //创建一个Configuration对象
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
//            创建模板
            Template template = configuration.getTemplate("item.ftl");

            Writer writer = new FileWriter(new File(FREEMARKER_LOCAL + itemId + ".html"));

            template.process(data,writer);

//            关闭流
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
