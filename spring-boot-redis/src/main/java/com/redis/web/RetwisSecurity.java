package com.redis.web;

import org.springframework.core.NamedThreadLocal;
import org.springframework.util.StringUtils;

public class RetwisSecurity {
	private static class UserInfo {
		String name;
		String uid;
	}

	private static final ThreadLocal<UserInfo> user = new NamedThreadLocal<UserInfo>("Retwis-id");

	public static String getName() {
		UserInfo userInfo = user.get();
		return (userInfo != null ? userInfo.name : null);
	}

	public static String getUid() {
		UserInfo userInfo = user.get();
		return (userInfo != null ? userInfo.uid : null);
	}

	public static void setUser(String name, String uid) {
		UserInfo userInfo = new UserInfo();
		userInfo.name = name;
		userInfo.uid = uid;
		user.set(userInfo);
	}

	public static boolean isUserSignedIn(String name) {
		UserInfo userInfo = user.get();
		return userInfo != null && userInfo.name.equals(name);
	}

	public static boolean isSignedIn() {
		return StringUtils.hasText(getName());
	}

	public static void clean() {
		user.set(null);
	}
}
