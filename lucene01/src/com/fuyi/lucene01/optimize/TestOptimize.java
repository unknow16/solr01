package com.fuyi.lucene01.optimize;

import org.apache.lucene.index.IndexWriter;
import org.junit.Test;

import com.fuyi.lucene01.utils.LuceneUtils;

public class TestOptimize {

	@Test
	public void testOptimize() throws Exception {
		IndexWriter indexWriter = LuceneUtils.getIndexWriter();
		//自动优化合并，当cfs文件的数量为3个时，合并一个文件，默认为10个
		indexWriter.setMergeFactor(3); 
	
		indexWriter.optimize(); //直接调用优化合并
		indexWriter.close();
	}
}
