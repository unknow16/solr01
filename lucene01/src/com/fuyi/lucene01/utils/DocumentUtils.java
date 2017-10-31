package com.fuyi.lucene01.utils;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;

import com.fuyi.lucene01.domain.Article;

/**
 * 在对索引库进行操作时，增、删、改过程要把一个JavaBean封装成Document，
 * 而查询的过程是要把一个Document转化成JavaBean。
 * 在进行维护的工作中，要反复进行这样的操作，所以我们有必要建立一个工具类来重用代码。
 * @author Administrator
 *
 */
public class DocumentUtils {

	public static Document articleToDocument(Article article) {
		Document document = new Document();
		
		document.add(new Field("id", article.getId().toString(), Store.YES, Index.NOT_ANALYZED));
		document.add(new Field("title", article.getTitle(), Store.YES, Index.ANALYZED));
		document.add(new Field("content", article.getContent(), Store.YES, Index.ANALYZED));
		
		return document;
	}
	
	public static Article documentToArticle(Document doc) {
		Article article = new Article();
		
		article.setId(Integer.parseInt(doc.get("id"))); //doc.getField("id").stringValue();
		article.setTitle(doc.get("title"));
		article.setContent(doc.get("content"));
		
		return article;
	}
}
