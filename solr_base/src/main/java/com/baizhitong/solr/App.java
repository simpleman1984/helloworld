package com.baizhitong.solr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.core.CoreContainer;

/**
 * Hello world!
 *
 */
public class App {
	
	
	public static void main(String[] args) throws SolrServerException, IOException {
//		 System.setProperty("solr.solr.home", "E:/tmp/solr/conf");
//		
//		 Map<String, String> params = new HashMap<String, String>();
//		 CoreContainer coreContainer = new CoreContainer("E:/tmp/solr/conf");
//		 coreContainer.load();
//		 if (!coreContainer.getAllCoreNames().contains("inited")) {
//		 coreContainer.create("inited", params);
//		 }
//		
//		  SolrClient solrClient = new EmbeddedSolrServer(coreContainer,
//		  "inited"); 
		
		String url = "http://192.168.0.138:8984/solr/new_core";
		HttpSolrClient solrClient = new HttpSolrClient(url);
		solrClient.setParser(new XMLResponseParser());

		// 全删除
		// solrClient.deleteByQuery( "*:*" );

		String lastId = UUID.randomUUID().toString();
		// 录入文档
		SolrInputDocument doc1 = new SolrInputDocument();
		doc1.addField("id", lastId, 1.0f);
		doc1.addField("name", "xxxsdfaf", 1.0f);
		doc1.addField("price", 10);
		solrClient.add(doc1);

		SolrInputDocument doc2 = new SolrInputDocument();
		doc2.addField("id", UUID.randomUUID().toString(), 1.0f);
		doc2.addField("name", "kjfkasdxcxvxmcvgdfg中国人说汉语", 1.0f);
		doc2.addField("price", 20);
		Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		docs.add(doc2);

		solrClient.add(docs);
		solrClient.commit();

		Item item = new Item();
		item.categories = new String[] { "1", "2", "3", "4" };
		// 更新
		item.id = UUID.randomUUID().toString();
		List<String> strList = new ArrayList();
		strList.add("asdfasdf");
		strList.add("kjfkasdxcxvxmcvgdfg 中国人 说汉语");
		item.features = strList;
		solrClient.addBean(item);

		// 查询
		SolrQuery query = new SolrQuery();

		// 查询条件
		// query.setQuery("*:*");//全部查询
		query.set("q", "name:*中国人*");
		query.addSort("price", SolrQuery.ORDER.desc)

		// .setHighlight(true)
		// .addHighlightField("name")
		// .setHighlightSimplePre("<font color='red'>")// 标记，高亮关键字前缀
		// .setHighlightSimplePost("</font>")// 后缀
		// .setHighlightSnippets(2)
		// .setParam("h1.f1", "name")
		// .setFacet(true)
		// .setFacetMinCount(1)
		// .setFacetLimit(8)
		// .addFacetField("name")
		;

		QueryResponse rsp = solrClient.query(query);
		SolrDocumentList solrDocumentList = rsp.getResults();

		for (SolrDocument solrDocument : solrDocumentList) {
			System.err.println("            查询结果:" + solrDocument.get("id") + "          " + solrDocument.get("name")
					+ "             " + solrDocument.get("price"));
		}

		// //根据唯一字符串取值
		// Map<String, Map<String, List<String>>> highlightingMap =
		// rsp.getHighlighting();
		// Map<String,List<String>> record = highlightingMap.get(lastId);
		// List<String> highlightStrings = record.get("name");
		// String s = "";
		// for(String str : highlightStrings)
		// {
		// s += str+",";
		// }
		// System.err.println(" 高亮取值"+s);

		 System.exit(9);
//		solrClient.close();
	}
}
