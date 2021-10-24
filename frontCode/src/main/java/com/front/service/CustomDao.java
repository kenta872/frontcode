package com.front.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * カスタムDAO
 */
@Service
public class CustomDao {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@PersistenceContext
	private EntityManager entityManager;
	@Value("${file.savedir}")
	private String dirname;
	
	/**
	 * 投稿管理テーブルで未承認のデータを抽出する
	 * @return
	 */
	public List<Object[]> findPostByStatus() throws Exception {
		
		String sql = "select ";
			  sql += "  pi.postdate as postdate ";
			  sql += "  , td.typename as typename ";
			  sql += "  , pi.postid as postid ";
			  sql += "  , pi.typeid as typeid ";
			  sql += "from ";
			  sql += "  PostinfoEntity pi ";
			  sql +="    left outer join TypedbEntity td ";
			  sql += "	  on pi.typeid = td.typeid ";
			  sql += "where ";
			  sql += "  pi.status = 1";
			  sql += "  and pi.delFlg = 'false'";
		
		// 未承認(status=1)の投稿ID一覧を取得する
		Query authoQuery = entityManager.createQuery(sql);
		return authoQuery.getResultList();
		
	}
	

	/**
	 * 投稿管理テーブルで未承認のデータを抽出する
	 * @return
	 */
	public List<Object[]> findPostByStatusFilter(Integer typeid) throws Exception {
		
		String sql = "select ";
			  sql += "  pi.postdate as postdate ";
			  sql += "  , td.typename as typename ";
			  sql += "  , pi.postid as postid ";
			  sql += "  , pi.typeid as typeid ";
			  sql += "from ";
			  sql += "  PostinfoEntity pi ";
			  sql +="    left outer join TypedbEntity td ";
			  sql += "	  on pi.typeid = td.typeid ";
			  sql += "where ";
			  sql += "  pi.status = 1";
			  sql += "  and pi.typeid = " + typeid;
			  sql += "  and pi.delFlg = 'false'";
		
		// 未承認(status=1)の投稿ID一覧を取得する
		Query authoQuery = entityManager.createQuery(sql);
		return authoQuery.getResultList();
		
	}

	/**
	 * ソースコードテーブルから指定した投稿IDに紐づくソースコードを取得する。
	 */
	public String[] searchSrcByPostid(Integer postid,Integer typeid) throws Exception {
		String[] queryResult = new String[3];
		try {
			// HTMLソースコード取得
			Query codehtmlQuery = entityManager.createQuery("select src from CodeinfoEntity where codegenre = 'html' and postid = " + postid);
			queryResult[0] = (String)codehtmlQuery.getSingleResult();
			// CSSソースコード取得
			Query codecssQuery = entityManager.createQuery("select src from CodeinfoEntity where codegenre = 'css' and postid = " + postid);
			queryResult[1] = (String)codecssQuery.getSingleResult();
			// パーツ種別を取得
			Query typedbQuery = entityManager.createQuery("select typename from TypedbEntity where typeid = " + typeid);
			queryResult[2] = (String)typedbQuery.getSingleResult();
			

		} catch (Exception e) {
			throw e;
		}
		return queryResult;
	}
	
	/**
	 * ソースコードテーブルから指定した投稿IDに紐づくソースコードを取得する。
	 */
	public String[] searchSrcByPostidFromSub(Integer postid,Integer typeid) throws Exception {
		String[] queryResult = new String[4];
		try {
			// HTMLソースコード取得
			Query codehtmlQuery = entityManager.createQuery("select src from CodeinfosubEntity where codegenre = 'html' and postid = " + postid);
			queryResult[0] = (String)codehtmlQuery.getSingleResult();
			// CSSソースコード取得
			Query codecssQuery = entityManager.createQuery("select src from CodeinfosubEntity where codegenre = 'css' and postid = " + postid);
			queryResult[1] = (String)codecssQuery.getSingleResult();
			// パーツ種別を取得
			Query typedbQuery = entityManager.createQuery("select typename from TypedbEntity where typeid = " + typeid);
			queryResult[2] = (String)typedbQuery.getSingleResult();
			

		} catch (Exception e) {
			throw e;
		}
		return queryResult;
	}
	
	

