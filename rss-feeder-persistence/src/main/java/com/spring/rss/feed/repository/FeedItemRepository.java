package com.spring.rss.feed.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.spring.rss.feed.entity.FeedItem;

public interface FeedItemRepository extends CrudRepository<FeedItem, String> {
	//List<FeedItem> findBySubscriptionId(String subscriptionId);
}
