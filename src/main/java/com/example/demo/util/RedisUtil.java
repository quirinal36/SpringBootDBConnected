package com.example.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RedisUtil {
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	public String getData(String key) {
		ValueOperations<String, String>valueOperations = stringRedisTemplate.opsForValue();
		return valueOperations.get(key);
	}
	
	public void setData(String key, String value, long duration) {
		ValueOperations<String, String>valueOperations = stringRedisTemplate.opsForValue();
		valueOperations.set(key, value, duration);
	}
}
