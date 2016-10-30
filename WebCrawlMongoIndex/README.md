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
  6. Unfortunately, Apache Nutch didn't have any indexer plugin for mongo, so I have to write my own. You can find the source code and instraction to add in nutch [here](https://github.com/ApurbaPandey/WebCrawlMongoIndex/tree/master/WebCrawlMongoIndex). We have to add the mongo connection parameteres to $NUTCH_HOME/conf/nutch-site.xml:
  
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
