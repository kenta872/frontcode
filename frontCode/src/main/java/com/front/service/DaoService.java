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
 * DAOサービス
 */
@Service
public class DaoService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@PersistenceContext
	private EntityManager entityManager;
	@Value("${file.savedir}")
	private String dirname;
	
	/**
	 * パーツ種別テーブルからパーツの種類一覧を取得します。
	 */
	public List<TypedbEntity> selectTypedbAll() throws Exception {
		
		try {
			// SQL生成
			String sql = "from TypedbEntity";
			Query query = entityManager.createQuery(sql);
			return (List<TypedbEntity>)query.getResultList();			
		} catch (Exception e) {
			throw e;
		}
	}
	public String findTypeByTypeid(Integer typeid) {
		Query typeQuery = entityManager.createQuery("select typename from TypedbEntity where typeid = " + typeid);
		return (String)typeQuery.getSingleResult();
	}
	/**
	 * ソースコードテーブルから指定した投稿IDに紐づくソースコードを取得する。
	 */
	public String[] searchSrcByPostid(Integer postid,Integer typeid) throws Exception {
		String[] queryResult = new String[4];
		try {
			// HTMLソースコード取得
			Query codehtmlQuery = entityManager.createQuery("select src from CodeinfoEntity where codegenre = 'html' and postid = " + postid);
			queryResult[0] = (String)codehtmlQuery.getSingleResult();
			// CSSソースコード取得
			Query codecssQuery = entityManager.createQuery("select src from CodeinfoEntity where codegenre = 'css' and postid = " + postid);
			queryResult[1] = (String)codecssQuery.getSingleResult();
			// JSソースコード取得
			Query codejsQuery = entityManager.createQuery("select src from CodeinfoEntity where codegenre = 'js' and postid = " + postid);
			queryResult[2] = (String)codejsQuery.getSingleResult();
			// パーツ種別を取得
			Query typedbQuery = entityManager.createQuery("select typename from TypedbEntity where typeid = " + typeid);
			queryResult[3] = (String)typedbQuery.getSingleResult();
			

		} catch (Exception e) {
			throw e;
		}
		return queryResult;
	}
	
	/**
	 * コードテーブルから指定する投稿IDに紐づくソースを取得する
	 * 
	 * @param postid 投稿ID
	 * @return
	 * @throws Exception
	 */
	public List<CodeinfoEntity> findSrcByPostid(Integer postid) throws Exception {
		
		List<CodeinfoEntity> codeinfoList = new ArrayList();
		try {
			// HTMLソースコード取得
			Query codehtmlQuery = entityManager.createQuery("from CodeinfoEntity where codegenre = 'html' and postid = " + postid);
			codeinfoList.add((CodeinfoEntity)codehtmlQuery.getSingleResult());
			// CSSソースコード取得
			Query codecssQuery = entityManager.createQuery("from CodeinfoEntity where codegenre = 'css' and postid = " + postid);
			codeinfoList.add((CodeinfoEntity)codecssQuery.getSingleResult());
			// JSソースコード取得
			Query codejsQuery = entityManager.createQuery("from CodeinfoEntity where codegenre = 'js' and postid = " + postid);
			codeinfoList.add((CodeinfoEntity)codejsQuery.getSingleResult());
		} catch ( Exception e) {
			throw e;
		}
		return codeinfoList;
	}
	
	/**
	 * ファイル情報テーブルから指定した投稿IDに紐づくファイル情報を取得する
	 * @param postid 投稿ID
	 * @return
	 * @throws Exception
	 */
	public List<FileinfoEntity> findFileByPostid(Integer postid) throws Exception {
		
		List<FileinfoEntity> fileinfoList = new ArrayList();
		try {
			// HTMLファイル取得
			Query filehtmlQuery = entityManager.createQuery("from FileinfoEntity where filegenre = 'html' and postid = " + postid);
			fileinfoList.add((FileinfoEntity)filehtmlQuery.getSingleResult());
			// ZIPファイル取得
			Query filezipQuery = entityManager.createQuery("from FileinfoEntity where filegenre = 'zip' and postid = " + postid);
			fileinfoList.add((FileinfoEntity)filezipQuery.getSingleResult());
		} catch ( Exception e) {
			throw e;
		}
		return fileinfoList;
	}
	
	public FileinfoEntity findFileByPostid(Integer postid, String filetype) {
		// HTMLファイル取得
		Query filehtmlQuery = entityManager.createQuery("from FileinfoEntity where filegenre = '" + filetype + "' and postid = " + postid);
		return (FileinfoEntity)filehtmlQuery.getSingleResult();
	}
	
	/**
	 * 投稿情報テーブルから指定した投稿IDに紐づくレコードを取得する
	 * @param postid 投稿ID
	 * @return
	 * @throws Exception
	 */
	public PostinfoEntity findPostByPostid(Integer postid) throws Exception {

		PostinfoEntity postinfoEntity = new PostinfoEntity();
		try {
			// 投稿ID一覧を取得
			Query postinfoQuery = entityManager.createQuery("from PostinfoEntity where postid = " + postid);
			postinfoEntity = (PostinfoEntity)postinfoQuery.getSingleResult();
		} catch ( Exception e) {
			throw e;
		}
		return postinfoEntity;
	}
	
	public List<Object[]> findPostByStatus() {
		
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
		
		// 未承認(status=1)の投稿ID一覧を取得する
		Query authoQuery = entityManager.createQuery(sql);
		List<Object[]> postDataList = authoQuery.getResultList();
		
		return postDataList;
	}
	
	
	public String findCodeByPostid(Integer postid, String codeType) {
		// ソースコードを取得
		Query codeinfoQuery = entityManager.createQuery("select src from CodeinfoEntity where codegenre = '" + codeType +"' and postid = " + postid);
		String srccode = (String)codeinfoQuery.getSingleResult();
		
		return srccode;
	}
	
	
	/**
	 * 項目種別ごとにソースコード一覧を取得する
	 */
	public Map<Integer,Map<Integer,List<String>>> selectCodeAll() {
		Map<Integer,Map<Integer,List<String>>> outMap = new HashMap<>();
		
		// 項目種別ID一覧を取得
		Query typedbQuery = entityManager.createQuery("select typeid from TypedbEntity");
		List<Integer> typeidList = typedbQuery.getResultList();
		
		// 投稿ID一覧を取得
		Query postinfoQuery = entityManager.createQuery("from PostinfoEntity where status = 2");
		List<PostinfoEntity> postinfoList = postinfoQuery.getResultList();
		
		// HTMLソースコード一覧取得
		Query codehtmlQuery = entityManager.createQuery("from CodeinfoEntity where codegenre = 'html'");
		List<CodeinfoEntity> codehtmlList = codehtmlQuery.getResultList();
		// CSSソースコード一覧取得
		Query codecssQuery = entityManager.createQuery("from CodeinfoEntity where codegenre = 'css'");
		List<CodeinfoEntity> codecssList = codecssQuery.getResultList();
		// JSソースコード一覧取得
		Query codejsQuery = entityManager.createQuery("from CodeinfoEntity where codegenre = 'js'");
		List<CodeinfoEntity> codejsList = codejsQuery.getResultList();
		
		// HTMLファイル一覧取得
		Query filehtmlQuery = entityManager.createQuery("from FileinfoEntity where filegenre = 'html'");
		List<FileinfoEntity> filehtmlList = filehtmlQuery.getResultList();
		// ZIPファイル一覧取得
		Query filezipuery = entityManager.createQuery("from FileinfoEntity where filegenre = 'zip'");
		List<FileinfoEntity> filezipList = filezipuery.getResultList();
		
		// パーツ種別の数だけループ
		for(int typeidIndex=0;typeidIndex<typeidList.size();typeidIndex++) {
			int typeid = typeidList.get(typeidIndex);
			Map<Integer,List<String>> codeMap = new HashMap<>();
			// 投稿されている数だけループ
			for(int i=0;i<postinfoList.size();i++) {
				// パーツ種別に紐づく投稿の場合
				if(typeid == postinfoList.get(i).getTypeid()) {
					int postid = postinfoList.get(i).getPostid();
					String codehtml = null;
					String codecss = null;
					String codejs = null;
					String filehtml = dirname + "/src/";
					String filezip = dirname + "/zip/";
					// HTMLソースコードの数だけループ
					for(int codehtmlIndex=0;codehtmlIndex<codehtmlList.size();codehtmlIndex++) {
						// 投稿IDに紐づくソースコードがある場合
						if(codehtmlList.get(codehtmlIndex).getPostid() == postid) {
							codehtml = codehtmlList.get(codehtmlIndex).getSrc();
							break;
						}
					}
					// CSSソースコードの数だけループ
					for(int codecssIndex=0;codecssIndex<codecssList.size();codecssIndex++) {
						// 投稿IDに紐づくソースコードがある場合
						if(codecssList.get(codecssIndex).getPostid() == postid) {
							codecss = codecssList.get(codecssIndex).getSrc();
							break;
						}
					}
					// JSソースコードの数だけループ
					for(int codejsIndex=0;codejsIndex<codejsList.size();codejsIndex++) {
						// 投稿IDに紐づくソースコードがある場合
						if(codejsList.get(codejsIndex).getPostid() == postid) {
							codejs = codejsList.get(codejsIndex).getSrc();
							break;
						}
					}
					// HTMLファイル一覧の数だけループ
					for(int filehtmlIndex=0;filehtmlIndex<filehtmlList.size();filehtmlIndex++) {
						// 投稿IDと紐づくファイルがあった場合
						if(filehtmlList.get(filehtmlIndex).getPostid() == postid) {
							filehtml = filehtml + filehtmlList.get(filehtmlIndex).getFilename();
							break;
						}
					}
					// ZIPファイル一覧の数だけループ
					for(int filezipIndex=0;filezipIndex<filezipList.size();filezipIndex++) {
						// 投稿IDに紐づくファイルがあった場合
						if(filezipList.get(filezipIndex).getPostid() == postid) {
							filezip = filezip + filezipList.get(filezipIndex).getFilename();
						}
					}
					List<String> outlist = new ArrayList();
					outlist.add(codehtml);
					outlist.add(codecss);
					outlist.add(codejs);
					outlist.add(filehtml);
					outlist.add(filezip);
					codeMap.put(postid, outlist);
				}
				outMap.put(typeid, codeMap);
			}

		}
		return outMap;
	}

}
