package com.spring.rss.feed.repository;

import org.springframework.data.repository.CrudRepository;

import com.spring.rss.feed.entity.Subscription;

public interface SubscriptionRepository extends CrudRepository<Subscription, String> {

	Subscription findByRssUrl(String rssUrl);

}
