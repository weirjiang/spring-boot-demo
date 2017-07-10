package com.redis.db;

import org.springframework.stereotype.Repository;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;
@Repository
public interface JedisDataSource {
	public Jedis getRedisClient();
	public void returnResource(Jedis shardedJedis);
	public void returnResource(Jedis shardedJedis,boolean broken);
}
