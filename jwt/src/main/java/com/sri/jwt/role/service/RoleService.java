package com.sri.jwt.role.service;

import com.sri.jwt.role.dao.RoleDao;
import com.sri.jwt.role.entity.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleDao roleDao;

    public Role createNewRole(Role role) {
        return roleDao.save(role);
    }
}
