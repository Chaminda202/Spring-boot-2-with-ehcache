package com.spring.ehcache.response;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse implements Serializable{
	private static final long serialVersionUID = 5979093076271614786L;
	@JsonProperty("user_id")
	private Integer userId;
	private String name;
	private Integer age;
	private String occupation;
	private BigDecimal salary;
}
