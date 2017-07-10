package com.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.redis.interceptor.CookieInterceptor;
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter{
	@Autowired
	CookieInterceptor cookieInterceptor;
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(cookieInterceptor).addPathPatterns("/*");
	}
}
