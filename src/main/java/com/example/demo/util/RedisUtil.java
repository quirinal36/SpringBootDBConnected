package com.example.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class RedisUtil {
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	public String getData(String key) {
		ValueOperations<String, String>valueOperations = stringRedisTemplate.opsForValue();
		return valueOperations.get(key);
	}
	
	public void setData(String key, String value) {
		ValueOperations<String, String>valueOperations = stringRedisTemplate.opsForValue();
		valueOperations.set(key, value);
	}
}
