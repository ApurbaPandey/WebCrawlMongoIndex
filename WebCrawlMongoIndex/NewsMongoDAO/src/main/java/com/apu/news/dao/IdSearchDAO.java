package com.apu.news.dao;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

/**
 * @author apurbapandey
 * DateSearchDAO is created for searching Articles by ID.
 */
public class IdSearchDAO {

	private static final Logger logger = Logger.getLogger(IdSearchDAO.class);
	
	/**
	 * @param id
	 * @param collection
	 * @return Document searched by id
	 */
	public Document getById(String id, MongoCollection<Document> collection){
		
		logger.info("getById : id = "+id);
		
		Document searchId = new Document().append("_id", id);
		
		FindIterable<Document> articles = collection.find(searchId).limit(1);
		
		return articles.first();
	}
}
