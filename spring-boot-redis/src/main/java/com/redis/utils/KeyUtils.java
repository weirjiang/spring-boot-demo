package com.redis.utils;

public class KeyUtils {
	public static final String UID = "uid:";

	public static String following(String uid) {
		return UID + uid + ":following";
	}

	public static String followers(String uid) {
		return UID + uid + ":followers";
	}

	public static String timeline(String uid) {
		return UID + uid + ":timeline";
	}

	public static String mentions(String uid) {
		return UID + uid + ":mentions";
	}

	public static String posts(String uid) {
		return UID + uid + ":posts";
	}

	public static String auth(String uid) {
		return UID + uid + ":auth";
	}

	public static String uid(String uid) {
		return UID + uid;
	}

	public static String post(String pid) {
		return "pid:" + pid;
	}

	public static String authKey(String auth) {
		return "auth:" + auth;
	}

	public static String user(String name) {
		return "user:" + name + ":uid";
	}

	public static String users() {
		return "users";
	}

	public static String timeline() {
		return "timeline";
	}

	public static String globalUid() {
		return "global:uid";
	}

	public static String globalPid() {
		return "global:pid";
	}

	public static String alsoFollowed(String uid, String targetUid) {
		return UID + uid + ":also:uid:" + targetUid;
	}

	public static String commonFollowers(String uid, String targetUid) {
		return UID + uid + ":common:uid:" + targetUid;
	}
}
