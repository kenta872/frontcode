package com.front.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.front.controller.entity.PostinfoEntity;
import com.front.controller.repository.PostinfoRepository;
import com.front.util.Constants;

/**
 * 投稿管理テーブルDAO
 */
@Service
@Transactional
public class PostinfoService {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	PostinfoRepository postinfoRepos;

	/**
	 * 投稿一覧をすべて取得
	 * 
	 * @return 投稿一覧
	 */
	public List<PostinfoEntity> findPostAll() {
		Query postinfoQuery = entityManager.createQuery("from PostinfoEntity where delFlg = 'false'");
		return postinfoQuery.getResultList();
	}

	/**
	 * 投稿情報テーブルから指定した投稿IDに紐づくレコードを取得する
	 * 
	 * @param postid 投稿ID
	 * @return
	 * @throws Exception
	 */
	public PostinfoEntity findPostByPostid(Integer postid) {

		Query postinfoQuery = entityManager
				.createQuery("from PostinfoEntity where delFlg = 'false' and postid = " + postid);
		return (PostinfoEntity) postinfoQuery.getSingleResult();
	}

	/**
	 * 指定したステータスの投稿を取得する
	 * 
	 * @param status 投稿テーブルステータス
	 * @return
	 * @throws Exception
	 */
	public List<PostinfoEntity> findPostByStatus(Integer status) {

		Query postinfoQuery = entityManager
				.createQuery("from PostinfoEntity where delFlg = 'false' and status = " + status);
		return postinfoQuery.getResultList();
	}

	/**
	 * 削除フラグがtrueの投稿一覧を取得
	 * 
	 * @return 削除フラグtrueの投稿一覧
	 */
	public List<PostinfoEntity> findPostByDelflg() {
		Query postinfoQuery = entityManager.createQuery("from PostinfoEntity where delFlg = 'true'");
		return postinfoQuery.getResultList();
	}

	/**
	 * 投稿情報を登録する
	 * 
	 * @param postid 投稿ID
	 * @param typeid パーツ種別ID
	 * @return 登録情報
	 */
	public PostinfoEntity insertPostinfo(Integer postid, Integer typeid) {
		PostinfoEntity postinfoEntity = new PostinfoEntity();

		LocalDateTime nowDate = LocalDateTime.now();

		// 投稿情報を本番データに移行
		postinfoEntity.setPostid(postid);
		postinfoEntity.setPostdate(Constants.DATE_FORMAT.format(nowDate));
		postinfoEntity.setStatus(Constants.POSTINFO_STATUS_MISHONIN);
		postinfoEntity.setTypeid(typeid);
		postinfoEntity = postinfoRepos.saveAndFlush(postinfoEntity);

		return postinfoEntity;
	}

	/**
	 * 投稿情報を削除する
	 * 
	 * @param postinfoEntity 削除対象エンティティ
	 */
	public void deletePostinfo(PostinfoEntity postinfoEntity) {
		postinfoEntity.setDelFlg(true);
		postinfoRepos.saveAndFlush(postinfoEntity);
	}

	/**
	 * 削除フラグを立てる
	 * 
	 * @param postinfoEntity 削除フラグ対象投稿エンティティ
	 */
	public void chageStatus(PostinfoEntity postinfoEntity) {
		postinfoEntity.setStatus(Constants.POSTIFNO_STATUS_SHONIN);
		postinfoRepos.saveAndFlush(postinfoEntity);
	}

	/**
	 * 対象エンティティをデータベースから削除する
	 * 
	 * @param postinfoEntity 削除対象エンティティ
	 */
	public void rejectPostinfo(PostinfoEntity postinfoEntity) {
		postinfoRepos.delete(postinfoEntity);
	}

}
