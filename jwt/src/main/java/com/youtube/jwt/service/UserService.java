package com.youtube.jwt.service;

import com.youtube.jwt.dao.RoleDao;
import com.youtube.jwt.dao.UserDao;
import com.youtube.jwt.entity.Role;
import com.youtube.jwt.entity.SoftwareCompanies;
import com.youtube.jwt.entity.User;
import com.youtube.jwt.exception.CompanyNameNotFoundException;
import com.youtube.jwt.exception.UserNameExistException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private static boolean updatePassword = false;
	private static boolean cretaeNewUser = false;

	public void initRoleAndUser() {

		Role adminRole = new Role();
		adminRole.setRoleName("Admin");
		adminRole.setRoleDescription("Admin roles");
		roleDao.save(adminRole);

		Role userRole = new Role();
		userRole.setRoleName("User");
		userRole.setRoleDescription("Default role for newly created record");
		roleDao.save(userRole);

		User adminUser = new User();
		adminUser.setUserName("admin");
		adminUser.setUserPassword(getEncodedPassword("admin"));
		adminUser.setUserFirstName("admin");
		adminUser.setUserLastName("admin");

		Set<Role> adminRoles = new HashSet<>();
		adminRoles.add(adminRole);
		adminUser.setRole(adminRoles);
		userDao.save(adminUser);

	}

	// create new user
	public User registerNewUser(User user) {
		String userNameDetails = user.getUserName();
		cretaeNewUser = true;

		if (checkuser(userNameDetails) && cretaeNewUser) {

			Role role = roleDao.findById("User").get();
			Set<Role> userRoles = new HashSet<>();
			userRoles.add(role);
			user.setRole(userRoles);
			user.setUserPassword(getEncodedPassword(user.getUserPassword()));

			return userDao.save(user);
		}
		return null;

	}

	// update password
	public User updatePasswordDetails(String userName, String password) {
		
		User user = userDao.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));
		
		user.setUserPassword(getEncodedPassword(password));

        // Save the updated user
        return userDao.save(user);
	}

	private boolean checkuser(String userNameDetails) {
		Optional<User> softwareCompanies = userDao.findById(userNameDetails);

		if (softwareCompanies.isEmpty() && cretaeNewUser) {
			System.out.println("*********** user name is Empty this is for Create user **********");
			return true;
		} else if (softwareCompanies != null && updatePassword) {
			System.out.println("*********** password update Logic **********");
			return true;
		}
		throw new UserNameExistException("userName :" + userNameDetails);
	}

	public String getEncodedPassword(String password) {
		return passwordEncoder.encode(password);
	}

	public List<User> findAll() {
		List<User> usr = new ArrayList<>();
		usr.add((User) userDao.findAll());

		return usr;
	}

}
