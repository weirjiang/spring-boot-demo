package com.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 
 * @author vic
 * @desc redis config bean
 *
 */
@Configuration
public class RedisConfig {
	@Bean(name = "jedis.pool")
	@Autowired
	public JedisPool jedisPool(@Qualifier("jedis.pool.config") JedisPoolConfig config, @Value("${jedis.pool.host}") String host,
			@Value("${jedis.pool.port}") int port) {
		System.out.println(host);
		return new JedisPool(config, host, port);
	}

	@Bean(name = "jedis.pool.config")
	public JedisPoolConfig jedisPoolConfig(@Value("${jedis.pool.maxTotal}") int maxTotal, @Value("${jedis.pool.maxIdle}") int maxIdle,
			@Value("${jedis.pool.maxWaitMillis}") int maxWaitMillis) {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(maxTotal);
		config.setMaxIdle(maxIdle);
		config.setMaxWaitMillis(maxWaitMillis);
		return config;
	}
}