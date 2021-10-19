package com.front.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.front.entity.CodeinfoEntity;
import com.front.entity.CodeinfosubEntity;
import com.front.entity.FileinfoEntity;
import com.front.entity.FileinfosubEntity;
import com.front.entity.PostinfoEntity;
import com.front.entity.PostinfosubEntity;
import com.front.entity.TypedbEntity;
import com.front.repository.CodeinfoRepository;
import com.front.repository.CodeinfosubRepository;
import com.front.repository.FileinfoRepository;
import com.front.repository.FileinfosubRepository;
import com.front.repository.PostinfoRepository;
import com.front.repository.PostinfosubRepository;
import com.front.util.Constants;

@Service
public class OperationService {

	@Autowired
	DaoService daoService;
	@Autowired
	CodeinfoDao codeinfoDao;
	@Autowired
	CodeinfosubDao codeinfosubDao;
	@Autowired
	FileinfosubDao fileinfosubDao;
	@Autowired
	FileinfoDao fileinfoDao;
	@Autowired
	PostinfoDao postinfoDao;
	@Autowired
	PostinfosubDao postinfosubDao;
	@Autowired
	TypedbDao typedbDao;

	@Autowired
	PostinfoRepository postRepos;
	@Autowired
	PostinfosubRepository postsubRepos;
	@Autowired
	CodeinfoRepository codeRepos;
	@Autowired
	CodeinfosubRepository codesubRepos;
	@Autowired
	FileinfoRepository fileRepos;
	@Autowired
	FileinfosubRepository filesubRepos;

	@Autowired
	IoService ioService;

	@Value("${file.savedir}")
	private String dirname;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * ホーム画面のソースコード一覧情報を取得する
	 * 
	 * @return パーツ種別ごとにソースをマップに格納し返却
	 * @throws Exception
	 */
	public Map<Integer, Map<Integer, List<String>>> getSrcAllOpe() throws Exception {

		// 項目種別一覧取得
		List<TypedbEntity> typedbList = typedbDao.selectTypedbAll();
		// 未承認投稿一覧を取得
		List<PostinfoEntity> postinfoList = postinfoDao.findPostByStatus(2);
		// ソースコード一覧取得
		List<CodeinfoEntity> codeinfoList = codeinfoDao.selectCodeAll();
		// ファイル一覧を取得
		List<FileinfoEntity> fileinfoList = fileinfoDao.selectFileAll();

		Map<Integer, Map<Integer, List<String>>> indexMap = new HashMap<>();
		// パーツ種別の数だけループ
		for (int typeidIndex = 0; typeidIndex < typedbList.size(); typeidIndex++) {
			int typeid = typedbList.get(typeidIndex).getTypeid();
			Map<Integer, List<String>> codeMap = new HashMap<>();
			// 投稿されている数だけループ
			for (int i = 0; i < postinfoList.size(); i++) {
				// パーツ種別に紐づく投稿の場合
				if (typeid == postinfoList.get(i).getTypeid()) {
					int postid = postinfoList.get(i).getPostid();
					String codehtml = null;
					String codecss = null;
					String codejs = null;
					String filehtml = dirname + "/src/";
					String filezip = dirname + "/zip/";
					// ソースコードの数だけループ
					for (int codeinfoIndex = 0; codeinfoIndex < codeinfoList.size(); codeinfoIndex++) {
						boolean htmlHit = false;
						boolean cssHit = false;
						boolean jsHit = false;
						// 投稿IDに紐づくソースコードがある場合
						if (codeinfoList.get(codeinfoIndex).getPostid() == postid) {
							if (codeinfoList.get(codeinfoIndex).getCodegenre().equals(Constants.CODE_TYPE_HTML)) {
								codehtml = codeinfoList.get(codeinfoIndex).getSrc();
								htmlHit = true;
							} else if (codeinfoList.get(codeinfoIndex).getCodegenre().equals(Constants.CODE_TYPE_CSS)) {
								codecss = codeinfoList.get(codeinfoIndex).getSrc();
								cssHit = true;
							} else if (codeinfoList.get(codeinfoIndex).getCodegenre().equals(Constants.CODE_TYPE_JS)) {
								codejs = codeinfoList.get(codeinfoIndex).getSrc();
								jsHit = true;
							}
						}
						if (htmlHit == true && cssHit == true && jsHit == true) {
							break;
						}

					}

					// ファイル一覧の数だけループ
					for (int fileinfoIndex = 0; fileinfoIndex < fileinfoList.size(); fileinfoIndex++) {
						boolean htmlHit = false;
						boolean zipHit = false;
						// 投稿IDと紐づくファイルがあった場合
						if (fileinfoList.get(fileinfoIndex).getPostid() == postid) {
							if (fileinfoList.get(fileinfoIndex).getFilegenre().equals(Constants.FILE_TYPE_HTML)) {
								filehtml = filehtml + fileinfoList.get(fileinfoIndex).getFilename();
								htmlHit = true;
							}
							if (fileinfoList.get(fileinfoIndex).getFilegenre().equals(Constants.FILE_TYPE_ZIP)) {
								filezip = filezip + fileinfoList.get(fileinfoIndex).getFilename();
								zipHit = true;
							}
						}
						if (zipHit == true && zipHit == true) {
							break;
						}
					}
					List<String> outlist = new ArrayList<>();
					outlist.add(codehtml);
					outlist.add(codecss);
					outlist.add(codejs);
					outlist.add(filehtml);
					outlist.add(filezip);
					codeMap.put(postid, outlist);
				}
				indexMap.put(typeid, codeMap);
			}
		}
		return indexMap;
	}

