package com.spring.rss.feed;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.spring.rss.feed.entity.Feed;
import com.spring.rss.feed.entity.FeedItem;
import com.spring.rss.feed.entity.Subscription;
import com.spring.rss.feed.repository.FeedItemRepository;
import com.spring.rss.feed.repository.FeedRepository;
import com.spring.rss.feed.repository.SubscriptionRepository;
import com.spring.rss.feed.util.RssFeedFilter;

@SpringBootApplication
@ComponentScan(basePackages = "com.spring.rss.feed")
@EnableMongoRepositories(basePackages = "com.spring.rss.feed.repository")
@Configuration
public class SpringRssFeederApplication extends SpringBootServletInitializer {
	
	@Bean(name = "propertiesBean")
	public PropertiesFactoryBean reloadableProperties() {
		PropertiesFactoryBean propertiesBean = new PropertiesFactoryBean();
		// "tracking-properties" should be in classpath for this to work
		List<Resource> resourcesList = getResourcesList("classpath:url.properties");
		propertiesBean.setLocations(resourcesList.toArray(new Resource[] {}));
		propertiesBean.setIgnoreResourceNotFound(true);
		propertiesBean.setFileEncoding("UTF-8");
		return propertiesBean;
	}
	
	 @Bean("rssFeedFilter")
     public FilterRegistrationBean<RssFeedFilter> filterRegistrationBean() {
        FilterRegistrationBean<RssFeedFilter> bean = new FilterRegistrationBean<>(new RssFeedFilter());
        bean.addUrlPatterns("/*");
        return bean;
     }

	/**
	 * Gets the resources list.
	 *
	 * @param paths
	 *            the paths
	 * @return the resources list
	 */
	private List<Resource> getResourcesList(String... paths) {
		List<Resource> resouceList = new ArrayList<>();
		for (String path : paths) {
			Resource[] resources = getResources(path);
			if (resources != null) {
				resouceList.addAll(Arrays.asList(resources));
			}
		}
		return resouceList;
	}

	/**
	 * Gets the resources.
	 *
	 * @param files
	 *            the files
	 * @return the resources
	 */
	private Resource[] getResources(String files) {
		try {
			ClassLoader classLoader = this.getClass().getClassLoader();
			PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(classLoader);
			return resolver.getResources(files);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Resource[0];
	}


	@Autowired
	private RssFeedMessageSource rssFeedMessageSource;

	@Bean(name = "feedChannel")
	public PollableChannel feedChannel() {
		return new QueueChannel();
	}

	@Bean
	@InboundChannelAdapter(value = "feedChannel", poller = @Poller(maxMessagesPerPoll = "1", fixedDelay = "50000"))
	public MessageSource<List<SyndFeedWrapper>> feedAdapter() {
		return rssFeedMessageSource;
	}

	@MessageEndpoint
	public static class Endpoint {

		@Autowired
		private FeedItemRepository feedItemRepository;
		
		@Autowired
		private FeedRepository feedRepository;

		@Autowired
		private SubscriptionRepository subscriptionRepository;

		@ServiceActivator(inputChannel = "feedChannel", poller = @Poller(maxMessagesPerPoll = "1", fixedDelay = "50000"))
		public void handleFeeds(Message<List<SyndFeedWrapper>> message) throws IOException {
			List<SyndFeedWrapper> syndFeeds = message.getPayload();
			for (SyndFeedWrapper syndFeedWrapper : syndFeeds) {
				SyndFeed syndFeed = syndFeedWrapper.getSyndFeed();
				String feedUrl = syndFeed.getLink();
				String feedName = syndFeedWrapper.getFeedName();
				if (feedUrl == null) {
					System.out.println("Feed Link undefined for FEED[title]: " + syndFeed.getTitle());
					continue;
				}

				Subscription s = subscriptionRepository.findByRssUrl(syndFeedWrapper.getRssUrs());
				if (s == null) {
					System.out.println("No subscription found for URL: " + syndFeedWrapper.getRssUrs());
					s = new Subscription(null, syndFeedWrapper.getRssUrs(), 1,feedName);
				}

				//Feed feed = s.getFeed();

				/*if (feed == null) {
					feed = new Feed(feedUrl, syndFeed.getTitle(), syndFeed.getPublishedDate());
					s.setFeed(feed);
					subscriptionRepository.save(s);
				}*/
				
				List<FeedItem> items = new ArrayList<>();
				System.out.println("syndFeed.getTitle()" + syndFeed.getTitle());
				System.out.println("syndFeed.getUri()" + syndFeed.getUri());
				

				for (SyndEntry syndEntry : syndFeed.getEntries()) {
					
					String guid = syndEntry.getUri();
					if (StringUtils.isBlank(guid)) {
						guid = syndEntry.getLink();
					}
					
					FeedItem feedItem = new FeedItem();
					
					System.out.println("syndEntry.getAuthor()" + syndEntry.getAuthor());
					System.out.println("syndEntry.getTitle()" + syndEntry.getTitle());
					System.out.println("syndEntry.getLink()" + syndEntry.getLink());
					System.out.println("syndEntry.getUri()" + syndEntry.getUri());
					
					if (StringUtils.isBlank(guid)) {
						continue;
					}
					
					feedItem = new FeedItem(null, guid, syndEntry.getTitle(),
								syndEntry.getDescription() != null ? syndEntry.getDescription().getValue() : null, syndEntry.getAuthor(), syndEntry.getPublishedDate(),syndEntry.getLink());
					items.add(feedItem);
					
					
   				    //feedItemRepository.save(feedItem);
					
				}
				s.setFeedItems(items);
				//feedRepository.save(feed);
				subscriptionRepository.save(s);

			}
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringRssFeederApplication.class, args);
	}
}
