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
@Table(name="fileinfosub", schema="v1")
public class FileinfosubEntity {

	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer fileid;
	
	@Column
	private String filename;
	
	@Column
	private Integer postid;
	
	@Column
	private String filegenre;
}
