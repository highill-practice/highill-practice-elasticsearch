package com.highill.practice.elasticsearch.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.suggest.analyzing.FSTUtil.Path;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

public class LuceneIndexingAndSearchingDemo {

	public static void main(String[] args) {

		try {
			Analyzer analyzer = new StandardAnalyzer();

			// Store the index in memory
			Directory directory = new RAMDirectory();
			System.out.println("----- directory: " + directory);
			// To store an index on disk, user this instead
			// Directory directoryDisk = FSDirectory.open();

			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			IndexWriter indexWriter = new IndexWriter(directory, config);
			
			Document document = new Document();
			System.out.println("----- document empty: " + document);
			String text = "This is th text to be indexed. 北京欢迎你!";
			document.add(new Field("message", text, TextField.TYPE_STORED));
			indexWriter.addDocument(document);
			indexWriter.close();
			System.out.println("------document after add: " + document);
			
			// search the index
			DirectoryReader directoryReader = DirectoryReader.open(directory);
			IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
			
			// Parse a simple query
			QueryParser queryParser = new QueryParser("message", analyzer);
			Query query = queryParser.parse("北京");
			ScoreDoc[] hitArray = indexSearcher.search(query, 1000).scoreDocs;
			System.out.println("----hitArray length: " + (hitArray == null ? -1 : hitArray.length));
			
			for(int size = 0; size < hitArray.length; size++) {
				Document hitDocument = indexSearcher.doc(hitArray[size].doc);
				System.out.println("-----hitDocument: " + hitDocument);
			}
			
			directoryReader.close();
			directory.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
