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
@Table(name="codegenre", schema="v1")
public class CodegenreEntity {

	@Id
	@Column
	private String genre;
}
