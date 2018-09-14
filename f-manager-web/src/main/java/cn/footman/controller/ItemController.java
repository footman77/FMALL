package cn.footman.controller;

import cn.footman.common.utils.FResult;
import cn.footman.pojo.TbItem;
import cn.footman.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class ItemController {

    @Resource
    private ItemService itemService;

//    public ItemService getItemService() {
//        return itemService;
//    }
//
//    public void setItemService(ItemService itemService) {
//        this.itemService = itemService;
//    }

    @RequestMapping("/item/{itemId}")
    @ResponseBody
    public TbItem getItemById(@PathVariable() long itemId ){

        return itemService.getItemById(itemId);
    }


    @RequestMapping(value = "/item/save",method = RequestMethod.POST)
    @ResponseBody
    public FResult saveItem(TbItem item,String desc){

        return itemService.addItem(item,desc);
    }
}
