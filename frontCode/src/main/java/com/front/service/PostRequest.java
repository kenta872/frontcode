package com.front.service;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class PostRequest {
	
	private Integer typeid;
	
	private String srchtml;
	
	private String srccss;
	
	private String srcjs;

}
