package com.spring.ehcache.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.spring.ehcache.common.GsonUtil;
import com.spring.ehcache.config.AppErrorConfig;
import com.spring.ehcache.exception.ApplicationException;
import com.spring.ehcache.model.User;
import com.spring.ehcache.repository.UserRepository;
import com.spring.ehcache.request.UserRequest;
import com.spring.ehcache.response.UserPaginResponse;
import com.spring.ehcache.response.UserResponse;
import com.spring.ehcache.service.UserService;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UserServiceImpl implements UserService {
	private Logger logger;
	private UserRepository userRepository;
	private AppErrorConfig appErrorConfig;
	
	public UserServiceImpl(UserRepository userRepository, AppErrorConfig appErrorConfig){
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.userRepository = userRepository;
		this.appErrorConfig = appErrorConfig;
	}
	
	@Override
	public UserResponse save(UserRequest request) throws ApplicationException {
		logger.info("Start service save {}", GsonUtil.getToString(request, UserRequest.class));
		User createUser = new User();
		logger.info("End save {}", GsonUtil.getToString(request, UserRequest.class));
		return buildResponse(this.userRepository.save(buildEntity(createUser, request)));
	}

	@Override
	public UserResponse update(UserRequest request, Integer id) throws ApplicationException {
		Optional<User> userOptional = this.userRepository.findById(id);
		if (userOptional.isPresent()) {
			User user = this.userRepository.save(buildEntity(userOptional.get(), request));
			return buildResponse(user);
		}
		throw new ApplicationException(this.appErrorConfig.getNotExist());
	}

	@Override
	public UserPaginResponse getAllWithPagin(int page, int size) {
		List<UserResponse> responseList = new ArrayList<>();
		Pageable pageable = PageRequest.of(page, size);
		Page<User> data = this.userRepository.findAll(pageable);
		data.getContent().forEach(item -> responseList.add(buildResponse(item)));
		return UserPaginResponse.builder()
				.userList(responseList)
				.pageNumber(pageable.getPageNumber())
				.pageSize(pageable.getPageSize())
				.totalRecords(data.getTotalElements())
				.build();
	}

	@Override
	@Cacheable(cacheNames="userAll")
	public List<UserResponse> getAll() {
		List<UserResponse> responseList = new ArrayList<>();
		List<User> data = this.userRepository.findAll();
		data.forEach(item -> responseList.add(buildResponse(item)));
		return responseList;
	}

	@Override
	@Cacheable(cacheNames="userById", key="#userId")
	public UserResponse getById(Integer userId) throws ApplicationException {
		Optional<User> userOptional = this.userRepository.findById(userId);
		if (userOptional.isPresent()) {
			return buildResponse(userOptional.get());
		}
		throw new ApplicationException(this.appErrorConfig.getNotExist());
	}

	@Override
	public void delete(Integer userId) throws ApplicationException {
		Optional<User> userOptional = this.userRepository.findById(userId);
		if (userOptional.isPresent()) {
			this.userRepository.delete(userOptional.get());
			return;
		}
		throw new ApplicationException(this.appErrorConfig.getNotExist());
	}

	private User buildEntity(User user, UserRequest request) {
		user.setName(request.getName());
		user.setOccupation(request.getOccupation());
		user.setAge(request.getAge());
		user.setSalary(request.getSalary());
		return user;
	}
	
	private UserResponse buildResponse(User user) {
		return UserResponse.builder()
				.userId(user.getId())
				.name(user.getName())
				.occupation(user.getOccupation())
				.age(user.getAge())
				.salary(user.getSalary())
				.build();
	}
}