	/**
	 * 初期画面表示用SQL
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> findInitMap() throws Exception {
		
		String initSql = "select ";
		initSql += "  td.typeid ";
		initSql += "  , pi.postid ";
		initSql += "  , cihtml.htmlcode ";
		initSql += "  , cicss.csscode ";
		initSql += "  , fihtml.htmlfile ";
		initSql += "  , fizip.zipfile ";
		initSql += "from ";
		initSql += "  v1.typedb td left outer join v1.postinfo pi ";
		initSql += "    on td.typeid = pi.typeid ";
		initSql += "    left outer join ( ";
		initSql += "      select ";
		initSql += "    v1.codeinfo.postid ";
		initSql += "    , v1.codeinfo.src htmlcode ";
		initSql += "  from ";
		initSql += "    v1.codeinfo ";
		initSql += "  where ";
		initSql += "    v1.codeinfo.codegenre = 'html' ";
		initSql += "    ) cihtml ";
		initSql += "      on pi.postid = cihtml.postid ";
		initSql += "  left outer join ( ";
		initSql += "    select ";
		initSql += "  v1.codeinfo.postid ";
		initSql += "  , v1.codeinfo.src csscode ";
		initSql += "    from ";
		initSql += "  v1.codeinfo ";
		initSql += "    where ";
		initSql += "  v1.codeinfo.codegenre = 'css' ";
		initSql += "  ) cicss ";
		initSql += "    on pi.postid = cicss.postid ";
		initSql += "    left outer join ( ";
		initSql += "      select ";
		initSql += "    v1.fileinfo.postid ";
		initSql += "    , v1.fileinfo.filename htmlfile ";
		initSql += "      from ";
		initSql += "    v1.fileinfo ";
		initSql += "      where ";
		initSql += "    v1.fileinfo.filegenre = 'html' ";
		initSql += "    ) fihtml ";
		initSql += "      on pi.postid = fihtml.postid ";
		initSql += "      left outer join ( ";
		initSql += "        select ";
		initSql += "      v1.fileinfo.postid ";
		initSql += "      , v1.fileinfo.filename zipfile ";
		initSql += "        from ";
		initSql += "      v1.fileinfo ";
		initSql += "        where ";
		initSql += "      v1.fileinfo.filegenre = 'zip' ";
		initSql += "      ) fizip ";
		initSql += "      on pi.postid = fizip.postid ";
		initSql += "where ";
		initSql += "  pi.del_flg = 'false' ";
		initSql += "  and pi.status = 2 ";
		initSql += "order by ";
		initSql += " td.typeid ";
		initSql += " ,pi.postid; ";
		
		// 未承認(status=1)の投稿ID一覧を取得する
		Query authoQuery = entityManager.createNativeQuery(initSql);
		return authoQuery.getResultList();
		
	}
	
	
	/**
	 * 初期画面表示用SQL
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> findMisyoninMap() throws Exception {
		
		String initSql = "select ";
		initSql += "  td.typeid ";
		initSql += "  , pi.postid ";
		initSql += "  , cihtml.htmlcode ";
		initSql += "  , cicss.csscode ";
		initSql += "  , fihtml.htmlfile ";
		initSql += "from ";
		initSql += "  v1.typedb td left outer join v1.postinfo pi ";
		initSql += "    on td.typeid = pi.typeid ";
		initSql += "    left outer join ( ";
		initSql += "      select ";
		initSql += "    v1.codeinfo.postid ";
		initSql += "    , v1.codeinfo.src htmlcode ";
		initSql += "  from ";
		initSql += "    v1.codeinfo ";
		initSql += "  where ";
		initSql += "    v1.codeinfo.codegenre = 'html' ";
		initSql += "    ) cihtml ";
		initSql += "      on pi.postid = cihtml.postid ";
		initSql += "  left outer join ( ";
		initSql += "    select ";
		initSql += "  v1.codeinfo.postid ";
		initSql += "  , v1.codeinfo.src csscode ";
		initSql += "    from ";
		initSql += "  v1.codeinfo ";
		initSql += "    where ";
		initSql += "  v1.codeinfo.codegenre = 'css' ";
		initSql += "  ) cicss ";
		initSql += "    on pi.postid = cicss.postid ";
		initSql += "    left outer join ( ";
		initSql += "      select ";
		initSql += "    v1.fileinfo.postid ";
		initSql += "    , v1.fileinfo.filename htmlfile ";
		initSql += "      from ";
		initSql += "    v1.fileinfo ";
		initSql += "      where ";
		initSql += "    v1.fileinfo.filegenre = 'html' ";
		initSql += "    ) fihtml ";
		initSql += "      on pi.postid = fihtml.postid ";
		initSql += "      left outer join ( ";
		initSql += "        select ";
		initSql += "      v1.fileinfo.postid ";
		initSql += "      , v1.fileinfo.filename zipfile ";
		initSql += "        from ";
		initSql += "      v1.fileinfo ";
		initSql += "        where ";
		initSql += "      v1.fileinfo.filegenre = 'zip' ";
		initSql += "      ) fizip ";
		initSql += "      on pi.postid = fizip.postid ";
		initSql += "where ";
		initSql += "  pi.del_flg = 'false' ";
		initSql += "  and pi.status = 2 ";
		initSql += "order by ";
		initSql += " td.typeid ";
		initSql += " ,pi.postid; ";
		
		// 未承認(status=1)の投稿ID一覧を取得する
		Query authoQuery = entityManager.createNativeQuery(initSql);
		return authoQuery.getResultList();
		
	}
}
