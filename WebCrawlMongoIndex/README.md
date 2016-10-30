#WebCrawlMongoIndex

This Project is my attempt to provide sollution for a coding challenge. The aim is to create a solution that crawls for articles from a news website, cleanses the response, stores in a mongo database then makes it available to search via an API.

## Approach

There are two sections in this problem.

  1. Web Crawling for Articles and storing them into Mongodb
  2. Providing and API to search articles

For Web Crawling, I have chosen [Apache Nuth](http://nutch.apache.org/). Apache Nutch is extensible and scalable open source web crawler software project. The main reason I went for Apache Nutch is that it is developed in Java. Other than being written in java, it has a wide range of plugins available for parsing, filtering and storing. Nutch can run on a single machine, but gains a lot of its strength from running in a Hadoop cluster. I have used Apache Nutch 2.3.1.

### Installing and building Nutch
