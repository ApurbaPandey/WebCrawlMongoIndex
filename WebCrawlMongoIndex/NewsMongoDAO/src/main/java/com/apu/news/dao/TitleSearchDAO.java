package com.apu.news.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;


/**
 * @author apurbapandey
 * title is an indexed field.
 * TitleSearchDAO is created for searches based on title.
 */
public class TitleSearchDAO {
	
	private static final Logger logger = Logger.getLogger(TitleSearchDAO.class);
	
	/**
	 * @param title
	 * @param limit
	 * @param collection
	 * @return List of Documents searched by indexed field title
	 */
	public List<Document> searchTitle(String title, int limit, MongoCollection<Document> collection){
		
		logger.info("searchTitle : title = "+title);
		
		Document search = new Document().append("$search", title);
		Document txtSearch = new Document().append("$text", search);
		
		MongoCursor<Document> searchResults = collection.find(txtSearch).limit(limit).iterator();
		
		List<Document> articles = new ArrayList<Document>();
		
		Document doc;
		while(searchResults.hasNext()){
			doc = searchResults.next();
			articles.add(doc);
		}
		
		return articles;
	} 
	
	/**
	 * @param title
	 * @param collection
	 * @return Document searched by title
	 */
	public Document getByTitle(String title, MongoCollection<Document> collection){
		
		logger.info("getByTitle : title = "+title);
		
		Document txtSearch = new Document().append("title", title);
		
		FindIterable<Document> articles = collection.find(txtSearch).limit(1);
		
		return articles.first();
	}
}
