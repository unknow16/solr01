package com.fuyi.lucene01.hello;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import com.fuyi.lucene01.domain.Article;

/**
 * 1. 把一个article对像放到索引库中
 * 2. 从索引库中把article对像检索出来
 * @author Administrator
 *
 */
public class HelloLucene {

	/**
	 * 1)	创建IndexWriter对象
		2)	把JavaBean转化为Document
		3)	利用IndexWriter.addDocument方法增加索引
		4)	关闭资源
	 * @throws Exception
	 */
	@Test
	public void testCreateIndex() throws Exception {
		
		//创建IndexWriter
		Directory directory = FSDirectory.open(new File("./indexDir"));
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
		IndexWriter indexWriter = new IndexWriter(directory, analyzer, MaxFieldLength.LIMITED);
		
		Article article = new Article();
		article.setId(1);
		article.setTitle("lucene可以做互联网搜索");
		article.setContent("搜索引擎，google可以查询互联网中的网页");
		
		/**
		 * 将article转化城document
		 * Store 是否把数据存目录中起来
		 * Index 是否增加或更新目录
		 * 		。NO 表示不把内容和目录关联
		 * 		。ANALYZER 表示分词后把内容和目录关联
		 * 		.NOT_ANALYZER 表示不分词把内容和目录关联
		 */
		Document doc = new Document();
					//存入索引库目录的名        索引目录值                                                              是否存入内容库                
		doc.add(new Field("id", article.getId().toString(), Store.YES, Index.NOT_ANALYZED));
		doc.add(new Field("title", article.getTitle(), Store.YES, Index.ANALYZED));
		doc.add(new Field("content", article.getContent(), Store.YES, Index.ANALYZED));
		
		//存入索引库
		indexWriter.addDocument(doc);
		indexWriter.close();
		
	}
	
	/**
	 * 1)	创建IndexSearch
		2)	创建Query对象
		3)	进行搜索
		4)	获得总结果数和前N行记录ID列表
		5)	根据目录ID列表把Document转为为JavaBean并放入集合中。
		6)	循环出要检索的内容

	 * @throws Exception
	 */
	@Test
	public void testSearch() throws Exception {
		String queryString = "搜索";
		
		List<Article> result = new ArrayList<Article>();
		
		//创建indexSearcher
		Directory directory = FSDirectory.open(new File("./indexDir"));
		IndexSearcher indexSearcher = new IndexSearcher(directory);
		
		/**
		 * 创建Query对像
		 * 版本
		 * Field 字段
		 * 分词器
		 */
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
		//content 为在哪个字段中检索
		//多个字段中检索         new MultiFieldQueryParser(matchVersion, fields, analyzer)
		QueryParser queryParser =  new MultiFieldQueryParser(Version.LUCENE_30, new String[]{"content", "title"}, analyzer);//new QueryParser(Version.LUCENE_30, "content", analyzer);
		Query query = queryParser.parse(queryString);
		
		/**
		 * 进行搜索
		 * query 查询对像
		 * n 表示显示前n行记录
		 */
		TopDocs topDocs = indexSearcher.search(query, 100);
		
		
		//解析搜索
		int count = topDocs.totalHits; //总记录数
		ScoreDoc[] scoreDocs = topDocs.scoreDocs; //前n行目录id列表
		
		for(int i=0; i<scoreDocs.length; i++) {
			ScoreDoc scoreDoc = scoreDocs[i];
			float score = scoreDoc.score; //获取相关度得分
			int docId = scoreDoc.doc; //获取目录列表id
			
			Document doc = indexSearcher.doc(docId); //根据id获得doc
			
			//把doc转化为article
			Article a = new Article();
			a.setId(Integer.parseInt(doc.get("id"))); //doc.getField("id").stringValue();
			a.setTitle(doc.get("title"));
			a.setContent(doc.get("content"));
			
			result.add(a);
		}
		
		for (Article art : result) {
			System.out.println(art);
		}
	
		indexSearcher.close();
	}
}
