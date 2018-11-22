package com.spring.rss.feed;

import com.rometools.rome.feed.synd.SyndFeed;

public class SyndFeedWrapper {

	private String rssUrs;
	private String feedName;
	

	private SyndFeed syndFeed;

	public SyndFeedWrapper() {
	}

	public SyndFeedWrapper(String rssUrs, SyndFeed syndFeed, String feedName) {
		this.rssUrs = rssUrs;
		this.syndFeed = syndFeed;
		this.feedName = feedName;
	}

	public String getRssUrs() {
		return rssUrs;
	}

	public void setRssUrs(String rssUrs) {
		this.rssUrs = rssUrs;
	}

	public SyndFeed getSyndFeed() {
		return syndFeed;
	}

	public void setSyndFeed(SyndFeed syndFeed) {
		this.syndFeed = syndFeed;
	}
	
	public String getFeedName() {
		return feedName;
	}

	public void setFeedName(String feedName) {
		this.feedName = feedName;
	}

}
