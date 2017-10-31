package com.fuyi.lucene01.utils;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.Directory;

public class LuceneUtils {

	private static IndexWriter indexWriter = null;
	static {
		Directory directory = LuceneConfig.directory;
		Analyzer analyzer = LuceneConfig.analyzer;
		
		try {
			indexWriter = new IndexWriter(directory, analyzer, MaxFieldLength.LIMITED);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void close() {
		if(indexWriter != null) {
			try {
				indexWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static IndexWriter getIndexWriter() {
		return indexWriter;
	}

}
