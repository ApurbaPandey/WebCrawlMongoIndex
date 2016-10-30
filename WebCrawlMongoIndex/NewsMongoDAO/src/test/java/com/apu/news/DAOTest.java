package com.apu.news;

import javax.naming.ConfigurationException;

import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

import com.apu.news.dao.DAOFactory;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class DAOTest {
	
	@Test
	public void getMongoClientTest() throws ConfigurationException{
		MongoClient client = DAOFactory.getMongoClient();
		
		Assert.assertNotNull(client);
	}
	
	@Test
	public void getMongoDBTest() throws ConfigurationException{
		MongoDatabase db = DAOFactory.getMongoDB("nutchresults");
		
		Assert.assertNotNull(db);
	}
	
	@Test
	public void getMongoCollection() throws ConfigurationException{
		MongoCollection<Document> collection = DAOFactory.getMongoCollection("nutchresults","articles");
		
		Assert.assertNotNull(collection);
	}

}
