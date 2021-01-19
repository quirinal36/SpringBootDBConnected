package com.example.demo.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardVO {
	int id;
	int writer;
	String title;
	String content;
	Date wdate;
}
