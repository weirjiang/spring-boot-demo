package com.shiro.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shiro.domain.Menu;
import com.shiro.domain.Role;
import com.shiro.domain.SystemUser;

public class ShiroRealm extends AuthorizingRealm {
	@Autowired
	private UserService userService;
	@Autowired
	private UserRoleService userRoleService;
	@Autowired
	private MenuService menuService;

	private static final Logger logger = Logger.getLogger(ShiroRealm.class);

	// 授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		SystemUser user = userService.findByUserName((String) principals.getPrimaryPrincipal());
//		SecurityUtils.getSubject().getSession().setAttribute(String.valueOf(user.getId()), SecurityUtils.getSubject().getPrincipals());
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		List<Role> roleList = userRoleService.findRoleByUserId(String.valueOf(user.getId()));
		// 赋予角色
		for (Role role : roleList) {
			info.addRole(role.getName());
		}
		// 赋予权限
		List<Menu> menuList = menuService.findByUid(String.valueOf(user.getId()));
		for (Menu menu : menuList) {
			info.addStringPermission(menu.getUrl().trim());
		}
		return info;
	}

	// 身份认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// TODO Auto-generated method stub
		logger.info("doGetAuthenticationInfo" + token.toString());
		UsernamePasswordToken authenticationToken = (UsernamePasswordToken) token;
		String userName = authenticationToken.getUsername();
		logger.info(userName + authenticationToken.getPassword());

		SystemUser user = userService.findByUserName(userName);
		if (user != null) {
			Session session = SecurityUtils.getSubject().getSession();
			session.setAttribute("userSession", user);
			session.setAttribute("userSessionId", user.getId());
			return new SimpleAuthenticationInfo(userName, user.getPassword(), getName());
		} else {
			return null;
		}
	}

}
