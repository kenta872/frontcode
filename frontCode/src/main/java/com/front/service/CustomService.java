package com.front.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * カスタムDAO
 */
@Service
@Transactional
public class CustomService {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * 投稿管理テーブルで未承認のデータを抽出する
	 * 
	 * @return 未承認投稿一覧
	 */
	public List<Object[]> findPostByStatus() throws Exception {

		String sql = "select ";
		sql += "  pi.postdate as postdate ";
		sql += "  , td.typename as typename ";
		sql += "  , pi.postid as postid ";
		sql += "  , pi.typeid as typeid ";
		sql += "from ";
		sql += "  PostinfoEntity pi ";
		sql += "    left outer join TypedbEntity td ";
		sql += "	  on pi.typeid = td.typeid ";
		sql += "where ";
		sql += "  pi.status = 1";
		sql += "  and pi.delFlg = 'false'";

		// 未承認(status=1)の投稿ID一覧を取得する
		Query authoQuery = entityManager.createQuery(sql);
		return authoQuery.getResultList();

	}

	/**
	 * パーツ種別ごとに未承認投稿一覧を取得する
	 * 
	 * @param typeid パーツ種別ID
	 * @return パーツ種別ごとの未承認投稿一覧
	 * @throws Exception
	 */
	public List<Object[]> findPostByStatusFilter(Integer typeid) throws Exception {

		String sql = "select ";
		sql += "  pi.postdate as postdate ";
		sql += "  , td.typename as typename ";
		sql += "  , pi.postid as postid ";
		sql += "  , pi.typeid as typeid ";
		sql += "from ";
		sql += "  PostinfoEntity pi ";
		sql += "    left outer join TypedbEntity td ";
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
	 * 初期画面表示用SQL
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> findInitMap() throws Exception {

		String initSql = "select ";
		initSql += "  td.typeid ";
		initSql += "  , pi.postid ";
		initSql += "  , cihtml.htmlcode ";
		initSql += "  , cicss.csscode ";
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

//	
//	/**
//	 * 初期画面表示用SQL
//	 * @return
//	 * @throws Exception
//	 */
//	public List<Object[]> findMisyoninMap() throws Exception {
//		
//		String initSql = "select ";
//		initSql += "  td.typeid ";
//		initSql += "  , pi.postid ";
//		initSql += "  , cihtml.htmlcode ";
//		initSql += "  , cicss.csscode ";
//		initSql += "from ";
//		initSql += "  v1.typedb td left outer join v1.postinfo pi ";
//		initSql += "    on td.typeid = pi.typeid ";
//		initSql += "    left outer join ( ";
//		initSql += "      select ";
//		initSql += "    v1.codeinfo.postid ";
//		initSql += "    , v1.codeinfo.src htmlcode ";
//		initSql += "  from ";
//		initSql += "    v1.codeinfo ";
//		initSql += "  where ";
//		initSql += "    v1.codeinfo.codegenre = 'html' ";
//		initSql += "    ) cihtml ";
//		initSql += "      on pi.postid = cihtml.postid ";
//		initSql += "  left outer join ( ";
//		initSql += "    select ";
//		initSql += "  v1.codeinfo.postid ";
//		initSql += "  , v1.codeinfo.src csscode ";
//		initSql += "    from ";
//		initSql += "  v1.codeinfo ";
//		initSql += "    where ";
//		initSql += "  v1.codeinfo.codegenre = 'css' ";
//		initSql += "  ) cicss ";
//		initSql += "    on pi.postid = cicss.postid ";
//		initSql += "where ";
//		initSql += "  pi.del_flg = 'false' ";
//		initSql += "  and pi.status = 2 ";
//		initSql += "order by ";
//		initSql += " td.typeid ";
//		initSql += " ,pi.postid; ";
//		
//		// 未承認(status=1)の投稿ID一覧を取得する
//		Query authoQuery = entityManager.createNativeQuery(initSql);
//		return authoQuery.getResultList();
//		
//	}

	/**
	 * ログインユーザーのお気に入り一覧を取得する
	 * 
	 * @param userid ユーザーID
	 * @return お気に入り投稿一覧
	 */
	public List<Object[]> findFavoriteMap(Integer userid) {

		String initSql = "select ";
		initSql += "  fi.favoriteid ";
		initSql += "  , pi.postid ";
		initSql += "  , cihtml.htmlcode ";
		initSql += "  , cicss.csscode ";
		initSql += "from ";
		initSql += "  v1.favoriteinfo fi left outer join v1.postinfo pi ";
		initSql += "    on fi.postid = pi.postid ";
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
		initSql += "where ";
		initSql += "  pi.del_flg = 'false' ";
		initSql += "  and pi.status = 2 ";
		initSql += "  and fi.accountid = '" + userid + "'";
		initSql += "order by ";
		initSql += " fi.favoriteid ";
		initSql += " ,pi.postid; ";

		// 未承認(status=1)の投稿ID一覧を取得する
		Query authoQuery = entityManager.createNativeQuery(initSql);
		return authoQuery.getResultList();

	}

}
