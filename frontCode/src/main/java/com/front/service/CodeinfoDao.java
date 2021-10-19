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
 * ソースコードテーブル操作DAO
 */
@Service
public class CodeinfoDao {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@PersistenceContext
	private EntityManager entityManager;
	
	/**
	 * すべてのソースコードを取得する
	 * @return
	 */
	public List<CodeinfoEntity> selectCodeAll() {
		Query Query = entityManager.createQuery("from CodeinfoEntity");
		return Query.getResultList();
	}
	/**
	 * ソースコード管理テーブルから投稿IDとソースジャンルに紐づくレコードを抽出する
	 * 
	 * @param postid 投稿ID
	 * @param codegenre ソースコードのジャンル
	 * @return 投稿IDとソースジャンルで絞り込んだレコード
	 * @throws Exception
	 */
	public CodeinfoEntity searchSrcByPostid(Integer postid, String codegenre) throws Exception {

		Query Query = entityManager.createQuery("from CodeinfoEntity where codegenre = '" + codegenre +"' and postid = " + postid);
		return (CodeinfoEntity)Query.getSingleResult();
	}
	
	/**
	 * ソースコードテーブルから指定する投稿IDに紐づくソースを取得する
	 * 
	 * @param postid 投稿ID
	 * @return 投稿IDで絞り込んだレコード
	 * @throws Exception
	 */
	public List<CodeinfoEntity> findSrcByPostid(Integer postid) throws Exception {
		
		Query Query = entityManager.createQuery("from CodeinfoEntity where postid = " + postid);
		return Query.getResultList();
	}

	
	public String findCodeByPostid(Integer postid, String codeType) {
		// ソースコードを取得
		Query codeinfoQuery = entityManager.createQuery("select src from CodeinfoEntity where codegenre = '" + codeType +"' and postid = " + postid);
		String srccode = (String)codeinfoQuery.getSingleResult();
		
		return srccode;
	}
}
