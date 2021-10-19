package com.front.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.front.entity.PostinfoEntity;
import com.front.entity.PostinfosubEntity;


/**
 * 投稿管理テーブルDAO
 */
@Service
public class PostinfosubDao {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@PersistenceContext
	private EntityManager entityManager;

	
	/**
	 * 投稿情報テーブルから指定した投稿IDに紐づくレコードを取得する
	 * @param postid 投稿ID
	 * @return
	 * @throws Exception
	 */
	public PostinfosubEntity findPostByPostid(Integer postid) throws Exception {
		
		Query postinfoQuery = entityManager.createQuery("from PostinfosubEntity where postid = " + postid);
		return (PostinfosubEntity)postinfoQuery.getSingleResult();
	}

}
