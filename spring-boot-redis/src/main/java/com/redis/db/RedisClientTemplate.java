package com.redis.db;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Jedis;

@Repository
public class RedisClientTemplate {
	private final Logger logger = Logger.getLogger(RedisClientTemplate.class);
	@Autowired
	private JedisDataSource jedisDataSource;

	public void disConnect() {
		Jedis Jedis = jedisDataSource.getRedisClient();
		Jedis.close();

	}

	public Jedis getJedis() {
		return jedisDataSource.getRedisClient();
	}

	public void closeJedis(Jedis Jedis) {
		jedisDataSource.returnResource(Jedis);
	}

	/**
	 * 设置单个值
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public String set(String key, String value) {
		String result = null;

		Jedis Jedis = jedisDataSource.getRedisClient();
		if (Jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = Jedis.set(key, value);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;
		} finally {
			jedisDataSource.returnResource(Jedis, broken);
		}

		return result;
	}

	/**
	 * 获取单个值
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key) {
		String result = null;
		Jedis Jedis = jedisDataSource.getRedisClient();
		if (Jedis == null) {
			return result;
		}

		boolean broken = false;
		try {
			result = Jedis.get(key);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;
		} finally {
			jedisDataSource.returnResource(Jedis, broken);
		}

		return result;
	}

	public Boolean exists(String key) {
		Boolean result = false;
		Jedis Jedis = jedisDataSource.getRedisClient();
		if (Jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = Jedis.exists(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;
		} finally {
			jedisDataSource.returnResource(Jedis, broken);
		}

		return result;
	}

	public String type(String key) {
		String result = null;
		Jedis Jedis = jedisDataSource.getRedisClient();
		if (Jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = Jedis.type(key);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;
		} finally {
			jedisDataSource.returnResource(Jedis, broken);
		}
		return result;
	}

	/**
	 * 在某段时间后失效
	 * 
	 * @param key
	 * @param seconds
	 * @return
	 */
	public Long expire(String key, int seconds) {
		Long result = null;
		Jedis Jedis = jedisDataSource.getRedisClient();
		if (Jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = Jedis.expire(key, seconds);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;
		} finally {
			jedisDataSource.returnResource(Jedis, broken);
		}
		return result;
	}

	/**
	 * 在某个时间点失效
	 * 
	 * @param key
	 * @param unixTime
	 * @return
	 */
	public Long expireAt(String key, long unixTime) {
		Long result = null;
		Jedis Jedis = jedisDataSource.getRedisClient();
		if (Jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = Jedis.expireAt(key, unixTime);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;
		} finally {
			jedisDataSource.returnResource(Jedis, broken);
		}
		return result;
	}

	public Long ttl(String key) {
		Long result = null;
		Jedis Jedis = jedisDataSource.getRedisClient();
		if (Jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = Jedis.ttl(key);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;
		} finally {
			jedisDataSource.returnResource(Jedis, broken);
		}
		return result;
	}

	public boolean setbit(String key, long offset, boolean value) {

		Jedis Jedis = jedisDataSource.getRedisClient();
		boolean result = false;
		if (Jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = Jedis.setbit(key, offset, value);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;
		} finally {
			jedisDataSource.returnResource(Jedis, broken);
		}
		return result;
	}

	public boolean getbit(String key, long offset) {
		Jedis Jedis = jedisDataSource.getRedisClient();
		boolean result = false;
		if (Jedis == null) {
			return result;
		}
		boolean broken = false;

		try {
			result = Jedis.getbit(key, offset);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;
		} finally {
			jedisDataSource.returnResource(Jedis, broken);
		}
		return result;
	}

	public long setRange(String key, long offset, String value) {
		Jedis Jedis = jedisDataSource.getRedisClient();
		long result = 0;
		if (Jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = Jedis.setrange(key, offset, value);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;
		} finally {
			jedisDataSource.returnResource(Jedis, broken);
		}
		return result;
	}

	public String getRange(String key, long startOffset, long endOffset) {
		Jedis Jedis = jedisDataSource.getRedisClient();
		String result = null;
		if (Jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = Jedis.getrange(key, startOffset, endOffset);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;
		} finally {
			jedisDataSource.returnResource(Jedis, broken);
		}
		return result;
	}

	/**
	 * seq
	 * 
	 * @param key
	 * @return
	 */
	public Long incr(String key) {

		Jedis Jedis = null;
		long seq = 0;
		try {
			Jedis = jedisDataSource.getRedisClient();
			seq = Jedis.incr(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			jedisDataSource.returnResource(Jedis);
		}

		return seq;
	}

	public String hmset(String key, Map<String, String> hash) {
		Jedis Jedis = null;
		String result = "";
		try {
			Jedis = getJedis();
			result = Jedis.hmset(key, hash);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			jedisDataSource.returnResource(Jedis);
		}

		return result;
	}

	/**
	 * hash
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public Long hset(String key, String field, String value) {
		Jedis Jedis = null;
		long result = 0;
		try {
			Jedis = getJedis();
			result = Jedis.hset(key, field, value);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			jedisDataSource.returnResource(Jedis);
		}

		return result;
	}

	public String hget(String key, String field) {
		String result = "";
		Jedis Jedis = getJedis();
		try {
			result = Jedis.hget(key, field);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			jedisDataSource.returnResource(Jedis);
		}

		return result;
	}

	/**
	 * list push
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long lPush(String key, String value) {
		Jedis Jedis = null;
		long result = 0;
		try {
			Jedis = getJedis();
			result = Jedis.lpush(key, value);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			jedisDataSource.returnResource(Jedis);
		}

		return result;
	}

	public Long del(String key) {
		Jedis Jedis = null;
		long result = 0;
		try {
			Jedis = getJedis();
			result = Jedis.del(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			jedisDataSource.returnResource(Jedis);
		}

		return result;
	}

	public List<String> lrange(String key, long start, long end) {
		List<String> list = new ArrayList<String>();
		Jedis jedis = null;
		try {
			jedis = getJedis();
			list = jedis.lrange(key, start, end);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			jedisDataSource.returnResource(jedis);
		}
		return list;
	}

	public Long sadd(String key, String... members) {
		Jedis Jedis = null;
		long result = 0;
		try {
			Jedis = getJedis();
			result = Jedis.sadd(key, members);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			jedisDataSource.returnResource(Jedis);
		}
		return result;
	}

	public Set<String> smembers(String key) {
		Set<String> set = new HashSet<String>();
		Jedis jedis = null;
		try {
			jedis = getJedis();
			set = jedis.smembers(key);
		} catch (Exception e) {

			logger.error(e.getMessage(), e);
		} finally {
			jedisDataSource.returnResource(jedis);
		}
		return set;
	}

	public long srem(String key, String... members) {
		Jedis jedis = null;
		long result = 0;
		try {
			jedis = getJedis();
			result = jedis.srem(key, members);
		} catch (Exception e) {

			logger.error(e.getMessage(), e);
		} finally {
			jedisDataSource.returnResource(jedis);
		}
		return result;
	}
}
