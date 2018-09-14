package cn.footman.controller;

import cn.footman.common.pojo.EasyUITreeNode;
import cn.footman.service.ItemCatService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品分类管理
 */
@Controller
public class ItemCatController {


    @Resource
    private ItemCatService itemCatService;

    @RequestMapping("/item/cat/list")
    @ResponseBody
    public List<EasyUITreeNode> getItemCatList(@RequestParam(name = "id",defaultValue = "0") Long parentId){

        return itemCatService.getItemCatList(parentId);
    }
}
