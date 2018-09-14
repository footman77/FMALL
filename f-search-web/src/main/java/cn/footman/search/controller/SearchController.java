package cn.footman.search.controller;


import cn.footman.common.pojo.SearchItem;
import cn.footman.common.pojo.SearchResult;
import cn.footman.search.service.SearchService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品搜索
 */
@Controller
public class SearchController {

    @Resource
    private SearchService searchService;

    @Value("${SEARCH_RESULT_ROWS}")
    private Integer SEARCH_RESULT_ROWS;

    @RequestMapping("/search")
    public String searchItemList(String keyword, @RequestParam(defaultValue = "1") Integer page, Model model) throws Exception {

        keyword = new String(keyword.getBytes("iso-8859-1"),"utf-8");

        SearchResult searchResult = searchService.search(keyword, page, SEARCH_RESULT_ROWS);
        model.addAttribute("query",keyword);
        model.addAttribute("page",page);

        List<SearchItem> itemList = searchResult.getItemList();
        model.addAttribute("itemList",itemList);
        int totalPages = searchResult.getTotalPages();
        model.addAttribute("totalPages",totalPages);
        long recourdCount = searchResult.getRecourdCount();
        model.addAttribute("recourdCount",recourdCount);

//        异常
//        int a = 1/0;
        return "search";


    }

}
