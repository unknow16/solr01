package com.fuyi.solr01;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.junit.Test;

/**
 * 使用solrJ来调用搜索
 * 
 * 更新
 * @author Administrator
 *
 */
public class SolrDao {

	/**
	 * 查询方式一
	 * @throws Exception
	 */
	@Test
	public void addIndex() throws Exception {
		String urlString = "http://localhost:8983/solr";
		SolrServer solr = new HttpSolrServer(urlString);
		
		SolrInputDocument doc = new SolrInputDocument();
		
		doc.addField("id", 1);
		doc.addField("name", "付一");
		doc.addField("description", "王者荣耀");
		doc.addField("price", 32f);
		
		solr.add(doc);
		
		solr.commit();
	}
	
	/**
	 * 查询方式二
	 * @throws Exception
	 */
	@Test
	public void addIndex1() throws Exception {
		String urlString = "http://localhost:8983/solr";
		SolrServer solr = new HttpSolrServer(urlString);
		
		for(int i=2; i<25; i++) {
			
			Product product = new Product();
			product.setId(i);
			product.setName("lucene 是一个全文检索工具包");
			product.setDescription("apache");
			product.setPrice(99f);
			
			solr.addBean(product);
		}
		
		solr.commit();
	}
	
	/**
	 * 删除
	 * @throws Exception
	 */
	@Test
	public void deleteIndex() throws Exception {
		SolrServer solr =  new HttpSolrServer("http://localhost:8983/solr");
		
		solr.deleteById("3");
		solr.commit();
	}
	
	/**
	 * 更新只需id一样即可
	 */
	@Test
	public void updateIndex() {
		
	}
	
	@Test
	public void query() throws SolrServerException {
		SolrServer solr = new HttpSolrServer("http://localhost:8983/solr");
		
		SolrQuery params = new SolrQuery();
		params.setQuery("name:全文检索");
		
		//分页
		params.setStart(5);
		params.setRows(2);
		
		//高亮
		//1.开启高亮
		params.setHighlight(true);
		//2.设置高亮显示的格式
		params.setHighlightSimplePre("<font color='red'>");
		params.setHighlightSimplePost("</font>");
		//3.需要将哪些字段进行高亮
		params.setParam("hl.fl", "name");

		QueryResponse response  = solr.query(params);
		//返回没有高亮的结果list
		SolrDocumentList results = response.getResults();
		/**
		 * 返回高亮结果集
		 * 第一个map的key 代表document_id   doc.get("id")
		 *         value(第二个map) 代表高亮的字段（key）, 高亮结果（value）
		 *  第二个map的key  代表字段（name, price等）
		 *          value 高亮结果
		 */
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		
		List<Product> res = new ArrayList<Product>();
		Product product = null;
		for(SolrDocument doc : results) {
			product = new Product();
			
			Map<String, List<String>> map = highlighting.get(doc.get("id"));
			List<String> list = map.get("name");
			
			product.setId(Integer.parseInt(doc.get("id").toString()));
			product.setName(list.toString());
			product.setPrice(Float.parseFloat(doc.get("price").toString()));
			product.setDescription(doc.get("description").toString());
			res.add(product);
			
			//System.out.println(list.toString());
			
			/*System.out.println("id === " + doc.get("id").toString()
					+ ", name == " + doc.get("name") + ", description == " 
					+ doc.get("description") + ", price == " + doc.get("price"));*/
		}
		
		for (Product p : res) {
			System.out.println(p);
		}
	}
}
