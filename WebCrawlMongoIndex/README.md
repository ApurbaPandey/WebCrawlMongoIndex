#WebCrawlMongoIndex

This Project is my attempt to provide sollution for a coding challenge. The aim is to create a solution that crawls for articles from a news website, cleanses the response, stores in a mongo database then makes it available to search via an API.

## Approach

There are two sections in this problem.

  1. Web Crawling for Articles and storing them into Mongodb
  2. Providing and API to search articles

For Web Crawling, I have chosen [Apache Nuth](http://nutch.apache.org/). Apache Nutch is extensible and scalable open source web crawler software project. The main reason I went for Apache Nutch is that it is developed in Java. Other than being written in java, it has a wide range of plugins available for parsing, filtering and storing. Nutch can run on a single machine, but gains a lot of its strength from running in a Hadoop cluster. I have used Apache Nutch 2.3.1. I have chosen HBase as crawldb for Nutch and index on Mongo as per the problem statment.

### Installing and building Nutch

  1. Download latest Nutch from [here](http://nutch.apache.org/downloads.html) and extract. Set `export NUTCH_HOME=$(pwd)`
  2. Edit $NUTCH_HOME/conf/nutch-site.xml to add:
  
  ```
  <property>
    <name>storage.data.store.class</name>
    <value>org.apache.gora.hbase.store.HBaseStore</value>
    <description>Default class for storing data</description>
  </property>
  ```
  3. Ensure the HBase gora-hbase dependency is available in $NUTCH_HOME/ivy/ivy.xml; Uncomment the below line from the file:
  
  ```
  <dependency org="org.apache.gora" name="gora-hbase" rev="0.6.1" conf="*->default" />
  ```
  Also add hbase-common :
  
  ```
  <dependency org="org.apache.hbase" name="hbase-common" rev="0.98.8-hadoop2" conf="*->default" />
  ```
  4. Ensure that HBaseStore is set as the default datastore in $NUTCH_HOME/conf/gora.properties:
  
  ```
  gora.datastore.default=org.apache.gora.hbase.store.HBaseStore
  ```
  5. Add the following to pluggins in the $NUTCH_HOME/conf/nutch-site.xml:
  
  ```
  <property>
    <name>plugin.includes</name>
    <value>protocol-(http|httpclient)|urlfilter-regex|index-(basic|more)|query-(basic|site|url|lang)|indexer-mongo|nutch-extensionpoints|parse-(text|html|msexcel|msword|mspowerpoint|pdf)|summary-basic|scoring-opic|urlnormalizer-(pass|regex|basic)|parse-(html|tika|metatags)|index-(basic|anchor|more|metadata)</value>
  </property>
  ```
  6. Unfortunately, Apache Nutch didn't have any indexer plugin for mongo, so I have to write my own. You can find the source code and instraction to add in nutch [here](https://github.com/ApurbaPandey/WebCrawlMongoIndex/tree/master/WebCrawlMongoIndex/Nutch-plugins). We have to add the mongo connection parameteres to $NUTCH_HOME/conf/nutch-site.xml:
  
  ```
  <property>
    <name>mongo.host</name>
    <value>localhost</value>
  </property>
  <property>
    <name>mongo.port</name>
    <value>27017</value>
  </property>
  <property>
    <name>mongo.db</name>
    <value>nutchresults</value>
  </property>
  <property>
    <name>mongo.collection</name>
    <value>articles</value>
  </property>
  ```
  7. Finally we compile and build nutch `ant runtime`
  8. Verify the build.
  
  ```
  cd runtime/local
  bin/nutch
  ```
## Crawling and Storing 

  1. create urls/seeds.txt file and add the list of websites you want to crawl. 
  
  ```
  mkdir urls
  echo 'http://www.example.com' > urls/seeds.txt
  ```
  2. Edit the file conf/regex-urlfilter.txt and replace `+.` at the end of the file to `+^http://([a-z0-9]*\.)*example.com/` if you want to crawl only from one website.
  3. Initialize the crawldb `bin/nutch inject urls/seed.txt -crawlId 97`
  4. Generate URLs from crawldb `bin/nutch generate -topN 100 -crawlId 97`
  5. Fetch generated URLs `bin/nutch fetch -all -crawlId 97`
  6. Parse fetched URLs `bin/nutch parse -all -crawlId 97`
  7. Update database from parsed URLs `bin/nutch updatedb -all -crawlId 97`
  8. And Finally Index parsed URLs `bin/nutch index -all -crawlId 97`
  
## Challenges and Sollutions.

  1. Initially, I wanted to use mongodb as crawldb, but gora-mongodb had an onder version of mongo-java-driver. Then I decided to use Cassandra as crawldb but there is an [unresolved issue](https://issues.apache.org/jira/browse/GORA-416) in gora-cassandra which was failing while fetching. Finally I decided to use HBase as crawldb.
  
  2. Apache Nutch didn't have an in built plugin for mongobd as indexer, hence I had to write my own. [Here](https://github.com/ApurbaPandey/WebCrawlMongoIndex/tree/master/WebCrawlMongoIndex/Nutch-plugins) is the implementation for the same.
  
  3. Apache Nutch follows the robots.txt exclusion standard.
  
  
  For the Second part of the problem, which is to create search API, I planned to use spring boot with spring data for mongodb. But I found out that there is an [issue with mongo](https://jira.mongodb.org/browse/JAVA-2229) which is fixed in upcoming version of mongo-java-driver. Because of this issue, Mongodb Authentication with SSL was failing and I needed SSL authentication to connect my mongodb. Hence, I decided to create simple DAO with singletone. The reson I went for singletone is, MongoClient is thread-safe and has its own connetion pooling mechanism.
  
  The title field of the mongoDB collection article is an indexed field, hence we can do a text search on this. Except title currently search is available only id and date fields. I am currently working on providing more search features.
  
## Future Implementation Plan

  1. Currently I am using the html-parser plugin for parsing the text from crawled webpage available in Nutch. It doesn't aloow me to parse specific tags related to the webpage. I want to create a custom parser plugin which will help me to parse specific html tags with css.
  
  2. I am not usig spring boot and spring data for the search API as of now. I want to remove the current DAO implemntaion by Spring boot and spring data for mongodb. This will also help in creating more search options.
  
  3. A part of the problem was to deploy the solution as a public API using an Amazon EC2 instance. I am not able to complete id as of now, but want to complete it as early as possible.
  
