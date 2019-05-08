package com.xywei.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
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

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
		String username = (String) usernamePasswordToken.getPrincipal();
		// 模拟从数据库/缓存中取出密码
		// String password = "cd92a26534dba48cd785cdcc0b3e6bd1";
		//get password from database
		String password = getUserPasswordByUsername(username);
		// String password = "admin";
		String realmName = "myCustomRealm";
		SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(username, password, realmName);
		simpleAuthenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("root"));
		return simpleAuthenticationInfo;
	}

	private String getUserPasswordByUsername(String username) {
		User user = userService.getUserByUsername(username);
		return user == null ? null : user.getPassword();
	}

	@Test
	public void testMD5() {

		String algorithmName = "md5";
		String password = "admin";
		String salt = "root";
		int hashIterations = 1;
		SimpleHash hash = new SimpleHash(algorithmName, password, salt, hashIterations);

		System.out.println(hash.toString());
	}
}
