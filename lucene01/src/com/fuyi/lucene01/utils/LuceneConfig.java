package com.fuyi.lucene01.utils;

import java.io.File;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class LuceneConfig {

	public static Directory directory = null;
	public static Analyzer analyzer = null;
	
	static {
		try {
			directory = FSDirectory.open(new File("./indexDir"));
			analyzer = new StandardAnalyzer(Version.LUCENE_30);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
