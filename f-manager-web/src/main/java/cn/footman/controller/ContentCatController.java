package cn.footman.controller;

import cn.footman.common.pojo.EasyUITreeNode;
import cn.footman.common.utils.FResult;
import cn.footman.content.service.ContentCategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 内容分类
 */

@Controller
public class ContentCatController {

    @Resource
    private ContentCategoryService contentCategoryService;


    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUITreeNode> showContentCategory(@RequestParam(name = "id",defaultValue = "0") long parentId){
        return contentCategoryService.getContentCatListByPID(parentId);
    }


    @RequestMapping("/content/category/create")
    @ResponseBody
    public FResult addContentCategory(long parentId,String name){
        return contentCategoryService.addContentCategory(parentId,name);
    }

}
