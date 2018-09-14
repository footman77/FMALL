package cn.footman.portal.controller;

import cn.footman.content.service.ContentService;
import cn.footman.pojo.TbContent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * 展示首页
 */
@Controller
public class IndexController {

    @Resource
    private ContentService contentService;

    @Value("${CONTENT_LUNBO_ID}")
    private long CONTENT_LUNBO_ID;


    @RequestMapping("index")
    public String showIndex(Model model){
        //查询内容
        List<TbContent> ad1List = contentService.getContentByCID(CONTENT_LUNBO_ID);
        model.addAttribute("ad1List",ad1List);

        return "index";
    }
}
