package org.apache.nutch.indexwriter.mongo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.nutch.indexer.IndexWriter;
import org.apache.nutch.indexer.NutchDocument;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndReplaceOptions;

public class MongoIndexWriter implements IndexWriter{
	
	public static Logger LOG = LoggerFactory.getLogger(MongoIndexWriter.class);
	
	private static final int DEFAULT_MAX_BULK_DOCS = 250;
	private static final String AUTH_TRUE = "Y";
	
	private MongoClient mongoClient;
	private MongoDatabase mongoDB;
	private MongoCollection<Document> mongoCollection;
	//private DB mongoDB;
	//private DBCollection mongoCollection;
	
	private Configuration config;

	private int port = -1;
	private String host = null;
	private String db = null;
	private String collection = null;
	private String auth = null;
	private String user = null;
	private String pwd = null;
	
	private boolean createNewBulk = false;
	private int bulkDocs = 0;
	private int maxBulkDocs = DEFAULT_MAX_BULK_DOCS;
	
	private List<Document> insertList = new ArrayList<Document>();
	
	@Override
	public Configuration getConf() {
		return config;
	}

	@Override
	public void setConf(Configuration conf) {

		config = conf;
	    String cluster = conf.get(MongoConstants.DB);
	    String host = conf.get(MongoConstants.HOST);

	    if (StringUtils.isBlank(cluster) && StringUtils.isBlank(host)) {
	      String message = "Missing mongo.db and mongo.host. They should be set in nutch-site.xml ";
	      message += "\n" + describe();
	      LOG.error(message);
	      throw new RuntimeException(message);
	    }
	}

	@Override
	public void open(Configuration job) throws IOException {

		host = job.get(MongoConstants.HOST);
		port = job.getInt(MongoConstants.PORT, 27017);
		db = job.get(MongoConstants.DB);
		collection = job.get(MongoConstants.COLLECTION);
		auth = job.get(MongoConstants.AUTH);
		maxBulkDocs = job.getInt(MongoConstants.MAX_BULK_DOCS,DEFAULT_MAX_BULK_DOCS);
		
		if(host != null && port > 1 && db != null && collection != null && auth != null){
			
			List<ServerAddress> hosts = new ArrayList<ServerAddress>();
			hosts.add(new ServerAddress(host,port));
			
			if(auth.trim().equalsIgnoreCase(AUTH_TRUE)){
				user = job.get(MongoConstants.USER);
				pwd = job.get(MongoConstants.PWD);
				
				List<MongoCredential> credentials = new ArrayList<MongoCredential>();
				credentials.add( MongoCredential.createCredential(user, db, pwd.toCharArray()));
				
				mongoClient = new MongoClient(hosts, credentials);
				
			} else {
				mongoClient = new MongoClient(hosts);
			}
		} else {
			String message = "Missing mongo.db and mongo.host. They should be set in nutch-site.xml ";
		    message += "\n" + describe();
		    LOG.error(message);
		    throw new RuntimeException(message);
		}
		
		if(null != mongoClient && null != db && db.length() > 0){
			mongoDB = mongoClient.getDatabase(db);
		}
		
		if(null != mongoDB && null != collection && collection.length() > 0){
			mongoCollection = mongoDB.getCollection(collection);
		}
		
	}

	@Override
	public void write(NutchDocument doc) throws IOException {

		String id = (String) doc.getFieldValue("id");
		
		Document mongoDoc = new Document();
		mongoDoc.append("_id", id);
		
		for (String fieldName : doc.getFieldNames()) {
			if (doc.getFieldValues(fieldName).size() > 1) {
				mongoDoc.append(fieldName, doc.getFieldValues(fieldName));
			} else {
				mongoDoc.append(fieldName, doc.getFieldValue(fieldName));
			}
		}
		insertList.add(mongoDoc);
		bulkDocs++;
		
		if (bulkDocs >= maxBulkDocs) {
			
			LOG.info("Processing bulk request [docs = " + bulkDocs + ", "
					+ ", last doc in bulk = '" + id + "']");
			// Flush the bulk of indexing requests
			createNewBulk = true;
			commit();
		}
	}

	@Override
	public void delete(String key) throws IOException {

		Document filter = new Document().append("_id", key);
		mongoCollection.deleteOne(filter);
	}

	@Override
	public void update(NutchDocument doc) throws IOException {

		String id = (String) doc.getFieldValue("id");
		
		Document mongoDoc = new Document();
		mongoDoc.append("_id", id);
		
		for (String fieldName : doc.getFieldNames()) {
			if (doc.getFieldValues(fieldName).size() > 1) {
				mongoDoc.append(fieldName, doc.getFieldValues(fieldName));
			} else {
				mongoDoc.append(fieldName, doc.getFieldValue(fieldName));
			}
			
		}
		Document filter = new Document().append("_id", id);
		FindOneAndReplaceOptions options = new FindOneAndReplaceOptions();
		options.upsert(true);
		mongoCollection.findOneAndReplace(filter, mongoDoc, options);
	}

	@Override
	public void commit() throws IOException {
		
		if(null != mongoCollection && insertList.size() > 0){
			mongoCollection.insertMany(insertList);
		}
			
		if (createNewBulk) {
			bulkDocs = 0;
		}
		
		insertList.clear();
	}

	@Override
	public void close() throws IOException {
		
		createNewBulk = false;
	    commit();
		
		if(null != mongoClient){
			mongoClient.close();
		}
		
	}

	@Override
	public String describe() {

		StringBuffer sb = new StringBuffer("MongoIndexWriter\n");
	    sb.append("\t").append(MongoConstants.DB)
	        .append(" : mongo prefix cluster\n");
	    sb.append("\t").append(MongoConstants.HOST).append(" : hostname\n");
	    sb.append("\t").append(MongoConstants.PORT)
	        .append(" : port  (default 27017)\n");
	    sb.append("\t").append(MongoConstants.COLLECTION)
	        .append(" : mongo index command \n");
	    sb.append("\t").append(MongoConstants.MAX_BULK_DOCS)
	        .append(" : mongo bulk doc counts. (default 250) \n");
	    return sb.toString();
	
	}
	
	public static IOException makeIOException(MongoException e) {
	    final IOException ioe = new IOException();
	    ioe.initCause(e);
	    return ioe;
	  }

}
