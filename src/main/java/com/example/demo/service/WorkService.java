package com.example.demo.service;

import java.util.List;

public interface WorkService <T> {
	public List<T> selectAll();
	public T selectOne(T input);
	public int insert(T input);
}
