package com.apu.news;

import javax.naming.ConfigurationException;

import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.apu.news.dao.DAOFactory;
import com.apu.news.dao.IdSearchDAO;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

public class IdSearchTest {

	MongoCollection<Document> articles;
	
	@Before
	public void setup() throws ConfigurationException{
		
		articles = DAOFactory.getMongoCollection("nutchresults", "articles");
	}
	
	@Test
	public void searchArticleByIDTest(){
		
		IdSearchDAO dao = new IdSearchDAO();
		FindIterable<Document> resp = articles.find().limit(1);
		
		Document respDoc = resp.first();
		
		Assert.assertEquals(respDoc.containsKey("_id"), true);
		
		String id = respDoc.getString("_id");
		String title = respDoc.getString("title");
		
		Document getByTitleResp = dao.getById(title, articles);
		
		Assert.assertEquals(getByTitleResp.get("_id"), id);
		Assert.assertEquals(getByTitleResp.get("title"), title);
	}
	
	@After
	public void clean(){
		DAOFactory.close();
	}
}
