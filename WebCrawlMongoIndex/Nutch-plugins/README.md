#Nutch-plugins : indexer-mongo

[Apache Nutch 2.3.1](http://nutch.apache.org/downloads.html) doesn't provide an in-built plugin for indexing into mongodb. Hence, I decided to write one for myself. It uses basic authentication to connect to mongodb.

# Add indexer-mongo plugin while building nutch

  1. Paste the indexer-mongo directory under $NUTCH_HOME/src/plugin directory.
  
  2. Add the following in the $NUTCH_HOME/src/plugin/build.xml
  
  ```
  <ant dir="indexer-mongo" target="deploy"/>
  ```
  
  3. Add indexer-mongo in the plugins list of $NUTCH_HOME/conf/nutch-site.xml aong with other plugins
  
  ```
  <property>
    <name>plugin.includes</name>
    <value>protocol-(http|httpclient)|urlfilter-regex|index-(basic|more)|query-(basic|site|url|lang)|indexer-mongo|nutch-extensionpoints|parse-(text|html|msexcel|msword|mspowerpoint|pdf)|summary-basic|scoring-opic|urlnormalizer-(pass|regex|basic)|parse-(html|tika|metatags)|index-(basic|anchor|more|metadata)</value>
  </property>
  ```
  
  4. Add Mongo-db configuration properties in $NUTCH_HOME/conf/nutch-site.xml. If authentication is needed , set mongo.auth to Y and provide user and password details, if not set it to N.
  
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
  <property>
    <name>mongo.auth</name>
    <value>Y</value>
  </property>
  <property>
    <name>mongo.usr</name>
    <value>apurv</value>
  </property>
  <property>
    <name>mongo.pwd</name>
    <value>password</value>
  </property>
  ```
  
  5. Finally we compile and build nutch `ant runtime`
  6. Verify the build.
  
  ```
  cd runtime/local
  bin/nutch
  ```
