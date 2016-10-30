package com.apu.news;

import java.util.List;

import javax.naming.ConfigurationException;

import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.apu.news.dao.DAOFactory;
import com.apu.news.dao.TitleSearchDAO;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

public class TitleSearchTest {
	
	MongoCollection<Document> articles;
	
	@Before
	public void setup() throws ConfigurationException{
		
		articles = DAOFactory.getMongoCollection("nutchcrawl", "articles");
	}
	
	@Test
	public void searchArticleTest(){
		
		TitleSearchDAO dao = new TitleSearchDAO();
		List<Document> resp = dao.searchTitle("Bus", 1, articles );
		
		Assert.assertEquals(resp.size(), 1);
		
		Document doc = resp.get(0);
		
		Assert.assertEquals(doc.containsKey("_id"), true);
		Assert.assertEquals(doc.containsKey("title"), true);
	}
	
	@Test
	public void searchArticleByTitleTest(){
		
		TitleSearchDAO dao = new TitleSearchDAO();
		FindIterable<Document> resp = articles.find().limit(1);
		
		Document respDoc = resp.first();
		
		Assert.assertEquals(respDoc.containsKey("_id"), true);
		
		String id = respDoc.getString("_id");
		String title = respDoc.getString("title");
		
		Document getByTitleResp = dao.getByTitle(title, articles);
		
		Assert.assertEquals(getByTitleResp.get("_id"), id);
		Assert.assertEquals(getByTitleResp.get("title"), title);
	}
}
