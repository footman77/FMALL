package cn.footman.search.listener;

import cn.footman.common.pojo.SearchItem;
import cn.footman.search.mapper.ItemMapper;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;


import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class ItemAddMessageListener implements MessageListener {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private SolrServer solrServer;

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            String text = textMessage.getText();
            long itemId = Long.parseLong(text);
            Thread.sleep(100);
            SearchItem searchItem = itemMapper.searchItemById(itemId);

            SolrInputDocument document = new SolrInputDocument();


            document.setField("id",searchItem.getId());
            document.setField("item_title",searchItem.getTitle());
            document.setField("item_sell_point",searchItem.getSell_point());
            document.setField("item_price",searchItem.getPrice());
            document.setField("item_image",searchItem.getImage());
            document.setField("item_category_name",searchItem.getCategory_name());

            solrServer.add(document);
            solrServer.commit();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
