package com.spring.rss.feed.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "subscriptions")
public class Subscription {

	@Id
	private String id;
	@Indexed(unique = true)
	private String rssUrl;
	private String feedName;
	

	private int subscrCount;
	private List<FeedItem> feedItems;

	public List<FeedItem> getFeedItems() {
		return feedItems;
	}

	public void setFeedItems(List<FeedItem> feedItems) {
		this.feedItems = feedItems;
	}

	public Subscription() {
	}

	public Subscription(String id, String rssUrl, int subscrCount, String feedName) {
		this.id = id;
		this.rssUrl = rssUrl;
		this.subscrCount = subscrCount;
		this.feedName = feedName;
	}

	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRssUrl() {
		return rssUrl;
	}

	public void setRssUrl(String rssUrl) {
		this.rssUrl = rssUrl;
	}

	public int getSubscrCount() {
		return subscrCount;
	}

	public void setSubscrCount(int subscrCount) {
		this.subscrCount = subscrCount;
	}

	public String getFeedName() {
		return feedName;
	}

	public void setFeedName(String feedName) {
		this.feedName = feedName;
	}

}
