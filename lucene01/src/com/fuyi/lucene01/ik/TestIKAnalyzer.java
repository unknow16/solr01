package com.fuyi.lucene01.ik;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class TestIKAnalyzer {

	@Test
	public void testIK() throws Throwable {
		String text = "付一鸣是学生";
		
		Analyzer a = new IKAnalyzer();
		TokenStream tokenStream = a.tokenStream("content", new StringReader(text));
		tokenStream.addAttribute(TermAttribute.class);
		
		while(tokenStream.incrementToken()) {
			TermAttribute attribute = tokenStream.getAttribute(TermAttribute.class);
			System.out.println(attribute.term());
		}
		
		a.close();
	}
}
