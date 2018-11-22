package com.spring.rss.feed.util;

import java.io.IOException;

import org.springframework.beans.factory.config.PropertiesFactoryBean;

public class PropertyUtil {
	private static PropertiesFactoryBean properties;

	private static synchronized PropertiesFactoryBean getProperties() {

		if (properties == null) {
			properties = SpringApplicationContext.getContext().getBean("&propertiesBean", PropertiesFactoryBean.class);
		}
		return properties;
	}

	public static String getPropertyByKey(String messageKey) {
		try {
			return getProperties().getObject().getProperty(messageKey);
		} catch (IOException e) {
			System.out.println("Cannot find property for key: " + messageKey);
			e.printStackTrace();
		}
		return null;
	}

}
