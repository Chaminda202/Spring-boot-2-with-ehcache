package com.spring.ehcache.service;

import java.util.List;

import com.spring.ehcache.exception.ApplicationException;
import com.spring.ehcache.request.UserRequest;
import com.spring.ehcache.response.UserPaginResponse;
import com.spring.ehcache.response.UserResponse;

public interface UserService {
	UserResponse save(UserRequest request) throws ApplicationException;
	UserResponse update(UserRequest request, Integer id) throws ApplicationException;
	UserPaginResponse getAllWithPagin(int page, int size);
	List<UserResponse> getAll();
	UserResponse getById(Integer userId) throws ApplicationException;
	void delete(Integer userId) throws ApplicationException;
}
