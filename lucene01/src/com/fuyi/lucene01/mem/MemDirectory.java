package com.fuyi.lucene01.mem;

import java.io.IOException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

import com.fuyi.lucene01.domain.Article;
import com.fuyi.lucene01.utils.DocumentUtils;
import com.fuyi.lucene01.utils.LuceneConfig;

/**
 * 内存索引库
 * @author Administrator
 *
 */
public class MemDirectory {

	/**
	 * 当应用程序退出的时候，内存索引库把数据再次保存到文件索引库，完成文件的保存工作。
	 * @throws Exception
	 */
	@Test
	public void testRAMDirectory() throws Exception {
		Article article = new Article();
		article.setId(1);
		article.setTitle("libobobobobo可以做互联网搜索");
		article.setContent("搜索引擎，google可以查询互联网中的网页");
		
		
		//把fsDirectory索引库加载到ramDirectory内存中
		//当应用程序启动的时候，从文件索引库加载文件到内存索引库。应用程序直接与内存索引库交互。
		Directory fsDirectory = LuceneConfig.directory;
		Directory ramDirectory = new RAMDirectory(fsDirectory);
		
		//内存索引writer
		IndexWriter ramIndexWriter = new IndexWriter(ramDirectory, LuceneConfig.analyzer, MaxFieldLength.LIMITED);
		//新增doc
		ramIndexWriter.addDocument(DocumentUtils.articleToDocument(article));
		ramIndexWriter.close();
		
		//fs索引writer    true表示重新创建一个或覆盖     false追加 
		IndexWriter fsIndexWriter = new IndexWriter(LuceneConfig.directory, LuceneConfig.analyzer, true, MaxFieldLength.LIMITED);
		//把内存索引库合并到当前索引库
		fsIndexWriter.addIndexesNoOptimize(ramDirectory);
		fsIndexWriter.close();
	}
}
