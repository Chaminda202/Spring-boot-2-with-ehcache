package com.spring.ehcache.request;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequest {
	private String name;
	private Integer age;
	private String occupation;
	private BigDecimal salary;
}
