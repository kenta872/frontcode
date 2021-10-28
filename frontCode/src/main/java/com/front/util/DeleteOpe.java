package com.front.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.front.controller.entity.CodeinfoEntity;
import com.front.controller.entity.PostinfoEntity;
import com.front.controller.entity.PostinfosubEntity;
import com.front.controller.repository.PostinfoRepository;
import com.front.controller.repository.PostinfosubRepository;
import com.front.service.CodeinfoService;
import com.front.service.PostinfoService;
import com.front.service.PostinfosubService;

@Service
public class DeleteOpe {

	@Autowired
	CodeinfoService codeinfoService;
	@Autowired
	PostinfoService postinfoService;
	@Autowired
	PostinfosubService postinfosubService;

	@Autowired
	PostinfoRepository postRepos;
	@Autowired
	PostinfosubRepository postsubRepos;

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * postidに紐づくすべてのデータを削除する
	 * 
	 * @param postid 投稿ID
	 * @throws Exception
	 */
	public void deleteOpe(Integer postid) {
		
		// ソースコード削除
		List<CodeinfoEntity> codeinfoList = codeinfoService.findSrcByPostid(postid);
		for (CodeinfoEntity codeinfo : codeinfoList) {
			codeinfoService.deleteCodeinfo(codeinfo);
		}

		// 投稿情報削除
		PostinfoEntity postinfoEntity = postinfoService.findPostByPostid(postid);
		postinfoService.deletePostinfo(postinfoEntity);
	}

	/**
	 * postidに紐づくすべてのデータを削除する
	 * 
	 * @param postid 投稿ID
	 * @throws Exception
	 */
	public void deleteOpeForSub(Integer postid) {

		// 投稿情報削除
		PostinfosubEntity postinfosubEntity = postinfosubService.findPostByPostid(postid);
		postsubRepos.delete(postinfosubEntity);
	}

}
