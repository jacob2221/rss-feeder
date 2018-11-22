package com.spring.rss.feed.controller;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spring.rss.feed.entity.FeedItem;
import com.spring.rss.feed.entity.Subscription;
import com.spring.rss.feed.repository.SubscriptionRepository;
import com.spring.rss.feed.util.PropertyUtil;

@RestController
@RequestMapping("/api/")
public class RssFeedController {
	
	@Autowired
	private SubscriptionRepository subRepo;
	
	@RequestMapping("/getFeedUrls/")
	public List<String> getFeedUrls() {
		String urls = PropertyUtil.getPropertyByKey("rss.feed.urls");
		List<String> urlLst = new ArrayList<>();
		if(urls != null && !"".equals(urls.trim())){
			urlLst = Arrays.asList(urls.split(","));
		}
		
		return urlLst;
	}
	
	@RequestMapping(value = "/getFeeds/", method = RequestMethod.POST)
	public List<FeedItem> getFeeds(@RequestBody Subscription rssUrl) {
		List<FeedItem> feeds = new ArrayList<>();
		Subscription sub = subRepo.findByRssUrl(rssUrl.getRssUrl());
		if(sub != null && sub.getFeedItems() != null){
			feeds = sub.getFeedItems();
		}
		return feeds;
	}
	
	@RequestMapping(value = "/getAllFeeds/")
	public List<Subscription> getAllFeeds() {
		List<Subscription> feeds = new ArrayList<>();
		Iterable<Subscription> subIter = subRepo.findAll();
		if(subIter != null){
			subIter.forEach(feeds::add);
		}
		return feeds;
	}
	
	
	public static void main(String args[]){
		String a = "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";
	    try{
		new FileOutputStream("D:/sign.jpg").write(Base64.decodeBase64(a));
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	}

}
