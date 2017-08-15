package com.highill.practice.elasticsearch.lucene;

import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.util.Version;

/**
 * https://lucene.apache.org/core/6_6_0/core/org/apache/lucene/analysis/package-summary.html#package.description
 *
 */
public class LuceneDemoMain {

	public static void main(String[] args) {
		Version matchVersion = Version.LUCENE_6_6_0;
		Analyzer analyzer = new StandardAnalyzer();
		
		TokenStream tokenStream = analyzer.tokenStream("myfield", new StringReader("some text goes here 你最近好么, 北京欢迎你"));
		OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

		try {
			tokenStream.reset();
			
			while(tokenStream.incrementToken()) {
				System.out.println("token: " + tokenStream.reflectAsString(true));
				
				System.out.println("token start offset: " + offsetAttribute.startOffset());
				System.out.println("token end offset: " + offsetAttribute.endOffset());
				System.out.println("charTermAttribute: " + charTermAttribute.toString());
			}
			
			tokenStream.end();
			
			tokenStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
