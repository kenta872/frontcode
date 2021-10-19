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
 * 投稿管理テーブルDAO
 */
@Service
public class PostinfoDao {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@PersistenceContext
	private EntityManager entityManager;

	
	/**
	 * 投稿情報テーブルから指定した投稿IDに紐づくレコードを取得する
	 * @param postid 投稿ID
	 * @return
	 * @throws Exception
	 */
	public PostinfoEntity findPostByPostid(Integer postid) throws Exception {
		
		Query postinfoQuery = entityManager.createQuery("from PostinfoEntity where postid = " + postid);
		return (PostinfoEntity)postinfoQuery.getSingleResult();
	}
	
	
	/**
	 * 指定したステータスの投稿を取得する
	 * @param status 投稿テーブルステータス
	 * @return
	 * @throws Exception
	 */
	public List<PostinfoEntity> findPostByStatus(Integer status) throws Exception {
		
		Query postinfoQuery = entityManager.createQuery("from PostinfoEntity where status = " + status);
		return postinfoQuery.getResultList();
	}

}
