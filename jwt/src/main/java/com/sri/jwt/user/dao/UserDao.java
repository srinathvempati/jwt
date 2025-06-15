package com.sri.jwt.user.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sri.jwt.role.entity.Role;
import com.sri.jwt.user.entity.User;

@Repository
public interface UserDao extends CrudRepository<User, String> {

	Optional<User> findByUserName(String userName);
}
