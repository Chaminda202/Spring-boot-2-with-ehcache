package com.spring.ehcache.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserPaginResponse implements Serializable{
	private static final long serialVersionUID = -2470088827660001659L;
	private List<UserResponse> userList;
	@JsonProperty("page_number")
	private int pageNumber;
	@JsonProperty("page_size")
	private int pageSize;
	@JsonProperty("total_records")
	private long totalRecords;
}
