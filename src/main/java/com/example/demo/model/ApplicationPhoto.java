package com.example.demo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class ApplicationPhoto {
	int applicationId;
	int photoId;
	
	@Builder
	public ApplicationPhoto(int aid, int pid) {
		this.applicationId = aid;
		this.photoId = pid;
	}
}
