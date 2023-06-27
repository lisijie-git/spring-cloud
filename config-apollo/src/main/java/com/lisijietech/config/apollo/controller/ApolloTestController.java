package com.lisijietech.config.apollo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.lisijietech.config.apollo.bean.ApolloConfigurationPropertiesBean;
import com.lisijietech.config.apollo.bean.ApolloValueBean;

@RestController
public class ApolloTestController {
	
	@Autowired
	private ApolloConfigurationPropertiesBean propertiesBean;
	
	@Autowired
	private ApolloValueBean apolloValueBean;
	
	public void test() {
		System.out.println(propertiesBean.getNo());
		System.out.println(propertiesBean.getGame());
		System.out.println(apolloValueBean.getName());
		System.out.println(apolloValueBean.getAge());
	}

}
