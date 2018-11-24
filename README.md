# rss-feeder
Spring boot application which uses spring integration to poll configured RSS sources periodically, pull feed data and stored in mongo db

Feed Urls and corresponding feed names can be configured as comma separated values of below two properties in application.properties. Current values are given below - 

rss.feed.urls=https://www.infosys.com/blog.xml,http://rss.cnn.com/rss/edition.rss
rss.feed.names=Infosys Feed,CNN Feed
