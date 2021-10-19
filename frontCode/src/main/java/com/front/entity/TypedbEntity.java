package com.front.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="typedb", schema="v1")
public class TypedbEntity {
	
	@Id
	@Column
	private Integer typeid;
	
	@Column
	private String typename;
	
	@Column
	private String typenamejp;

}
