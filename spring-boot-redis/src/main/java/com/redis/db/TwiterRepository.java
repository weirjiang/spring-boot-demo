package com.redis.db;

import hash.DecoratingStringHashMapper;
import hash.HashMapper;
import hash.JacksonHashMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.redis.utils.KeyUtils;
import com.redis.utils.Post;
import com.redis.utils.Range;
import com.redis.web.RetwisSecurity;
import com.redis.web.WebPost;

@Repository
public class TwiterRepository {
	
	private static final Pattern MENTION_REGEX = Pattern.compile("@[\\w]+");
	@Autowired
	private RedisClientTemplate redisClientTemplate;
	private final HashMapper<Post, String, String> postMapper = new DecoratingStringHashMapper<Post>(new JacksonHashMapper<Post>(Post.class));
	public Long userIdCounter;

	/**
	 * userId seq
	 * 
	 * @return
	 */
	public Long nextUserIdCounter() {
		return incr(KeyUtils.globalUid());
	}

	public Long globalPid() {
		return incr(KeyUtils.globalPid());
	}

	public String addUser(String name, String pass) {
		String uid = String.valueOf(nextUserIdCounter());
		redisClientTemplate.set(KeyUtils.user(name), uid);// username -> uid
															// association

		redisClientTemplate.hset(KeyUtils.uid(uid), "name", name);// User info
		redisClientTemplate.hset(KeyUtils.uid(uid), "pass", pass);// User info
		redisClientTemplate.lPush(KeyUtils.users(), name);// user list
		String auth = addAuth(name);
		return auth;
	}

	public boolean isUserValid(String name) {
		return redisClientTemplate.exists(KeyUtils.user(name));
	}

	public boolean auth(String name, String pass) {
		String uid = redisClientTemplate.get(KeyUtils.user(name));
		if (StringUtils.hasText(uid)) {
			return redisClientTemplate.hget(KeyUtils.uid(uid), "pass").equals(pass);

		} else {
			return false;
		}
	}

	public Long incr(String key) {
		return redisClientTemplate.incr(key);
	}

	public String addAuth(String name) {
		String uid = redisClientTemplate.get(KeyUtils.user(name));
		// add random auth key relation
		String auth = UUID.randomUUID().toString();
		redisClientTemplate.set(KeyUtils.auth(uid), auth);
		redisClientTemplate.set(KeyUtils.authKey(auth), uid);
		return auth;
	}

	private String findName(String uid) {
		return redisClientTemplate.hget(KeyUtils.uid(uid), "name");
	}

	public String findUid(String name) {
		return redisClientTemplate.get(KeyUtils.user(name));
	}

	public String findNameForAuth(String value) {
		String uid = redisClientTemplate.get(KeyUtils.authKey(value));
		return findName(uid);
	}

	public void post(String name, WebPost post) {
		Post p = post.asPost();
		String uid = findUid(name);
		String pid = String.valueOf(globalPid());
		redisClientTemplate.hset(KeyUtils.post(pid), "content", p.getContent());
		redisClientTemplate.hset(KeyUtils.post(pid), "time", p.getTime());
		redisClientTemplate.hset(KeyUtils.post(pid), "uid", uid);
		redisClientTemplate.lPush(KeyUtils.posts(uid), pid);// posts
		redisClientTemplate.lPush(KeyUtils.timeline(uid), pid);// timeline
		
		String replyName = post.getReplyTo();
		if (StringUtils.hasText(replyName)) {
			String mentionUid = findUid(replyName);
			p.setReplyUid(mentionUid);
			// handle mentions below
			p.setReplyPid(post.getReplyPid());
		}
		
		// update followers
		for (String follower : getFollowersUid(uid)) {
			redisClientTemplate.lPush(KeyUtils.timeline(follower), pid);// followers  timeline
		}
		handleMentions(p, pid, replyName);
	}

	public void deleteAuth(String user) {
		String uid = findUid(user);

		String authKey = KeyUtils.auth(uid);
		String auth = redisClientTemplate.get(authKey);

		redisClientTemplate.del(KeyUtils.auth(uid));
		redisClientTemplate.del(KeyUtils.authKey(auth));
	}

	public List<String> newUsers(Range range) {
		return redisClientTemplate.lrange(KeyUtils.users(), range.begin, range.end);
	}

	public void follow(String name) {
		String targetUid = findUid(name);
		System.out.println("uid:" + RetwisSecurity.getUid());
		redisClientTemplate.sadd(KeyUtils.following(RetwisSecurity.getUid()), targetUid);
		redisClientTemplate.sadd(KeyUtils.followers(targetUid), RetwisSecurity.getUid());
	}

