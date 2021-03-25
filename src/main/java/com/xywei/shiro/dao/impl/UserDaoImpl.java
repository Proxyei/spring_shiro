package com.xywei.shiro.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.xywei.shiro.dao.UserDao;
import com.xywei.shiro.domain.User;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public User getUserByUsername(String username) {

		String sql = "select * from user where username=?";

		User user = null;
		try {
			user = jdbcTemplate.queryForObject(sql, new String[] { username }, new RowMapper<User>() {

				@Override
				public User mapRow(ResultSet rs, int rowNum) throws SQLException {
					if (rs.getRow() == 0) {
						return null;
					}
					User user = new User();
					user.setId(rs.getInt("id"));
					user.setUsername(rs.getString("username"));
					user.setPassword(rs.getString("password"));
					user.setPasswordSalt(rs.getString("password_salt"));
					return user;
				}
			});

		} catch (Exception e) {
		}

		return user;
	}

}
