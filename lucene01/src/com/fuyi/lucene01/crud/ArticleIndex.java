package com.fuyi.lucene01.crud;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;

import com.fuyi.lucene01.domain.Article;
import com.fuyi.lucene01.utils.DocumentUtils;
import com.fuyi.lucene01.utils.LuceneConfig;

public class ArticleIndex {
	
	public static void main(String[] args) {
		
		ArticleIndex ai = new ArticleIndex();
		
		Article article = new Article();
		article.setId(2);
		article.setTitle("lis可以做互联网搜索");
		article.setContent("搜索baidu可以查询互联网中的网页");
		//ai.save(article);
		//ai.delete("1");
		//ai.upate(article);
		
		List<Article> result = ai.query("搜索");
		for (Article art : result) {
			System.out.println(art);
		}
	}

	public void save(Article article) {
		IndexWriter indexWriter = null;
		try {
			indexWriter = new IndexWriter(LuceneConfig.directory, LuceneConfig.analyzer, MaxFieldLength.LIMITED);
			indexWriter.addDocument(DocumentUtils.articleToDocument(article));
			indexWriter.commit();
		} catch (Exception e) {
			try {
				indexWriter.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	public void delete(String id) {
		IndexWriter indexWriter = null;
		try {
			indexWriter = new IndexWriter(LuceneConfig.directory, LuceneConfig.analyzer, MaxFieldLength.LIMITED);
			
			Term term = new Term("id", id);
			indexWriter.deleteDocuments(term);;
			indexWriter.commit();
		} catch (Exception e) {
			try {
				indexWriter.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	public void upate(Article article) {
		IndexWriter indexWriter = null;
		try {
			indexWriter = new IndexWriter(LuceneConfig.directory, LuceneConfig.analyzer, MaxFieldLength.LIMITED);
			
			Term term = new Term("id", article.getId().toString());
			indexWriter.updateDocument(term, DocumentUtils.articleToDocument(article)); //先删除后新增
			indexWriter.commit();
		} catch (Exception e) {
			try {
				indexWriter.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	public List<Article> query(String keyword) {
		List<Article> result = new ArrayList<Article>();
		IndexSearcher indexSearcher = null;
		
		try {
			indexSearcher = new IndexSearcher(LuceneConfig.directory);
			
			QueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_30, new String[]{"title", "content"}, LuceneConfig.analyzer);
			Query query = queryParser.parse(keyword);
			TopDocs topDocs = indexSearcher.search(query, 100);
			
			ScoreDoc[] docs = topDocs.scoreDocs;
			for(int i=0; i<docs.length; i++) {
				ScoreDoc s = docs[i];
				int docId = s.doc;
				Document doc = indexSearcher.doc(docId);
				result.add(DocumentUtils.documentToArticle(doc));
			}
			
		} catch (Exception e) {
			try {
				indexSearcher.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return result;
	}
}
