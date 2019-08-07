package com.spring.ehcache.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Configuration
@PropertySource("classpath:app-error.properties")
@Data
public class AppErrorConfig {
	@Value("${error.not-exit}")
	private String notExist;

	@Value("${error.create}")
	private String create;

	@Value("${error.update}")
	private String update;

	@Value("${error.by-id}")
	private String byId;
	
	@Value("${error.by-name-occu}")
	private String byNameOccu;
	
	@Value("${error.all}")
	private String all;

	@Value("${error.delete}")
	private String delete;

	@Value("${error.validation}")
	private String validation;
	
	@Value("${error.type-matching}")
	private String typeMatching;
}
