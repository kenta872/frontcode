package com.front.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.front.entity.CodeinfoEntity;
import com.front.entity.FileinfoEntity;
import com.front.entity.PostinfoEntity;
import com.front.entity.TypedbEntity;


/**
 * パーツ種別管理テーブルDAO
 */
@Service
public class TypedbDao {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@PersistenceContext
	private EntityManager entityManager;
	@Value("${file.savedir}")
	private String dirname;
	

	/**
	 * パーツ種別をすべて取得する
	 * @return
	 * @throws Exception
	 */
	public List<TypedbEntity> selectTypedbAll() throws Exception {
		Query query = entityManager.createQuery("from TypedbEntity");
		return (List<TypedbEntity>)query.getResultList();
	}
	
	/**
	 * パーツ種別管理テーブルから指定するパーツ種別IDに紐づくレコードを取得する
	 * @param typeid パーツ種別ID
	 * @return
	 */
	public TypedbEntity findTypeByTypeid(Integer typeid) {
		Query typeQuery = entityManager.createQuery("from TypedbEntity where typeid = " + typeid);
		return (TypedbEntity)typeQuery.getSingleResult();
	}
}
