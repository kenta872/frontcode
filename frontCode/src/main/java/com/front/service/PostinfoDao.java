package com.front.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.front.controller.entity.PostinfoEntity;


/**
 * 投稿管理テーブルDAO
 */
@Service
public class PostinfoDao {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@PersistenceContext
	private EntityManager entityManager;
	
	/**
	 * 投稿一覧をすべて取得
	 * @return 投稿一覧
	 */
	public List<PostinfoEntity> findPostAll() {
		Query postinfoQuery = entityManager.createQuery("from PostinfoEntity where delFlg = 'false'");
		return postinfoQuery.getResultList();
	}

	
	/**
	 * 投稿情報テーブルから指定した投稿IDに紐づくレコードを取得する
	 * @param postid 投稿ID
	 * @return
	 * @throws Exception
	 */
	public PostinfoEntity findPostByPostid(Integer postid) {
		
		Query postinfoQuery = entityManager.createQuery("from PostinfoEntity where delFlg = 'false' and postid = " + postid);
		return (PostinfoEntity)postinfoQuery.getSingleResult();
	}
	
	
	/**
	 * 指定したステータスの投稿を取得する
	 * @param status 投稿テーブルステータス
	 * @return
	 * @throws Exception
	 */
	public List<PostinfoEntity> findPostByStatus(Integer status) throws Exception {
		
		Query postinfoQuery = entityManager.createQuery("from PostinfoEntity where delFlg = 'false' and status = " + status);
		return postinfoQuery.getResultList();
	}
	
	/**
	 * 削除フラグがtrueの投稿一覧を取得
	 * @return 削除フラグtrueの投稿一覧
	 */
	public List<PostinfoEntity> findPostByDelflg() {
		Query postinfoQuery = entityManager.createQuery("from PostinfoEntity where delFlg = 'true'");
		return postinfoQuery.getResultList();
	}

}
