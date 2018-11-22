package com.spring.rss.feed.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

//@Configuration
public class BootPropertyConfig {

	@Bean(name = "propertiesBean")
	public PropertiesFactoryBean reloadableProperties() {
		PropertiesFactoryBean propertiesBean = new PropertiesFactoryBean();
		// "tracking-properties" should be in classpath for this to work
		List<Resource> resourcesList = getResourcesList("classpath:*.properties");
		propertiesBean.setLocations(resourcesList.toArray(new Resource[] {}));
		propertiesBean.setIgnoreResourceNotFound(true);
		propertiesBean.setFileEncoding("UTF-8");
		return propertiesBean;
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

}
