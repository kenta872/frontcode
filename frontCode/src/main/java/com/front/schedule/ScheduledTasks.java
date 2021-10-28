package com.front.schedule;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.front.controller.entity.CodeinfoEntity;
import com.front.controller.entity.PostinfoEntity;
import com.front.controller.entity.PostinfosubEntity;
import com.front.controller.repository.CodeinfoRepository;
import com.front.controller.repository.PostinfoRepository;
import com.front.controller.repository.PostinfosubRepository;
import com.front.service.CodeinfoService;
import com.front.service.PostinfoService;
import com.front.service.PostinfosubService;
import com.front.service.TypedbService;
import com.front.util.FileIo;

@Component
public class ScheduledTasks {

	@Autowired
	CodeinfoService codeinfoService;
	@Autowired
	PostinfoService postinfoService;
	@Autowired
	PostinfosubService postinfosubService;

	@Autowired
	FileIo ioService;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	// cronは秒、分、時、日、月、曜日 を指定することが可能。曜日は（0,1,2,3,4,5,6,7が日～日を表す。※日は2つある）
	@Scheduled(cron = "0 5 0 * * *", zone = "Asia/Tokyo")
//	@Scheduled(fixedRate = 5000)
	public void deleteDb() throws Exception {

		// postinfoのデータ削除
		List<PostinfosubEntity> postinfosubEntityList = postinfosubService.findPostAll();
		for (PostinfosubEntity postinfosubEntity : postinfosubEntityList) {
			logger.info("仮登録投稿一覧を削除：" + postinfosubEntity.getPostid());
			postinfosubService.rejectPostinfosub(postinfosubEntity);
		}

		// ソースコードを削除
		List<CodeinfoEntity> codeinfoEntityList = codeinfoService.findSrcByDelflg();
		for (CodeinfoEntity codeinfoEntity : codeinfoEntityList) {
			logger.info("削除フラグソースコード削除：" + codeinfoEntity.getCodeid());
			codeinfoService.rejectCodeinfo(codeinfoEntity);
		}
		// 投稿情報を削除
		List<PostinfoEntity> postinfoEntityList = postinfoService.findPostByDelflg();
		for (PostinfoEntity postinfoEntity : postinfoEntityList) {
			logger.info("削除フラグ投稿一覧を削除：" + postinfoEntity.getPostid());
			postinfoService.rejectPostinfo(postinfoEntity);
		}
	}

}
