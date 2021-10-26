package com.front.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.front.controller.entity.CodeinfoEntity;
import com.front.controller.entity.PostinfoEntity;
import com.front.controller.entity.PostinfosubEntity;
import com.front.controller.repository.CodeinfoRepository;
import com.front.controller.repository.CodeinfosubRepository;
import com.front.controller.repository.PostinfoRepository;
import com.front.controller.repository.PostinfosubRepository;

@Service
public class OperationService {

	@Autowired
	CodeinfoDao codeinfoDao;
	@Autowired
	CodeinfosubDao codeinfosubDao;
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
	public void deleteOpe(Integer postid) {
		// ソースコード削除
		List<CodeinfoEntity> codeinfoList = codeinfoDao.findSrcByPostid(postid);
		for (CodeinfoEntity codeinfo : codeinfoList) {
			codeinfo.setDelFlg(true);
			codeRepos.saveAndFlush(codeinfo);
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
	public void deleteOpeForSub(Integer postid) {

		// 投稿情報削除
		PostinfosubEntity postinfosubEntity = postinfosubDao.findPostByPostid(postid);
		postsubRepos.delete(postinfosubEntity);
	}

}
