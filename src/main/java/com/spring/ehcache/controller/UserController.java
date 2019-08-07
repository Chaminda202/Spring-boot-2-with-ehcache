package com.spring.ehcache.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.ehcache.common.CommonConstantValue;
import com.spring.ehcache.common.GsonUtil;
import com.spring.ehcache.config.AppErrorConfig;
import com.spring.ehcache.exception.ApplicationException;
import com.spring.ehcache.request.UserRequest;
import com.spring.ehcache.service.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/users")
public class UserController {
	private Logger logger;
	private UserService userService;
	private AppErrorConfig appErrorConfig;

	public UserController(UserService userService, AppErrorConfig appErrorConfig) {
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.userService = userService;
		this.appErrorConfig = appErrorConfig;

	}

	@ApiOperation(value = "Add a User")
	@PostMapping
	public Map<String, Object> save(@Valid @RequestBody UserRequest request) {
		logger.info("Start controller {}", GsonUtil.getToString(request, UserRequest.class));
		Map<String, Object> response = new HashMap<>();
		try {
			response.put(CommonConstantValue.STATUS, true);
			response.put(CommonConstantValue.DATA, this.userService.save(request));
		} catch (ApplicationException e) {
			response.put(CommonConstantValue.STATUS, false);
			response.put(CommonConstantValue.MESSAGE, e.getMessage());
		} catch (Exception e) {
			response.put(CommonConstantValue.STATUS, false);
			response.put(CommonConstantValue.MESSAGE, this.appErrorConfig.getCreate());
		}
		return response;
	}

	@ApiOperation(value = "View a list of users with pagination")
	@GetMapping(value = "/allWtPagin")
	public Map<String, Object> getAllWithPagin(
			@ApiParam(required = true, name = "page", value = "Page cannot be missing or empty") @RequestParam("page") int page,
			@ApiParam(required = true, name = "size", value = "Size cannot be missing or empty") @RequestParam("size") int size) {
		Map<String, Object> response = new HashMap<>();
		try {
			response.put(CommonConstantValue.STATUS, true);
			response.put(CommonConstantValue.DATA, this.userService.getAllWithPagin(page, size));
		} catch (Exception e) {
			response.put(CommonConstantValue.STATUS, false);
			response.put(CommonConstantValue.MESSAGE, this.appErrorConfig.getAll());
		}
		return response;
	}

	@ApiOperation(value = "View a list of users")
	@GetMapping
	public Map<String, Object> getAll() {
		logger.info("Starts get users list");
		Map<String, Object> response = new HashMap<>();
		try {
			response.put(CommonConstantValue.STATUS, true);
			response.put(CommonConstantValue.DATA, this.userService.getAll());
			logger.info("Get users list {}", CommonConstantValue.STATUS_SUCCESS);
		} catch (Exception e) {
			response.put(CommonConstantValue.STATUS, false);
			response.put(CommonConstantValue.MESSAGE, this.appErrorConfig.getAll());
			logger.error("Get users list {} -> {}", CommonConstantValue.STATUS_FAILED, e.getMessage());
		}
		return response;
	}

	@ApiOperation(value = "Get a user by Id")
	@GetMapping(value = "/{id}")
	public Map<String, Object> getOne(
			@ApiParam(required = true, name = "id", value = "Id cannot be missing or empty") @PathVariable Integer id) {
		logger.info("Starts get user by id {}", id);
		Map<String, Object> response = new HashMap<>();
		try {
			response.put(CommonConstantValue.STATUS, true);
			response.put(CommonConstantValue.DATA, this.userService.getById(id));
			logger.info("Get user by id {} -> {}",id, CommonConstantValue.STATUS_SUCCESS);
		} catch (ApplicationException e) {
			response.put(CommonConstantValue.STATUS, false);
			response.put(CommonConstantValue.MESSAGE, e.getMessage());
			logger.error("Get user by id {} -> {} -> {}",id, CommonConstantValue.STATUS_FAILED, e.getMessage());
		} catch (Exception e) {
			response.put(CommonConstantValue.STATUS, false);
			response.put(CommonConstantValue.MESSAGE, this.appErrorConfig.getById());
			logger.error("Get user by id {} -> {} -> {}",id, CommonConstantValue.STATUS_FAILED, e.getMessage());
		}
		return response;
	}
	
	@ApiOperation(value = "Get a user by name and occupation")
	@GetMapping(value = "/nameAndOccupation")
	public Map<String, Object> getNameAndOccupation(
			@ApiParam(required = true, name = "name", value = "Name cannot be missing or empty") @RequestParam("name") String name,
			@ApiParam(required = true, name = "occupation", value = "Occupation cannot be missing or empty") @RequestParam("occupation") String occupation) {
		logger.info("Starts get user by name & occupation {} -> {}", name, occupation);
		Map<String, Object> response = new HashMap<>();
		try {
			response.put(CommonConstantValue.STATUS, true);
			response.put(CommonConstantValue.DATA, this.userService.getByNameAndOccupation(name, occupation));
			logger.info("Get user by name & occupation {} -> {}",name, CommonConstantValue.STATUS_SUCCESS);
		} catch (ApplicationException e) {
			response.put(CommonConstantValue.STATUS, false);
			response.put(CommonConstantValue.MESSAGE, e.getMessage());
			logger.error("Get user by id {} -> {} -> {}", name, CommonConstantValue.STATUS_FAILED, e.getMessage());
		} catch (Exception e) {
			response.put(CommonConstantValue.STATUS, false);
			response.put(CommonConstantValue.MESSAGE, this.appErrorConfig.getByNameOccu());
			logger.error("Get user by id {} -> {} -> {}",name, CommonConstantValue.STATUS_FAILED, e.getMessage());
		}
		return response;
	}

	@ApiOperation(value = "Update a user")
	@PutMapping(value = "/{id}")
	public Map<String, Object> update(@RequestBody UserRequest request,
			@ApiParam(required = true, name = "id", value = "Id cannot be missing or empty") @PathVariable Integer id) {
		Map<String, Object> response = new HashMap<>();
		try {
			response.put(CommonConstantValue.STATUS, true);
			response.put(CommonConstantValue.DATA, this.userService.update(request, id));
		} catch (ApplicationException e) {
			response.put(CommonConstantValue.STATUS, false);
			response.put(CommonConstantValue.MESSAGE, e.getMessage());
		} catch (Exception e) {
			response.put(CommonConstantValue.STATUS, false);
			response.put(CommonConstantValue.MESSAGE, this.appErrorConfig.getUpdate());
		}
		return response;
	}

	@ApiOperation(value = "Delete a user")
	@DeleteMapping(value = "/{id}")
	public Map<String, Object> delete(
			@ApiParam(required = true, name = "id", value = "Id cannot be missing or empty") @PathVariable Integer id) {
		Map<String, Object> response = new HashMap<>();
		try {
			this.userService.delete(id);
			response.put(CommonConstantValue.STATUS, true);
			response.put(CommonConstantValue.MESSAGE, "User delete successfully");
		} catch (ApplicationException e) {
			response.put(CommonConstantValue.STATUS, false);
			response.put(CommonConstantValue.MESSAGE, e.getMessage());
		} catch (Exception e) {
			response.put(CommonConstantValue.STATUS, false);
			response.put(CommonConstantValue.MESSAGE, this.appErrorConfig.getDelete());
		}
		return response;
	}
}
