package com.spring.rss.feed.repository;

import org.springframework.data.repository.CrudRepository;

import com.spring.rss.feed.entity.Subscription;

public interface SubscriptionRepository extends CrudRepository<Subscription, String> {
    //THIS COMMENT SHOULD SHOW IN MASTER AND DEVELOP
	//THIS COMMENT SHOULD SHOW ONLY IN DEVELOP
	Subscription findByRssUrl(String rssUrl);

}
