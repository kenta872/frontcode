package com.front.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.front.controller.entity.CodeinfoEntity;
import com.front.controller.repository.CodeinfoRepository;

/**
 * ソースコードテーブル操作DAO
 */
@Service
public class CodeinfoService {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	CodeinfoRepository codeinfoRepos;

	/**
	 * すべてのソースコードを取得する
	 * 
	 * @return
	 */
	public List<CodeinfoEntity> selectCodeAll() {
		Query Query = entityManager.createQuery("from CodeinfoEntity where delFlg = 'false'");
		return Query.getResultList();
	}

	/**
	 * ソースコード管理テーブルから投稿IDとソースジャンルに紐づくレコードを抽出する
	 * 
	 * @param postid    投稿ID
	 * @param codegenre ソースコードのジャンル
	 * @return 投稿IDとソースジャンルで絞り込んだレコード
	 * @throws Exception
	 */
	public CodeinfoEntity searchSrcByPostid(Integer postid, String codegenre) {

		Query Query = entityManager
				.createQuery("from CodeinfoEntity where delFlg = 'false' and codegenre = '" + codegenre + "' and postid = " + postid);
		return (CodeinfoEntity) Query.getSingleResult();
	}

	/**
	 * ソースコードテーブルから指定する投稿IDに紐づくソースを取得する
	 * 
	 * @param postid 投稿ID
	 * @return 投稿IDで絞り込んだレコード
	 * @throws Exception
	 */
	public List<CodeinfoEntity> findSrcByPostid(Integer postid) {

		Query Query = entityManager.createQuery("from CodeinfoEntity where delFlg = 'false' and postid = " + postid);
		return Query.getResultList();
	}

	public String findCodeByPostid(Integer postid, String codeType) {
		// ソースコードを取得
		Query codeinfoQuery = entityManager.createQuery(
				"select src from CodeinfoEntity where delFlg = 'false' and codegenre = '" + codeType + "' and postid = " + postid);
		String srccode = (String) codeinfoQuery.getSingleResult();

		return srccode;
	}
	
	public List<CodeinfoEntity> findSrcByDelflg() {
		
		Query Query = entityManager.createQuery("from CodeinfoEntity where delFlg = 'true'");
		return Query.getResultList();
	}
	
	public CodeinfoEntity insertCodeinfo(String codeType, Integer postid, String src) {
		CodeinfoEntity codeinfoEntity = new CodeinfoEntity();
		// HTMLソースコードを設定
		codeinfoEntity.setCodegenre(codeType);
		codeinfoEntity.setPostid(postid);
		codeinfoEntity.setSrc(src);
		codeinfoEntity = codeinfoRepos.saveAndFlush(codeinfoEntity);
		
		return codeinfoEntity;
	}
	
	public void deleteCodeinfo(CodeinfoEntity codeinfoEntity) {
		
		codeinfoEntity.setDelFlg(true);
		codeinfoRepos.saveAndFlush(codeinfoEntity);
	}
	
	public void rejectCodeinfo(CodeinfoEntity codeinfoEntity) {
		codeinfoRepos.delete(codeinfoEntity);
	}
}
