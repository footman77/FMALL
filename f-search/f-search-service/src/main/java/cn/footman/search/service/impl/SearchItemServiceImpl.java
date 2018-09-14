package cn.footman.search.service.impl;

import cn.footman.common.pojo.SearchItem;
import cn.footman.common.utils.FResult;
import cn.footman.search.mapper.ItemMapper;
import cn.footman.search.service.SearchItemService;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
@Service
public class SearchItemServiceImpl implements SearchItemService {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private SolrServer solrServer;

    @Override
    public FResult importAllItems() {

        try {
            //查询所有商品
            List<SearchItem> itemList = itemMapper.getSearchItem();


            for (SearchItem searchItem : itemList){
                SolrInputDocument document = new SolrInputDocument();
                document.setField("id",searchItem.getId());
                document.setField("item_title",searchItem.getTitle());
                document.setField("item_sell_point",searchItem.getSell_point());
                document.setField("item_price",searchItem.getPrice());
                document.setField("item_image",searchItem.getImage());
                document.setField("item_category_name",searchItem.getCategory_name());

                solrServer.add(document);
            }
            solrServer.commit();
            return FResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return FResult.build(500,"导入失败");
        }

    }
}
