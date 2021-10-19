package com.front.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="postinfo", schema="v1")
public class PostinfoEntity {
	
	@Id
	@Column
	private Integer postid;
	
	@Column
	private String postdate;
	
	@Column
	private Integer typeid;
	
	@Column
	private int status;

}
