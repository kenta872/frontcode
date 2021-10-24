package com.front.schedule;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.front.controller.entity.CodeinfoEntity;
import com.front.controller.entity.FileinfoEntity;
import com.front.controller.entity.FileinfosubEntity;
import com.front.controller.entity.PostinfoEntity;
import com.front.controller.entity.PostinfosubEntity;
import com.front.controller.repository.CodeinfoRepository;
import com.front.controller.repository.CodeinfosubRepository;
import com.front.controller.repository.FileinfoRepository;
import com.front.controller.repository.FileinfosubRepository;
import com.front.controller.repository.PostinfoRepository;
import com.front.controller.repository.PostinfosubRepository;
import com.front.service.CodeinfoDao;
import com.front.service.FileinfoDao;
import com.front.service.FileinfosubDao;
import com.front.service.IoService;
import com.front.service.PostinfoDao;
import com.front.service.PostinfosubDao;
import com.front.service.TypedbDao;

@Component
public class ScheduledTasks {

	@Autowired
	CodeinfoDao codeinfoDao;
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

	Logger logger = LoggerFactory.getLogger(this.getClass());

	// cronは秒、分、時、日、月、曜日 を指定することが可能。曜日は（0,1,2,3,4,5,6,7が日～日を表す。※日は2つある）
	@Scheduled(cron = "0 5 0 * * *", zone = "Asia/Tokyo")
//	@Scheduled(fixedRate=5000)
	public void deleteDb() throws Exception {

		// fileinfoのデータ削除
		// HTML、ZIPファイル削除
		List<FileinfosubEntity> fileinfosubEntityList = fileinfosubDao.selectFileAll();
		for (FileinfosubEntity fileinfosubEntity : fileinfosubEntityList) {
			ioService.deleteFile(fileinfosubEntity.getFilename(), true);
			logger.info("ファイル削除：" + fileinfosubEntity.getFilename());
			filesubRepos.delete(fileinfosubEntity);
		}

		// postinfoのデータ削除
		List<PostinfosubEntity> postinfosubEntityList = postinfosubDao.findPostAll();
		for (PostinfosubEntity postinfosubEntity : postinfosubEntityList) {
			logger.info("仮登録投稿一覧を削除：" + postinfosubEntity.getPostid());
			postsubRepos.delete(postinfosubEntity);
		}

		// 削除カラムがついている項目を削除
		// ファイル関連を削除
		List<FileinfoEntity> fileinfoEntityList = fileinfoDao.findFileByDelflg();
		for (FileinfoEntity fileinfoEntity : fileinfoEntityList) {
			logger.info("削除フラグファイル一覧を削除：" + fileinfoEntity.getFilename());
			ioService.deleteFile(fileinfoEntity.getFilename(), false);
			fileRepos.delete(fileinfoEntity);
		}
		// ソースコードを削除
		List<CodeinfoEntity> codeinfoEntityList = codeinfoDao.findSrcByDelflg();
		for (CodeinfoEntity codeinfoEntity : codeinfoEntityList) {
			logger.info("削除フラグソースコード削除：" + codeinfoEntity.getCodeid());
			codeRepos.delete(codeinfoEntity);
		}
		// 投稿情報を削除
		List<PostinfoEntity> postinfoEntityList = postinfoDao.findPostByDelflg();
		for (PostinfoEntity postinfoEntity : postinfoEntityList) {
			logger.info("削除フラグ投稿一覧を削除：" + postinfoEntity.getPostid());
			postRepos.delete(postinfoEntity);
		}
	}

}
