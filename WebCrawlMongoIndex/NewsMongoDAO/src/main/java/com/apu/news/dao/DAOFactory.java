package com.apu.news.dao;

import javax.naming.ConfigurationException;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.apu.news.util.NewsConstants;
import com.apu.news.util.PropUtil;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * @author apurbapandey
 * DAOFactory is Singleton implementation to create a mongoClient.
 * Since MongoClient maintains a connection pool for itself, we are using a single mongoClient instance for our application.
 */
public class DAOFactory {

	private static final Logger logger = Logger.getLogger(DAOFactory.class);
	
	private static MongoClient mongoClient;
	
	
	/**
	 * @return mongoClient
	 * @throws ConfigurationException 
	 */
	public static MongoClient getMongoClient() throws ConfigurationException{
		if(null == mongoClient ){
			synchronized(DAOFactory.class) {
				getMongoClientInstance();
			}
			logger.info("Mongo Connection Succesfull.");
		}
		
		return mongoClient;
	}

	/*private static void getMongoClientInstance() {
		try {
			mongoClient = new MongoClient("localhost",27017);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}*/
	
	public static MongoDatabase getMongoDB(String db) throws ConfigurationException {
		
		if(null == mongoClient){
			getMongoClientInstance();
		}
		logger.info("Mongo Connection to db "+db+" Succesfull.");
		MongoDatabase mongoDB = mongoClient.getDatabase(db);
		
		return mongoDB;
	}
	
	public static MongoCollection<Document> getMongoCollection(String db, String collection) throws ConfigurationException {
		
		if(null == mongoClient){
			getMongoClientInstance();
		}
		MongoDatabase mongoDB = getMongoDB(db);
		MongoCollection<Document> mongoCollection = mongoDB.getCollection(collection);
		logger.info("Mongo Connection to collection "+collection+" Succesfull.");
		return mongoCollection;
	}
	
	public static void close(){
		
		mongoClient.close();
		mongoClient = null;
		logger.info("Mongo Connection Closed.");
	}
	

	/**
	 * private method to create mongoClient Instance.
	 * @throws ConfigurationException 
	 */
	private static void getMongoClientInstance() throws ConfigurationException {

		System.setProperty(PropUtil.newsProps.getProperty(NewsConstants.JAVA_SSL_PATH_PROP),
				PropUtil.newsProps.getProperty(NewsConstants.JAVA_SSL_PATH_VAL));
		System.setProperty(PropUtil.newsProps.getProperty(NewsConstants.JAVA_SSL_PWD_PROP),
				PropUtil.newsProps.getProperty(NewsConstants.JAVA_SSL_PWD_VAL));
		
		if(PropUtil.newsProps.containsKey(NewsConstants.MONGO_URI)){
			String url = PropUtil.newsProps.getProperty(NewsConstants.MONGO_URI);
			mongoClient = new MongoClient(new MongoClientURI(url));
		} else {
			throw new ConfigurationException(NewsConstants.MONGO_URI+" is not availble.");
		}
		
	}
}
