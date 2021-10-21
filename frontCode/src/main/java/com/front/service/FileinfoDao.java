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
 * ファイル情報管理テーブルDAO
 */
@Service
public class FileinfoDao {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@PersistenceContext
	private EntityManager entityManager;
	
	/**
	 * すべてのファイル情報を取得する
	 * @return
	 */
	public List<FileinfoEntity> selectFileAll() {
		Query Query = entityManager.createQuery("from FileinfoEntity where delFlg = 'false'");
		return Query.getResultList();
	}
 	/**
	 * ファイル情報テーブルから指定した投稿IDに紐づくファイル情報を取得する
	 * @param postid 投稿ID
	 * @return
	 * @throws Exception
	 */
	public List<FileinfoEntity> findFileByPostid(Integer postid) throws Exception {

		Query Query = entityManager.createQuery("from FileinfoEntity where delFlg = 'false' and postid = " + postid);
		return Query.getResultList();
	}
	
	/**
	 * 投稿IDとファイル種別で抽出する
	 * @param postid 投稿ID
	 * @param filetype ファイル種別
	 * @return
	 */
	public FileinfoEntity findFileByPostid(Integer postid, String filetype) {

		Query filehtmlQuery = entityManager.createQuery("from FileinfoEntity where delFlg = 'false' and filegenre = '" + filetype + "' and postid = " + postid);
		return (FileinfoEntity)filehtmlQuery.getSingleResult();
	}
	
	
	public List<FileinfoEntity> findFileByDelflg() {
		Query filehtmlQuery = entityManager.createQuery("from FileinfoEntity where delFlg = 'true'");
		return filehtmlQuery.getResultList();
	}
}
