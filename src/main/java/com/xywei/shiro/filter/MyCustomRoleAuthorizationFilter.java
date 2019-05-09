package com.xywei.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.springframework.util.StringUtils;

public class MyCustomRoleAuthorizationFilter extends AuthorizationFilter {


	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		// 访问的主体
		Subject subject = getSubject(request, response);
		// 访问该URL需要的权限角色
		String[] roles = (String[]) mappedValue;
		// 如果roles是空，表示访问该url不需要权限
		if (StringUtils.isEmpty(roles)) {
			return true;
		}
		return subject.hasAllRoles(CollectionUtils.asSet(roles));
	}

}
