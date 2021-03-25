package com.xywei.shiro.realm;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xywei.shiro.domain.User;
import com.xywei.shiro.service.UserService;

public class MyCustomRealm extends AuthorizingRealm {

	@Autowired
	private UserService userService;

	/**
	 * 以下就不从数据库从获取了，直接写死
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

		String username = (String) principals.getPrimaryPrincipal();

		System.out.println("username===" + username);
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

		// TODO 出大事了，没role角色权限的时候也能通过注解配置授权？？
		// Set<String> roles = new HashSet<String>();
		Set<String> roles = getRolesByUsername(username);
		// roles.add("user");
		// roles.add("admin");
		simpleAuthorizationInfo.setRoles(roles);

		// TODO 没admin:delete权限也可以授权？原因，授权配置在了错误的地方，正确地方应该在spring-mvc.xml
		// Set<String> stringPermissions = new HashSet<String>();
		Set<String> stringPermissions = getPermissionByUsername(username);
		// stringPermissions.add("user:select");
		// stringPermissions.add("admin:select");
		// stringPermissions.add("admin:delete");
		simpleAuthorizationInfo.setStringPermissions(stringPermissions);

		return simpleAuthorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		SimpleAuthenticationInfo simpleAuthenticationInfo = null;

		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
		String username = (String) usernamePasswordToken.getPrincipal();

		User user = userService.getUserByUsername(username);
		if (user == null) {
			return null;
		}
		String password = user.getPassword();
		String passwordSalt = user.getPasswordSalt();
		String realmName = "myCustomRealm";

		simpleAuthenticationInfo = new SimpleAuthenticationInfo(username, password, realmName);
		simpleAuthenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(passwordSalt));
		return simpleAuthenticationInfo;
	}

	private String getUserPasswordByUsername(String username) {
		User user = userService.getUserByUsername(username);
		return user == null ? null : user.getPassword();
	}

	private Set<String> getRolesByUsername(String username) {
		System.err.println("==================角色权限数据库中获取======================");
		Set<String> roles = new HashSet<String>();
		roles.add("user");
		// roles.add("admin");
		return roles;
	}

	private Set<String> getPermissionByUsername(String username) {
		System.err.println("==================资源权限数据库中获取======================");
		Set<String> stringPermissions = new HashSet<String>();
		stringPermissions.add("user:select");
		stringPermissions.add("admin:select");
		return stringPermissions;
	}

	@Test
	public void testMD5() {

		String algorithmName = "md5";
		String password = "a";// 8a43b3f31f33900f34eefa1861f59352
		String salt = "root";
		int hashIterations = 1;
		SimpleHash hash = new SimpleHash(algorithmName, password, salt, hashIterations);

		System.out.println(hash.toString());
	}
}
