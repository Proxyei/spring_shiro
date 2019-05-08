package com.xywei.shiro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xywei.shiro.dao.UserDao;
import com.xywei.shiro.domain.User;
import com.xywei.shiro.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Override
	public User getUserByUsername(String username) {
		return userDao.getUserByUsername(username);
	}

}
