package cn.footman.controller;

import cn.footman.common.utils.FResult;
import cn.footman.search.service.SearchItemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
@Controller
public class ImportItemController {

    @Resource
    private SearchItemService searchItemService;


    @RequestMapping("/index/item/import")
    @ResponseBody
    public FResult importItem(){
        return searchItemService.importAllItems();
    }
}
