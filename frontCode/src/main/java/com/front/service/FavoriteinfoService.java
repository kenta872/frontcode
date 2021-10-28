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

import com.front.controller.entity.FavoriteEntity;
import com.front.controller.repository.FavoriteinfoRepository;
import com.front.security.account.Account;
import com.front.util.StringUtil;

/**
 * お気に入りテーブル操作DAO
 */
@Service
public class FavoriteinfoService {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	CustomService customDao;
	@Autowired
	FavoriteinfoRepository favoriteRepos;

	@PersistenceContext
	private EntityManager entityManager;

	public List<Object> findPostidByAccountid(Integer accountid) {
		Query Query = entityManager.createQuery("select postid from FavoriteEntity where accountid = " + accountid);
		return Query.getResultList();
	}
	
	public List<FavoriteEntity> findFavoriteAll(Integer accountid) {
		Query Query = entityManager.createQuery("from FavoriteEntity where accountid = " + accountid);
		return Query.getResultList();
	}
	
	public List<FavoriteEntity> findFavorite(Integer postid,Integer accountid) {
		Query Query = entityManager.createQuery("from FavoriteEntity where postid = " + postid + "and accountid = " + accountid);
		return Query.getResultList();
	}
	
	public FavoriteEntity findFavoriteByFavoriteid(Integer favoriteid) {
		Query Query = entityManager.createQuery("from FavoriteEntity where favoriteid = " + favoriteid);
		return (FavoriteEntity)Query.getSingleResult();
	}
	
	public Map<Integer, Map<Object, List<Object>>> getFavoriteMap(Account account) {
		Map<Integer, Map<Object, List<Object>>> favoriteMap = new HashMap<>();
		Map<Object, List<Object>> postMap = new HashMap<>();
		List<Object[]> favofiteObjArray = customDao.findFavoriteMap(account.getUserid());
		for (Object[] favoriteObj : favofiteObjArray) {
			List<Object> initList = Arrays.asList(favoriteObj);
			List<Object> outList = new ArrayList<>();
			outList.add(initList.get(2));
			outList.add(initList.get(3));
			outList.add(StringUtil.createIframe((String) initList.get(2), (String) initList.get(3)));
			postMap.put(initList.get(1), outList);
			favoriteMap.put((Integer) initList.get(0), postMap);
			postMap = new HashMap<>();
		}
		
		return favoriteMap;
	}
	
	public void insertFavorite(Integer accountid, Integer postid) {
		FavoriteEntity newFavoriteEntity = new FavoriteEntity();
		newFavoriteEntity.setAccountid(accountid);
		newFavoriteEntity.setPostid(postid);
		favoriteRepos.saveAndFlush(newFavoriteEntity);
	}
	
	public void deleteFavorite(FavoriteEntity favoriteEntity) {
		favoriteRepos.delete(favoriteEntity);
	}
}
