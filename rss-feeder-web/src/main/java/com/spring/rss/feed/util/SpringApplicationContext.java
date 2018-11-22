package com.spring.rss.feed.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringApplicationContext implements ApplicationContextAware {
	
	private static ApplicationContext CONTEXT;
	
	@Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringApplicationContext.CONTEXT = applicationContext;
    }
	
	public static ApplicationContext getContext() {
        if (CONTEXT == null) {
            System.out.println("Spring context not loaded as yet");
        }
        return CONTEXT;
    }

}
