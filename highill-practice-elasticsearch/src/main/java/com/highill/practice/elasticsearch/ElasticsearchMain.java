package com.highill.practice.elasticsearch;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**
 * 
 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/transport-client.html  
 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-docs-index.html  
 * 
 *
 */
public class ElasticsearchMain {

	public static long timeStamp() {
		long currentTimeStamp = 0;
		try {
			SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String timeStamp = simpleFormat.format(new Date());
			currentTimeStamp = Long.valueOf(timeStamp).longValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return currentTimeStamp;
	}

	public static void main(String[] args) {
		try {
		TransportClient elasticsearchClient = new PreBuiltTransportClient(Settings.EMPTY);
		elasticsearchClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.1.107"), 9300));
		
		System.out.println("-----elasticsearchClient: " + elasticsearchClient);
		
		// https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-docs-index.html
		Map<String, Object> valueMap = new HashMap<String, Object>();
		long timeStamp = timeStamp();
		valueMap.put("id", timeStamp);
		valueMap.put("user", "Username");
		valueMap.put("message", "测试Elasticsearch");
		
		XContentBuilder builder = XContentFactory.jsonBuilder().startObject()
				.field("id", timeStamp)
				.field("username", "Elasticsearch")
				.field("message", "测试 Elasticsearch")
				.endObject();
		String jsonText = builder.string();
		System.out.println("-----jsonText: " + jsonText);
		
		IndexResponse mapResponse = elasticsearchClient.prepareIndex("highill", "map").setSource(valueMap).get();
		System.out.println("-----mapResponse: " + mapResponse);
		IndexResponse builderResponse = elasticsearchClient.prepareIndex("highill", "builder").setSource(builder).get();
		System.out.println("-----builderResponse: " + builderResponse);
		
		GetResponse getResponse = elasticsearchClient.prepareGet("highill", "builder", "0").get();
		System.out.println("-----getResponse: " + getResponse);
		
		SearchResponse searchResponse = elasticsearchClient.prepareSearch("highill")
				// .setTypes("id", "name", "message")
				// .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				// .setQuery(QueryBuilders.termQuery("id", "20170807142324209"))
				// .setQuery(QueryBuilders.fuzzyQuery("username", "Elasticsearch"))
				.setQuery(QueryBuilders.fuzzyQuery("message", "Elasticsearch"))
				.setFrom(0).setSize(20).setExplain(true)
				.get();
		System.out.println("-----searchResponse: " + searchResponse);
		
		elasticsearchClient.close();
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
}
