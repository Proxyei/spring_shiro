package com.xywei.shiro.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xywei.shiro.domain.User;

@Controller
public class UserController {

	@RequestMapping(value = "/userLogin", method = RequestMethod.POST)
	@ResponseBody
	public String userLogin(User user) {

		System.out.println(user);
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getUsername(), user.getPassword());

		try {
			subject.login(usernamePasswordToken);
		} catch (AuthenticationException e) {
			return e.getMessage();
		}

		return "登录成功" + user;
	}

	@RequiresRoles("user")
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	@ResponseBody
	public String user() {
		return "有user 权限";
	}

	@RequiresRoles("admin")
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	@ResponseBody
	public String admin() {
		return "有admin 权限";
	}

	@RequiresPermissions("admin:delete")
	@RequestMapping(value = "/adminDelete", method = RequestMethod.GET)
	@ResponseBody
	public String adminDelete() {
		return "有admin:delete权限";
	}

	@RequiresPermissions("admin:select")
	@RequestMapping(value = "/adminSelect", method = RequestMethod.GET)
	@ResponseBody
	public String adminSelect() {
		return "有admin:select 权限";
	}

	@RequiresPermissions("user:select")
	@RequestMapping(value = "/userSelect", method = RequestMethod.GET)
	@ResponseBody
	public String userSelect() {
		return "有user:select 权限";
	}
}
