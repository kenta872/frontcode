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

import com.front.controller.entity.CodeinfoEntity;
import com.front.controller.entity.CodeinfosubEntity;
import com.front.controller.entity.FileinfoEntity;
import com.front.controller.entity.FileinfosubEntity;
import com.front.controller.entity.PostinfoEntity;
import com.front.controller.entity.PostinfosubEntity;
import com.front.controller.entity.TypedbEntity;
import com.front.controller.repository.CodeinfoRepository;
import com.front.controller.repository.CodeinfosubRepository;
import com.front.controller.repository.FileinfoRepository;
import com.front.controller.repository.FileinfosubRepository;
import com.front.controller.repository.PostinfoRepository;
import com.front.controller.repository.PostinfosubRepository;
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
	 * postidに紐づくすべてのデータを削除する
	 * 
	 * @param postid 投稿ID
	 * @throws Exception
	 */
	public void deleteOpe(Integer postid) throws Exception {
		// ソースコード削除
		List<CodeinfoEntity> codeinfoList = codeinfoDao.findSrcByPostid(postid);
		for (CodeinfoEntity codeinfo : codeinfoList) {
			codeinfo.setDelFlg(true);
			codeRepos.saveAndFlush(codeinfo);
		}
		// ファイル情報削除
		List<FileinfoEntity> fileinfoList = fileinfoDao.findFileByPostid(postid);
		for (FileinfoEntity fileinfo : fileinfoList) {
			fileinfo.setDelFlg(true);
			fileRepos.saveAndFlush(fileinfo);
		}
		// 投稿情報削除
		PostinfoEntity postinfoEntity = postinfoDao.findPostByPostid(postid);
		postinfoEntity.setDelFlg(true);
		postRepos.saveAndFlush(postinfoEntity);
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

}
