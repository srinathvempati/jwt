package com.sri.jwt.user.controller;

import com.sri.jwt.role.entity.Role;
import com.sri.jwt.user.entity.User;
import com.sri.jwt.user.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.annotation.PostConstruct;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostConstruct
    public void initRoleAndUser() {
        userService.initRoleAndUser();
   }

    @PostMapping({"/registerNewUser"})
    public User registerNewUser(@RequestBody User user) {
        return userService.registerNewUser(user);
    }
    
    @PutMapping({"/restPassword/{userName}"})
    public ResponseEntity<String> restPassword(@PathVariable String userName, @RequestBody User user) {
    	String newPassword = user.getUserPassword();
    	
    	if (newPassword == null || newPassword.isEmpty()) {
            return ResponseEntity.badRequest().body("Password cannot be empty");
        }
		 userService.updatePasswordDetails(userName, newPassword);
		 
		 return ResponseEntity.ok("Password updated successfully");
    	
    }
    
    @GetMapping({"/getRegisterUser"})
    public List<User> getRegisterUser(){
        return userService.findAll();
    }

    @GetMapping({"/forAdmin"})
    @PreAuthorize("hasRole('Admin')")
    public String forAdmin(){
        return "This URL is only accessible to the admin";
    }

    @GetMapping({"/forUser"})
    @PreAuthorize("hasRole('User')")
    public String forUser(){
        return "This URL is only accessible to the user";
    }
}
