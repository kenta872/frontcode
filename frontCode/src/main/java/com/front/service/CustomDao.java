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

import com.front.controller.entity.CodeinfoEntity;
import com.front.controller.entity.FileinfoEntity;
import com.front.controller.entity.PostinfoEntity;
import com.front.controller.entity.TypedbEntity;


/**
 * カスタムDAO
 */
@Service
public class CustomDao {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@PersistenceContext
	private EntityManager entityManager;
	@Value("${file.savedir}")
	private String dirname;
	
	/**
	 * 投稿管理テーブルで未承認のデータを抽出する
	 * @return
	 */
	public List<Object[]> findPostByStatus() throws Exception {
		
		String sql = "select ";
			  sql += "  pi.postdate as postdate ";
			  sql += "  , td.typename as typename ";
			  sql += "  , pi.postid as postid ";
			  sql += "  , pi.typeid as typeid ";
			  sql += "from ";
			  sql += "  PostinfoEntity pi ";
			  sql +="    left outer join TypedbEntity td ";
			  sql += "	  on pi.typeid = td.typeid ";
			  sql += "where ";
			  sql += "  pi.status = 1";
			  sql += "  and pi.delFlg = 'false'";
		
		// 未承認(status=1)の投稿ID一覧を取得する
		Query authoQuery = entityManager.createQuery(sql);
		return authoQuery.getResultList();
		
	}
	

	/**
	 * 投稿管理テーブルで未承認のデータを抽出する
	 * @return
	 */
	public List<Object[]> findPostByStatusFilter(Integer typeid) throws Exception {
		
		String sql = "select ";
			  sql += "  pi.postdate as postdate ";
			  sql += "  , td.typename as typename ";
			  sql += "  , pi.postid as postid ";
			  sql += "  , pi.typeid as typeid ";
			  sql += "from ";
			  sql += "  PostinfoEntity pi ";
			  sql +="    left outer join TypedbEntity td ";
			  sql += "	  on pi.typeid = td.typeid ";
			  sql += "where ";
			  sql += "  pi.status = 1";
			  sql += "  and pi.typeid = " + typeid;
		
		// 未承認(status=1)の投稿ID一覧を取得する
		Query authoQuery = entityManager.createQuery(sql);
		return authoQuery.getResultList();
		
	}

	/**
	 * ソースコードテーブルから指定した投稿IDに紐づくソースコードを取得する。
	 */
	public String[] searchSrcByPostid(Integer postid,Integer typeid) throws Exception {
		String[] queryResult = new String[3];
		try {
			// HTMLソースコード取得
			Query codehtmlQuery = entityManager.createQuery("select src from CodeinfoEntity where codegenre = 'html' and postid = " + postid);
			queryResult[0] = (String)codehtmlQuery.getSingleResult();
			// CSSソースコード取得
			Query codecssQuery = entityManager.createQuery("select src from CodeinfoEntity where codegenre = 'css' and postid = " + postid);
			queryResult[1] = (String)codecssQuery.getSingleResult();
			// パーツ種別を取得
			Query typedbQuery = entityManager.createQuery("select typename from TypedbEntity where typeid = " + typeid);
			queryResult[2] = (String)typedbQuery.getSingleResult();
			

		} catch (Exception e) {
			throw e;
		}
		return queryResult;
	}
	
	/**
	 * ソースコードテーブルから指定した投稿IDに紐づくソースコードを取得する。
	 */
	public String[] searchSrcByPostidFromSub(Integer postid,Integer typeid) throws Exception {
		String[] queryResult = new String[4];
		try {
			// HTMLソースコード取得
			Query codehtmlQuery = entityManager.createQuery("select src from CodeinfosubEntity where codegenre = 'html' and postid = " + postid);
			queryResult[0] = (String)codehtmlQuery.getSingleResult();
			// CSSソースコード取得
			Query codecssQuery = entityManager.createQuery("select src from CodeinfosubEntity where codegenre = 'css' and postid = " + postid);
			queryResult[1] = (String)codecssQuery.getSingleResult();
			// パーツ種別を取得
			Query typedbQuery = entityManager.createQuery("select typename from TypedbEntity where typeid = " + typeid);
			queryResult[2] = (String)typedbQuery.getSingleResult();
			

		} catch (Exception e) {
			throw e;
		}
		return queryResult;
	}
}
