package com.apu.news;

import java.util.List;

import javax.naming.ConfigurationException;

import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.apu.news.dao.DAOFactory;
import com.apu.news.dao.DateSearchDAO;
import com.mongodb.client.MongoCollection;

public class DateSearchTest {
	
MongoCollection<Document> articles;
	
	@Before
	public void setup() throws ConfigurationException{
		
		articles = DAOFactory.getMongoCollection("nutchcrawl", "articles");
	}
	
	@Test
	public void sortByDateTest(){
		
		DateSearchDAO dao = new DateSearchDAO();
		List<Document> resp = dao.sortByDate(1, articles);
		
		Assert.assertEquals(resp.size(), 1);
		
		Document doc = resp.get(0);
		
		Assert.assertEquals(doc.containsKey("_id"), true);
		Assert.assertEquals(doc.containsKey("title"), true);
	}
	
	
	@After
	public void clean(){
		DAOFactory.close();
	}

}
