package cn.footman.item.controller;

import cn.footman.item.pojo.Item;
import cn.footman.pojo.TbItem;
import cn.footman.pojo.TbItemDesc;
import cn.footman.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
public class ItemController {

    @Resource
    private ItemService itemService;

    @RequestMapping("/item/{itemId}")
    public String ItemDetail(@PathVariable long itemId,Model model){
        TbItem tbItem = itemService.getItemById(itemId);
        Item item = new Item(tbItem);
        model.addAttribute("item",item);
        TbItemDesc itemDesc = itemService.getItemDescById(itemId);
        model.addAttribute("itemDesc",itemDesc);
        
        return "item";

    }
}
