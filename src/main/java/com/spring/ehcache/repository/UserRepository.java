package com.spring.ehcache.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.ehcache.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	Optional<User> findByName(String name);
	Optional<User> findByOccupation(String name);
}
