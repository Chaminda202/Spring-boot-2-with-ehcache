package com.spring.ehcache.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.spring.ehcache.config.AppErrorConfig;
import com.spring.ehcache.exception.ApplicationException;
import com.spring.ehcache.request.UserRequest;
import com.spring.ehcache.response.UserResponse;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class UserServiceIntegrationTest {
	@Autowired
	private UserService userService;
	@Autowired
	private AppErrorConfig appErrorConfig;

	@Test
	@Order(1)
	public void testCreateUser() throws ApplicationException {
		UserRequest req = UserRequest.builder().name("Kevin").occupation("Developer").age(26)
				.salary(new BigDecimal(245.56)).build();

		UserResponse response = this.userService.save(req);
		assertEquals("Kevin", response.getName());
		assertEquals("Developer", response.getOccupation());
		assertEquals(26, response.getAge());
		assertEquals(1, response.getUserId());
		assertEquals(new BigDecimal(245.56), response.getSalary());
	}

	@Test

	@Order(2)
	public void testGetUser_whenIdNotExist() throws ApplicationException {
		Throwable exception = assertThrows(ApplicationException.class, () -> this.userService.getById(123));
		assertEquals(exception.getMessage(), this.appErrorConfig.getNotExist());
	}

	@Test

	@Order(3)
	public void testGetUser_whenIdExist() throws ApplicationException {
		UserResponse response = this.userService.getById(1);
		assertEquals("Kevin", response.getName());
		assertEquals("Developer", response.getOccupation());
		assertEquals(26, response.getAge());
		assertEquals(1, response.getUserId());
		assertEquals(new BigDecimal(245.56), response.getSalary());
	}

}
