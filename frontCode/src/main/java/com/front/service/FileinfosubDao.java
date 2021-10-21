package com.front.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.front.controller.entity.FileinfoEntity;
import com.front.controller.entity.FileinfosubEntity;


/**
 * ファイル情報管理テーブルDAO
 */
@Service
public class FileinfosubDao {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@PersistenceContext
	private EntityManager entityManager;
	
	/**
	 * すべてのファイル情報を取得する
	 * @return
	 */
	public List<FileinfosubEntity> selectFileAll() {
		Query Query = entityManager.createQuery("from FileinfosubEntity");
		return Query.getResultList();
	}
 	/**
	 * ファイル情報テーブルから指定した投稿IDに紐づくファイル情報を取得する
	 * @param postid 投稿ID
	 * @return
	 * @throws Exception
	 */
	public List<FileinfosubEntity> findFileByPostid(Integer postid) throws Exception {

		Query Query = entityManager.createQuery("from FileinfosubEntity where postid = " + postid);
		return Query.getResultList();
	}
	
	/**
	 * 投稿IDとファイル種別で抽出する
	 * @param postid 投稿ID
	 * @param filetype ファイル種別
	 * @return
	 */
	public FileinfosubEntity findFileByPostid(Integer postid, String filetype) {

		Query filehtmlQuery = entityManager.createQuery("from FileinfosubEntity where filegenre = '" + filetype + "' and postid = " + postid);
		return (FileinfosubEntity)filehtmlQuery.getSingleResult();
	}
}
