package com.spring.rss.feed.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.spring.rss.feed.entity.Feed;
import com.spring.rss.feed.entity.FeedItem;

public interface FeedRepository extends CrudRepository<Feed, String> {
	Feed findByFeedUrl(String url);
}
