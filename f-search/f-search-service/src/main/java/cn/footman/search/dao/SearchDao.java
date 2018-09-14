package cn.footman.search.dao;

import cn.footman.common.pojo.SearchItem;
import cn.footman.common.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SearchDao {

    @Autowired
    private SolrServer solrServer;

    public SearchResult searchQuery(SolrQuery query) throws Exception{
        //根据条件查询结果
        QueryResponse queryResponse = solrServer.query(query);
        SolrDocumentList solrDocuments = queryResponse.getResults();

        long numFound = solrDocuments.getNumFound();
        SearchResult searchResult = new SearchResult();
        //总记录数
        searchResult.setRecourdCount(numFound);

        //高亮
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();


        List<SearchItem> list = new ArrayList<>();
        for(SolrDocument solrDocument : solrDocuments){
            SearchItem searchItem = new SearchItem();
            searchItem.setId((String) solrDocument.get("id"));
            searchItem.setCategory_name((String) solrDocument.get("item_category_name"));
            searchItem.setImage((String) solrDocument.get("item_image"));
            searchItem.setPrice((Long) solrDocument.get("item_price"));
            searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
            List<String> stringList = highlighting.get(solrDocument.get("id")).get("item_title");
            String title = "";
            if(stringList != null && stringList.size() > 0){
                title = stringList.get(0);
            }else {
                title = (String) solrDocument.get("item_title");
            }
            searchItem.setTitle(title);
            list.add(searchItem);
        }

        searchResult.setItemList(list);
        return searchResult;
    }
}
