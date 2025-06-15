package com.sri.jwt.role.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sri.jwt.role.entity.Role;

@Repository
public interface RoleDao extends CrudRepository<Role, String> {

}
