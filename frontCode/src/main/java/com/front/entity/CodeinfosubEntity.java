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
@Table(name="codeinfosub", schema="v1")
public class CodeinfosubEntity {
	
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer codeid;
	
	@Column
	private Integer postid;
	
	@Column
	private String codegenre;
	
	@Column
	private String src;

}
