package com.xywei.shiro.service.impl;

import static org.junit.Assert.*;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;
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
	
	@Test
	public void testPassword() throws Exception {
		Md5Hash md5Hash=new Md5Hash("a", "a", 1);
		System.out.println(md5Hash.toString());
	}

}
