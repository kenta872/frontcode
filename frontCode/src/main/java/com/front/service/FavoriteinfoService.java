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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.front.controller.entity.FavoriteEntity;
import com.front.controller.repository.FavoriteinfoRepository;
import com.front.security.account.Account;
import com.front.util.StringUtil;

/**
 * お気に入りテーブル操作DAO
 */
@Service
@Transactional
public class FavoriteinfoService {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	FavoriteinfoRepository favoriteRepos;

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * ユーザーのお気に入り投稿IDを取得する
	 * 
	 * @param accountid アカウントID
	 * @return お気に入り投稿ID一覧
	 */
	public List<Object> findPostidByAccountid(Integer accountid) {
		Query Query = entityManager.createQuery("select postid from FavoriteEntity where accountid = " + accountid);
		return Query.getResultList();
	}

	/**
	 * ユーザーのお気に入り情報一覧を取得する
	 * 
	 * @param accountid アカウントID
	 * @return お気に入り投稿一覧
	 */
	public List<FavoriteEntity> findFavoriteAll(Integer accountid) {
		Query Query = entityManager.createQuery("from FavoriteEntity where accountid = " + accountid);
		return Query.getResultList();
	}

	/**
	 * 特定のお気に入り投稿を取得する
	 * 
	 * @param postid    投稿ID
	 * @param accountid アカウントID
	 * @return お気に入り投稿
	 */
	public List<FavoriteEntity> findFavorite(Integer postid, Integer accountid) {
		Query Query = entityManager
				.createQuery("from FavoriteEntity where postid = " + postid + "and accountid = " + accountid);
		return Query.getResultList();
	}

	/**
	 * お気に入り情報を登録する
	 * 
	 * @param accountid アカウントID
	 * @param postid    投稿ID
	 */
	public void insertFavorite(Integer accountid, Integer postid) {
		FavoriteEntity newFavoriteEntity = new FavoriteEntity();
		newFavoriteEntity.setAccountid(accountid);
		newFavoriteEntity.setPostid(postid);
		favoriteRepos.saveAndFlush(newFavoriteEntity);
	}

	/**
	 * お気に入り情報を削除する
	 * 
	 * @param favoriteEntity 削除対象エンティティ
	 */
	public void deleteFavorite(FavoriteEntity favoriteEntity) {
		favoriteRepos.delete(favoriteEntity);
	}
}
