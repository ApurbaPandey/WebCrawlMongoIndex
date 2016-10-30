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
  3. Ensure the MongoDB gora-hbase dependency is available in $NUTCH_HOME/ivy/ivy.xml; Uncomment the below line from the file:
  ```
  <dependency org="org.apache.gora" name="gora-hbase" rev="0.6.1" conf="*->default" />
  ```
  Also add hbase-common
  ```
  <dependency org="org.apache.hbase" name="hbase-common" rev="0.98.8-hadoop2" conf="*->default" />
  ```
