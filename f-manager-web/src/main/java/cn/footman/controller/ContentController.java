package cn.footman.controller;


import cn.footman.common.utils.FResult;
import cn.footman.content.service.ContentService;
import cn.footman.pojo.TbContent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 内容管理
 */
@Controller
public class ContentController {

    @Resource
    private ContentService contentService;

    //添加
    @RequestMapping(value = "/content/save",method = RequestMethod.POST)
    @ResponseBody
    public FResult saveContent(TbContent tbContent){
        return contentService.addContent(tbContent);
    }

}