	/**
	 * postidに紐づくすべてのデータを削除する
	 * 
	 * @param postid 投稿ID
	 * @throws Exception
	 */
	public void deleteOpe(Integer postid) throws Exception {
		// ソースコード削除
		List<CodeinfoEntity> codeinfoList = codeinfoDao.findSrcByPostid(postid);
		for (CodeinfoEntity codeinfo : codeinfoList) {
			codeRepos.delete(codeinfo);
		}
		// ファイル情報削除
		List<FileinfoEntity> fileinfoList = fileinfoDao.findFileByPostid(postid);
		for (FileinfoEntity fileinfo : fileinfoList) {
			fileRepos.delete(fileinfo);
			// html,zipファイルを削除
			ioService.deleteFile(fileinfo.getFilename(), false);
		}
		// 投稿情報削除
		PostinfoEntity postinfoEntity = postinfoDao.findPostByPostid(postid);
		postRepos.delete(postinfoEntity);
	}

	/**
	 * postidに紐づくすべてのデータを削除する
	 * 
	 * @param postid 投稿ID
	 * @throws Exception
	 */
	public void deleteOpeForSub(Integer postid) throws Exception {
		// ファイル情報削除
		List<FileinfosubEntity> fileinfoList = fileinfosubDao.findFileByPostid(postid);
		for (FileinfosubEntity fileinfo : fileinfoList) {
			filesubRepos.delete(fileinfo);
			// html,zipファイルを削除
			ioService.deleteFile(fileinfo.getFilename(), true);
		}
		// 投稿情報削除
		PostinfosubEntity postinfosubEntity = postinfosubDao.findPostByPostid(postid);
		postsubRepos.delete(postinfosubEntity);
	}

	/**
	 * 管理画面で表示するソースコード一覧情報を取得する
	 * 
	 * @return パーツ種別ごとにソースをマップに格納し返却
	 * @throws Exception
	 */
	public Map<Integer, Map<Integer, List<String>>> getSrcAllForManageOpe() throws Exception {

		// 項目種別一覧取得
		List<TypedbEntity> typedbList = typedbDao.selectTypedbAll();
		// 未承認投稿一覧を取得
		List<PostinfoEntity> postinfoList = postinfoDao.findPostByStatus(2);
		// ソースコード一覧取得
		List<CodeinfoEntity> codeinfoList = codeinfoDao.selectCodeAll();
		// ファイル一覧を取得
		List<FileinfoEntity> fileinfoList = fileinfoDao.selectFileAll();

		Map<Integer, Map<Integer, List<String>>> indexMap = new HashMap<>();
		// パーツ種別の数だけループ
		for (int typeidIndex = 0; typeidIndex < typedbList.size(); typeidIndex++) {
			int typeid = typedbList.get(typeidIndex).getTypeid();
			Map<Integer, List<String>> codeMap = new HashMap<>();
			// 投稿されている数だけループ
			for (int i = 0; i < postinfoList.size(); i++) {
				// パーツ種別に紐づく投稿の場合
				if (typeid == postinfoList.get(i).getTypeid()) {
					int postid = postinfoList.get(i).getPostid();
					String codehtml = null;
					String codecss = null;
					String codejs = null;
					String filehtml = dirname + "/src/";
					// ソースコードの数だけループ
					for (int codeinfoIndex = 0; codeinfoIndex < codeinfoList.size(); codeinfoIndex++) {
						boolean htmlHit = false;
						boolean cssHit = false;
						boolean jsHit = false;
						// 投稿IDに紐づくソースコードがある場合
						if (codeinfoList.get(codeinfoIndex).getPostid() == postid) {
							if (codeinfoList.get(codeinfoIndex).getCodegenre().equals(Constants.CODE_TYPE_HTML)) {
								codehtml = codeinfoList.get(codeinfoIndex).getSrc();
								htmlHit = true;
							} else if (codeinfoList.get(codeinfoIndex).getCodegenre().equals(Constants.CODE_TYPE_CSS)) {
								codecss = codeinfoList.get(codeinfoIndex).getSrc();
								cssHit = true;
							} else if (codeinfoList.get(codeinfoIndex).getCodegenre().equals(Constants.CODE_TYPE_JS)) {
								codejs = codeinfoList.get(codeinfoIndex).getSrc();
								jsHit = true;
							}
						}
						if (htmlHit == true && cssHit == true && jsHit == true) {
							break;
						}

					}

					// ファイル一覧の数だけループ
					for (int fileinfoIndex = 0; fileinfoIndex < fileinfoList.size(); fileinfoIndex++) {
						// 投稿IDと紐づくファイルがあった場合
						if (fileinfoList.get(fileinfoIndex).getPostid() == postid) {
							if (fileinfoList.get(fileinfoIndex).getFilegenre().equals(Constants.FILE_TYPE_HTML)) {
								filehtml = filehtml + fileinfoList.get(fileinfoIndex).getFilename();
								break;
							}
						}
					}
					List<String> outlist = new ArrayList();
					outlist.add(codehtml);
					outlist.add(codecss);
					outlist.add(codejs);
					outlist.add(filehtml);
					outlist.add(String.valueOf(postid));
					outlist.add(String.valueOf(typeid));
					codeMap.put(postid, outlist);
				}
				indexMap.put(typeid, codeMap);
			}
		}
		return indexMap;
	}
}
