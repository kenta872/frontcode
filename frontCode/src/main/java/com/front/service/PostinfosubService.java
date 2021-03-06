package com.front.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.front.controller.entity.PostinfosubEntity;
import com.front.controller.repository.PostinfosubRepository;

/**
 * 投稿管理テーブルDAO
 */
@Service
@Transactional
public class PostinfosubService {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	PostinfosubRepository postinfoSubRepos;

	/**
	 * 投稿一覧全取得
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<PostinfosubEntity> findPostAll() {

		Query postinfoQuery = entityManager.createQuery("from PostinfosubEntity");
		return postinfoQuery.getResultList();
	}

	/**
	 * 投稿情報テーブルから指定した投稿IDに紐づくレコードを取得する
	 * 
	 * @param postid 投稿ID
	 * @return
	 * @throws Exception
	 */
	public PostinfosubEntity findPostByPostid(Integer postid) {

		Query postinfoQuery = entityManager.createQuery("from PostinfosubEntity where postid = " + postid);
		return (PostinfosubEntity) postinfoQuery.getSingleResult();
	}

	/**
	 * 仮投稿情報を登録する
	 * 
	 * @param typeid パーツ種別ID
	 * @return
	 */
	public PostinfosubEntity insertPostinfo(Integer typeid) {

		PostinfosubEntity postinfosubEntity = new PostinfosubEntity();
		postinfosubEntity.setTypeid(typeid);
		// 投稿情報をDB登録
		postinfosubEntity = postinfoSubRepos.saveAndFlush(postinfosubEntity);

		return postinfosubEntity;
	}

	/**
	 * 対象エンティティをデータベースから削除する
	 * 
	 * @param postinfosubEntity 削除対象エンティティ
	 */
	public void rejectPostinfosub(PostinfosubEntity postinfosubEntity) {
		postinfoSubRepos.delete(postinfosubEntity);
	}

}
