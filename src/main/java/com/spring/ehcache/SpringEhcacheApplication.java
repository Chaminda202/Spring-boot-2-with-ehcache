package com.spring.ehcache;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class SpringEhcacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringEhcacheApplication.class, args);
	}

	@Bean
	public KeyGenerator multiplyKeyGenerator() {
		return (Object target, Method method, Object... params) -> method.getName() + "_" + Arrays.toString(params);
	}
}
