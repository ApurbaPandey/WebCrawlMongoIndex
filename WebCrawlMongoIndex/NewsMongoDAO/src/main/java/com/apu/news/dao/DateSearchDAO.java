package com.apu.news.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

/**
 * @author apurbapandey
 * DateSearchDAO is created for searching Articles based on date.
 */
public class DateSearchDAO {
	
	private static final Logger logger = Logger.getLogger(IdSearchDAO.class);

	/**
	 * @param limit
	 * @param collection
	 * @return List of Documents sorted based on date
	 */
	public List<Document> sortByDate(int limit, MongoCollection<Document> collection){
		
		logger.info("sortByDate : ");
		
		Document sortDate = new Document().append("date",1 );
		
		FindIterable<Document> searchResults = collection.find().sort(sortDate).limit(limit);
		MongoCursor<Document> itr = searchResults.iterator();
		
		List<Document> articles = new ArrayList<Document>();
		
		Document doc;
		while(itr.hasNext()){
			doc = itr.next();
			articles.add(doc);
		}
		
		return articles;
	}
	
	/**
	 * @param date
	 * @param limit
	 * @param collection
	 * @return List of Documents filtered based on date
	 */
	public List<Document> searchByDate(Date date, int limit, MongoCollection<Document> collection){
		
		logger.info("searchByDate : date = "+date);
		
		Document searchDate = new Document().append("date", date );
		
		FindIterable<Document> searchResults = collection.find(searchDate).limit(limit);
		
		MongoCursor<Document> itr = searchResults.iterator();
		
		List<Document> articles = new ArrayList<Document>();
		
		Document doc;
		while(itr.hasNext()){
			doc = itr.next();
			articles.add(doc);
		}
		
		return articles;
	}
}
