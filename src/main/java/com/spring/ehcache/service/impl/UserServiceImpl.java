package com.spring.ehcache.service.impl;

import com.spring.ehcache.common.GsonUtil;
import com.spring.ehcache.config.AppErrorConfig;
import com.spring.ehcache.exception.ApplicationException;
import com.spring.ehcache.model.User;
import com.spring.ehcache.repository.UserRepository;
import com.spring.ehcache.request.UserRequest;
import com.spring.ehcache.response.UserPaginResponse;
import com.spring.ehcache.response.UserResponse;
import com.spring.ehcache.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
		logger.info("End service save {}", GsonUtil.getToString(request, UserRequest.class));
		return buildResponse(this.userRepository.save(buildEntity(createUser, request)));
	}

	@Override
	@CachePut(cacheNames = "users", key = "#id")
	public UserResponse update(UserRequest request, Integer id) throws ApplicationException {
		logger.info("Start service update {} -> {}", id, GsonUtil.getToString(request, UserRequest.class));
		Optional<User> userOptional = this.userRepository.findById(id);
		if (userOptional.isPresent()) {
			User user = this.userRepository.save(buildEntity(userOptional.get(), request));
			logger.info("End service save {}", GsonUtil.getToString(user, User.class));
			return buildResponse(user);
		}
		throw new ApplicationException(this.appErrorConfig.getNotExist());
	}

	@Override
	public UserPaginResponse getAllWithPaging(int page, int size) {
		logger.info("Start service get user with paging {} -> {}", page, size);
		Pageable pageable = PageRequest.of(page, size);
		Page<User> data = this.userRepository.findAll(pageable);
		List<UserResponse> responseList =  data.getContent()
				.stream()
				.map(this::buildResponse)
				.collect(Collectors.toList());
		logger.info("End service get user with paging");
		return UserPaginResponse.builder()
				.userList(responseList)
				.pageNumber(pageable.getPageNumber())
				.pageSize(pageable.getPageSize())
				.totalRecords(data.getTotalElements())
				.build();
	}

	@Override
	public List<UserResponse> getAll() {
		logger.info("Service get all users");
		return this.userRepository.findAll().stream()
				.map(this::buildResponse)
				.collect(Collectors.toList());
	}

	@Override
	@Cacheable(cacheNames = "users", key = "#userId")
	public UserResponse getById(Integer userId) throws ApplicationException {
		logger.info("Start service get user by id {}", userId);
		Optional<User> userOptional = this.userRepository.findById(userId);
		if (userOptional.isPresent()) {
			logger.info("End service get user by id {}", GsonUtil.getToString(userOptional.get(), User.class));
			return buildResponse(userOptional.get());
		}
		throw new ApplicationException(this.appErrorConfig.getNotExist());
	}

	@Override
	@CacheEvict(cacheNames = "users", key = "#userId")
	public void delete(Integer userId) throws ApplicationException {
		logger.info("Start service delete user {}", userId);
		Optional<User> userOptional = this.userRepository.findById(userId);
		if (userOptional.isPresent()) {
			this.userRepository.delete(userOptional.get());
			logger.info("End service delete user");
			return;
		}
		throw new ApplicationException(this.appErrorConfig.getNotExist());
	}
	
	@Override
	public UserResponse getByNameAndOccupation(String name, String occupation) throws ApplicationException {
		logger.info("Start service get user by name and occupation {} -> {}", name, occupation);
		Optional<User> userOptional = this.userRepository.findByNameAndOccupation(name, occupation);
		if (userOptional.isPresent()) {
			UserResponse response = buildResponse(userOptional.get());
			logger.info("End service get user by name and occupation {}", GsonUtil.getToString(response, UserResponse.class));
			return response;
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
