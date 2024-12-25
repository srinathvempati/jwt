package com.youtube.jwt.dao;

import com.youtube.jwt.entity.Role;
import com.youtube.jwt.entity.User;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends CrudRepository<User, String> {

	Optional<User> findByUserName(String userName);
}
