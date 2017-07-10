package com.redis.db;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.compat.JreCompat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedis;

@Repository
public class JedisDataSourceImpl implements JedisDataSource{
	Logger logger = Logger.getLogger(JedisDataSourceImpl.class);
	@Autowired
//	private ShardedJedisPool shardedJedisPool;
	private JedisPool shardedJedisPool;
	public Jedis getRedisClient() {
		logger.info("[JedisDS] getRedisClent");
		Jedis shardedJedis = null;
		shardedJedis = shardedJedisPool.getResource();
		return shardedJedis;
	}

	public void returnResource(Jedis shardedJedis) {
		shardedJedis.disconnect();
		shardedJedisPool.returnBrokenResource(shardedJedis);
	}

	public void returnResource(Jedis shardedJedis, boolean broken) {
		// TODO Auto-generated method stub
		shardedJedis.close();
	}
}
