package cn.footman.search.service.impl;

import cn.footman.common.pojo.SearchResult;
import cn.footman.search.dao.SearchDao;
import cn.footman.search.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchDao searchDao;

    @Override
    public SearchResult search(String keyword, int page, int rows) throws Exception{

        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(keyword);
        if(page < 0){
            page = 1;
        }
        solrQuery.setStart((page-1) * rows);
        solrQuery.setRows(rows);
        //搜索域
        solrQuery.set("df","item_title");
        //设置高亮显示
        solrQuery.setHighlight(true);
        solrQuery.setHighlightSimplePre("<span style=\"color:red\">");
        solrQuery.setHighlightSimplePost("</span>");
        SearchResult searchResult = searchDao.searchQuery(solrQuery);
        searchResult.setTotalPages((int) Math.ceil(searchResult.getRecourdCount() % rows));
        return searchResult;
    }
}
