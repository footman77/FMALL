package cn.footman.solrJ;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class TestSolrJ {

    @Test
    public void addDocument() throws Exception{


        SolrServer solrServer = new HttpSolrServer("http://192.168.25.130:8080/solr");

        SolrInputDocument solrDocument = new SolrInputDocument();
        solrDocument.addField("id","100");
        solrDocument.addField("item_title","牢记");
        solrDocument.addField("item_sell_point","便宜");
        solrServer.add(solrDocument);
        solrServer.commit();

    }

    @Test
    public void deleteDocument() throws Exception{
        SolrServer solrServer = new HttpSolrServer("http://192.168.25.130:8080/solr");
        solrServer.deleteById("100");
        solrServer.commit();
    }


    @Test
    public void queryDocument() throws Exception{
        SolrServer solrServer = new HttpSolrServer("http://192.168.25.130:8080/solr");
        SolrQuery query = new SolrQuery();
        query.set("q","手机");
        query.setStart(0);
        query.setRows(10);
        query.set("df","item_title");
        //高亮
        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em>");
        query.setHighlightSimplePost("</em>");

        QueryResponse queryResponse = solrServer.query(query);
        SolrDocumentList documents = queryResponse.getResults();

        for (SolrDocument document : documents){
            System.out.println(document.get("id"));

            Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
            List<String> list = highlighting.get(document.get("id")).get("item_title");
            if(list != null && list.size() > 0){
                System.out.println(list.get(0));
            }else {
                System.out.println(document.get("item_title"));

            }
            System.out.println(document.get("item_price"));
            System.out.println(document.get("item_image"));
            System.out.println(document.get("item_sell_point"));
            System.out.println(document.get("item_category_name"));

        }

    }

}
