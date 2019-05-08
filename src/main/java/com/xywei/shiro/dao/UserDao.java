package com.xywei.shiro.dao;

import com.xywei.shiro.domain.User;

public interface UserDao {

	User getUserByUsername(String username);

}