	public List<String> getFollowers(String uid) {
		Set<String> uidSet = redisClientTemplate.smembers(KeyUtils.followers(uid));
		List<String> followersName = new ArrayList<String>();
		for (String id : uidSet) {
			followersName.add(findName(id));
		}
		return followersName;
	}
	
	public List<String> getFollowersUid(String uid) {
		Set<String> uidSet = redisClientTemplate.smembers(KeyUtils.followers(uid));

		return new ArrayList<String>(uidSet);
	}

	public List<String> getFollowing(String uid) {
		Set<String> uidSet = redisClientTemplate.smembers(KeyUtils.following(uid));
		List<String> followersName = new ArrayList<String>();
		for (String id : uidSet) {
			followersName.add(findName(id));
		}
		return followersName;
	}

	public boolean isFollowing(String uid, String targetUid) {
		return redisClientTemplate.smembers(KeyUtils.followers(uid)).contains(targetUid);
	}

	public void stopFollowing(String targetUser) {
		String targetUid = findUid(targetUser);
		String uid = RetwisSecurity.getUid();

		redisClientTemplate.srem(KeyUtils.following(targetUid), uid);
		redisClientTemplate.srem(KeyUtils.followers(uid), targetUid);
	}

	public Collection<WebPost> timeline(Range range) {
		return convertPidsToPosts(KeyUtils.timeline(), range);
	}
	public List<WebPost> getPost(String pid) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		final String uid = "uid";
		final String content = "content";
		final String replyPid = "replyPid";
		final String replyUid = "replyUid";
		final String time = "time";
		map.put(content, redisClientTemplate.hget(KeyUtils.post(pid), content));
		map.put(time, redisClientTemplate.hget(KeyUtils.post(pid), time));
		map.put(uid, redisClientTemplate.hget(KeyUtils.post(pid), uid));
		return Collections.singletonList(convertPost(pid,map));
	}
	
	private List<WebPost> convertPidsToPosts(String key, Range range) {
		String pid = "pid:*->";
		final String pidKey = "#";
		final String uid = "uid";
		final String content = "content";
		final String replyPid = "replyPid";
		final String replyUid = "replyUid";
		final String time = "time";
		List<String> pids = redisClientTemplate.lrange(KeyUtils.posts(RetwisSecurity.getUid()), range.begin, range.end);
		List<WebPost> sort = new ArrayList<WebPost>();
		for (String postId : pids) {
			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put(content, redisClientTemplate.hget(KeyUtils.post(postId), content));
			map.put(time, redisClientTemplate.hget(KeyUtils.post(postId), time));
			map.put(uid, redisClientTemplate.hget(KeyUtils.post(postId), uid));
			sort.add(convertPost(postId, map));
		}

		return sort;
	}

	private WebPost convertPost(String pid, Map hash) {
		Post post = postMapper.fromHash(hash);
		WebPost wPost = new WebPost(post);
		wPost.setPid(pid);
		wPost.setName(findName((String) hash.get("uid")));
		// wPost.setReplyTo(findName(post.getReplyUid()));
		// wPost.setContent(replaceReplies(post.getContent()));
		return wPost;
	}
	
	public boolean isPostValid(String pid){
		return redisClientTemplate.exists(KeyUtils.post(pid));
	}
	
	private void handleMentions(Post post, String pid, String name) {
		// find mentions
		Collection<String> mentions = findMentions(post.getContent());

		for (String mention : mentions) {
			String uid = findUid(mention);
			if (uid != null) {
				redisClientTemplate.lPush(KeyUtils.mentions(uid), pid);
			}
		}
	}
	
	public static Collection<String> findMentions(String content) {
		Matcher regexMatcher = MENTION_REGEX.matcher(content);
		List<String> mentions = new ArrayList<String>(4);

		while (regexMatcher.find()) {
			mentions.add(regexMatcher.group().substring(1));
		}

		return mentions;
	}
	

	public List<WebPost> getMentions(String uid, Range range) {
		return convertPidsToPosts(KeyUtils.mentions(uid), range);
	}
	
	public List<String> alsoFollowed(String targetUid, String uid ) {
		List<String> alsoFollowedName = new ArrayList<String>();
		List<String> targetUidFollowerList = getFollowersUid(uid);
		for(String id1:targetUidFollowerList){
			alsoFollowedName.add(findName(id1));
			}
		return alsoFollowedName;
	}
	
	public List<String> commonFollowers(String uid, String targetUid) {
		List<String> commonFollowers = new ArrayList<String>();
		List<String> uidFollowerList = getFollowers(uid);
		List<String> targetUidFollowerList = getFollowers(targetUid);
		for(String id1:targetUidFollowerList){
			for(String id2:uidFollowerList){
				if(id1.equals(id2)){
					commonFollowers.add(findName(id1));
				}
			}
		}
		return commonFollowers;
	}
}
