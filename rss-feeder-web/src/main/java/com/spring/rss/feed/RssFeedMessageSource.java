package com.spring.rss.feed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.spring.rss.feed.util.PropertyUtil;

@Component
public class RssFeedMessageSource implements MessageSource<List<SyndFeedWrapper>> {
	@Override
	public Message<List<SyndFeedWrapper>> receive() {
		return MessageBuilder.withPayload(getFeeds()).build();
	}

	private List<SyndFeedWrapper> getFeeds() {
		List<SyndFeedWrapper> feeds = new ArrayList<>();
		String[] urls = StringUtils.split(PropertyUtil.getPropertyByKey("rss.feed.urls"), ",");
		String[] feedNames = StringUtils.split(PropertyUtil.getPropertyByKey("rss.feed.names"), ",");
		int count = 0;
		for (String url : urls) {
			try {
				SyndFeed feed = readFeed(url);
				if (feed != null) {
					feeds.add(new SyndFeedWrapper(url, feed,feedNames[count++]));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return feeds;
	}

	private SyndFeed readFeed(String url) throws IOException, IllegalArgumentException, FeedException {

		// fetch data from URL
		SyndFeedInput input = new SyndFeedInput();
		SyndFeed feed = null;
		HttpGet httpGet = new HttpGet(url);
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {

			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				try (BufferedReader reader = new BufferedReader(
						new InputStreamReader(new BOMInputStream(httpResponse.getEntity().getContent())))) {
					feed = input.build(reader);
				}
			}
		}

		return feed;

	}
}
