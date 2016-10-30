package org.apache.nutch.indexwriter.mongo;

public interface MongoConstants {
	
	public static final String MONGO_PREFIX = "mongo.";
	
	public static final String HOST = MONGO_PREFIX + "host";
	public static final String PORT = MONGO_PREFIX + "port";  
	public static final String DB = MONGO_PREFIX + "db";
	public static final String AUTH = MONGO_PREFIX + "auth";
	public static final String USER = MONGO_PREFIX + "usr";
	public static final String PWD = MONGO_PREFIX + "pwd";
	public static final String COLLECTION = MONGO_PREFIX + "collection";
	public static final String MAX_BULK_DOCS = MONGO_PREFIX + "max.docs";

}
