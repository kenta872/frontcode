package com.front.controller.entity;

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
@Table(name="favoriteinfo", schema="v1")
public class FavoriteEntity {
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer favoriteid;
	
	@Column
	private Integer accountid;
	
	@Column
	private Integer postid;
}
