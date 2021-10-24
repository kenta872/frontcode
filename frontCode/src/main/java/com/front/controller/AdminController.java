package com.front.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.front.controller.entity.CodeinfoEntity;
import com.front.controller.entity.FileinfoEntity;
import com.front.controller.entity.PostinfoEntity;
import com.front.controller.entity.TypedbEntity;
import com.front.controller.repository.CodeinfoRepository;
import com.front.controller.repository.CodeinfosubRepository;
import com.front.controller.repository.FileinfoRepository;
import com.front.controller.repository.FileinfosubRepository;
import com.front.controller.repository.PostinfoRepository;
import com.front.controller.repository.PostinfosubRepository;
import com.front.error.Errors;
import com.front.service.CodeinfoDao;
import com.front.service.CodeinfosubDao;
import com.front.service.CustomDao;
import com.front.service.FileinfoDao;
import com.front.service.FileinfosubDao;
import com.front.service.IoService;
import com.front.service.OperationService;
import com.front.service.PostinfoDao;
import com.front.service.PostinfosubDao;
import com.front.service.TypedbDao;
import com.front.util.Constants;
import com.front.util.StringUtil;

/**
 * ホームコントローラー
 */
@Controller
public class AdminController {

	@Autowired
	CodeinfoDao codeinfoDao;
	@Autowired
	FileinfoDao fileinfoDao;
	@Autowired
	CodeinfosubDao codeinfosubDao;
	@Autowired
	FileinfosubDao fileinfosubDao;
	@Autowired
	PostinfoDao postinfoDao;
	@Autowired
	PostinfosubDao postinfosubDao;
	@Autowired
	TypedbDao typedbDao;
	@Autowired
	CustomDao customDao;
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
	@Autowired
	OperationService operationService;
	@Autowired
	Errors errors;

	@Value("${file.savedir}")
	private String dirname;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * パーツ種別プルダウンの初期化
	 * 
	 * @return
	 * @throws Exception
	 */
	@ModelAttribute("typedbList")
	List<TypedbEntity> addAttributeTypedb() throws Exception {
		List<TypedbEntity> typedbList = typedbDao.selectTypedbAll();
		return typedbList;
	}

	/**
	 * 管理者画面用全ソースコードデータの準備
	 * 
	 * @return
	 * @throws Exception
	 */
	@ModelAttribute("initMap")
	Map<Integer, Map<Object, List<Object>>> addAttributeinitMap() throws Exception {

		List<Object[]> sqlResultList = customDao.findMisyoninMap();
		List<TypedbEntity> typedbEntityList = typedbDao.selectTypedbAll();

		Map<Integer, Map<Object, List<Object>>> initMap = new HashMap<>();
		Map<Object,List<Object>> postMap = new HashMap<>();
		
		for (TypedbEntity typedbEntity : typedbEntityList) {
			for (Object[] sqlResultObj : sqlResultList) {
				List<Object> initList = Arrays.asList(sqlResultObj);

				if ((Integer) initList.get(0) == typedbEntity.getTypeid()) {
					List<Object> outList = new ArrayList<>();
					outList.add(initList.get(2));
					outList.add(initList.get(3));
					outList.add(dirname + "/src/" + initList.get(4));
					outList.add(initList.get(1));
					postMap.put(initList.get(1), outList);
				} else if ((Integer) initList.get(0) > typedbEntity.getTypeid()) {
					break;
				}
			}
			initMap.put(typedbEntity.getTypeid(), postMap);
			postMap = new HashMap<>();
		}
		return initMap;
	}

	/**
	 * 管理者画面表示
	 * 
	 * @param model モデル
	 * @return 管理者画面
	 */
	@GetMapping("/admin")
	public String admin(Model model) {

		return "admin";
	}

	/**
	 * 管理用ソース一覧画面表示
	 * 
	 * @param model モデル
	 * @return 管理者用ソース画面
	 * @throws Exception
	 */
	@GetMapping("/admin/postList")
	public String listView(Model model) throws Exception {

		return "postList";
	}

	/**
	 * 管理者用ソース削除画面表示
	 * 
	 * @param postid 投稿ID
	 * @param typeid パーツ種別ID
	 * @param model  モデル
	 * @return 管理者用ソース削除画面
	 * @throws Exception
	 */
	@GetMapping("/admin/postDetail/{postid}")
	public String deleteview(@PathVariable("postid") Integer postid, Model model) throws Exception {

		// URLチェック
		if (!StringUtil.isValidNumber(postid.toString())) {
			// URL例外
			throw errors.errorUrl();
		}

		PostinfoEntity postinfoEntity = postinfoDao.findPostByPostid(postid);
		// DBに該当の投稿が存在しない場合
		if (postinfoEntity == null) {
			throw errors.errorDbIllegal();
		}
		model.addAttribute("typename", typedbDao.findTypeByTypeid(postinfoEntity.getTypeid()).getTypename());
		model.addAttribute("htmlsrc", codeinfoDao.findCodeByPostid(postid, Constants.CODE_TYPE_HTML));
		model.addAttribute("csssrc", codeinfoDao.findCodeByPostid(postid, Constants.CODE_TYPE_CSS));
		model.addAttribute("postid", postid);

		return "postDetail";
	}

	/**
	 * 投稿削除
	 * 
	 * @param postid 投稿ID
	 * @param model  モデル
	 * @return 管理者投稿一覧画面にリダイレクト
	 * @throws Exception
	 */
	@PostMapping("/admin/postDelete")
	public String manageDelete(@RequestParam(value = "postidForDelete") int postid, Model model) throws Exception {
		// 投稿IDに紐づく情報をすべて削除
		operationService.deleteOpe(postid);

		return "redirect:/admin/postList";
	}

	/**
	 * 未承認投稿一覧画面表示
	 * 
	 * @param model モデル
	 * @return 未承認投稿一覧画面
	 * @throws Exception
	 */
	@GetMapping("/admin/unapprovedList")
	public String unapprovedList(Model model) throws Exception {

		// 未承認の投稿一覧を取得 [postdate,typename,postid,typeid]
		List<Object[]> postDataList = customDao.findPostByStatus();

		model.addAttribute("postDataList", postDataList);

		return "unapprovedList";
	}

	/**
	 * 承認画面表示
	 * 
	 * @param postid 投稿ID
	 * @param typeid パーツ種別ID
	 * @param mav    モデルアンドビュー
	 * @return
	 */
	@GetMapping("/admin/unapprovedDetail/{postid}")
	public ModelAndView manageDetail(@PathVariable("postid") Integer postid, ModelAndView mav) throws Exception {

		// URLチェック
		if (!StringUtil.isValidNumber(postid.toString())) {
			// URL例外
			throw errors.errorUrl();
		}

		PostinfoEntity postinfoEntity = postinfoDao.findPostByPostid(postid);
		FileinfoEntity fileinfoEntity = fileinfoDao.findFileByPostid(postid, Constants.FILE_TYPE_HTML);
		// DBに該当の投稿が存在しない場合
		if (postinfoEntity == null || fileinfoEntity == null) {
			throw errors.errorDbIllegal();
		}
		mav.addObject("typename", typedbDao.findTypeByTypeid(postinfoEntity.getTypeid()).getTypename());
		mav.addObject("htmlsrc", codeinfoDao.findCodeByPostid(postid, Constants.CODE_TYPE_HTML));
		mav.addObject("csssrc", codeinfoDao.findCodeByPostid(postid, Constants.CODE_TYPE_CSS));
		mav.addObject("htmllink", dirname + "/src/" + fileinfoEntity.getFilename());
		mav.addObject("postid", postid);
		mav.setViewName("unapprovedDetail");
		return mav;
	}

	/**
	 * 承認コントローラー
	 * 
	 * @param postid 投稿ID
	 * @param model  モデル
	 * @return 未承認一覧画面にリダイレクト
	 * @throws Exception
	 */
	@PostMapping("/admin/regist")
	public String postRegist(@RequestParam(value = "postidForRegist") Integer postid, Model model) throws Exception {
		PostinfoEntity postinfoEntity = postinfoDao.findPostByPostid(postid);
		postinfoEntity.setStatus(2);
		postRepos.saveAndFlush(postinfoEntity);

		return "redirect:/admin/unapprovedList";

	}

	/**
	 * 未承認投稿一覧絞り込み
	 * 
	 * @param typeid 投稿ID
	 * @param model  モデル
	 * @return 未承認投稿一覧絞り込み結果
	 * @throws Exception
	 */
	@PostMapping("/admin/filter")
	public String postFilter(@RequestParam(value = "item_type") int typeid, Model model) throws Exception {

		List<Object[]> postDataList = new ArrayList<>();
		// すべてが選択されている場合
		if (typeid == 0) {
			// 未承認の投稿一覧を取得 [postdate,typename,postid,typeid]
			postDataList = customDao.findPostByStatus();
		} else {
			// 指定したパーツ種別の未承認の投稿一覧を取得 [postdate,typename,postid,typeid]
			postDataList = customDao.findPostByStatusFilter(typeid);
		}

		model.addAttribute("postDataList", postDataList);

		return "unapprovedList";

	}

	/**
	 * 投稿非承認コントローラー
	 * 
	 * @param postid 投稿ID
	 * @param model  モデル
	 * @return 未承認投稿一覧画面にリダイレクト
	 * @throws Exception
	 */
	@PostMapping("/admin/reject")
	public String reject(@RequestParam(value = "postidForDelete") int postid, Model model) throws Exception {

		// 投稿IDに紐づく情報をすべて削除
		operationService.deleteOpe(postid);

		return "redirect:/admin/unapprovedList";
	}

	/**
	 * herokuのファイル削除復活対応
	 * 
	 * @return 管理ページでリダイレクト
	 * @throws Exception
	 */
	@GetMapping("/admin/heroku")
	public String herokuFileService() throws Exception {

		List<PostinfoEntity> postinfoEntityList = postinfoDao.findPostAll();
		List<FileinfoEntity> fileinfoEntityList = fileinfoDao.selectFileAll();
		List<CodeinfoEntity> codeinfoEntityList = codeinfoDao.selectCodeAll();

		// 投稿数ループ
		for (PostinfoEntity postinfoEntity : postinfoEntityList) {
			String htmlname = null;
			String zipname = null;
			Map<String, String> srcMap = new HashMap<>();

			// 投稿IDに紐づくファイル情報を取得
			for (FileinfoEntity fileinfoEntity : fileinfoEntityList) {
				if (fileinfoEntity.getPostid() == postinfoEntity.getPostid()) {
					if (fileinfoEntity.getFilegenre().equals(Constants.FILE_TYPE_HTML)) {
						htmlname = fileinfoEntity.getFilename();
					}
					if (fileinfoEntity.getFilegenre().equals(Constants.FILE_TYPE_ZIP)) {
						zipname = fileinfoEntity.getFilename();
					}
					if (htmlname != null && zipname != null) {
						break;
					}
				}
			}

			// 投稿IDに紐づくソースコードを取得
			for (CodeinfoEntity codeinfoEntity : codeinfoEntityList) {
				if (codeinfoEntity.getPostid() == postinfoEntity.getPostid()) {
					boolean htmlcheck = false;
					boolean csscheck = false;
					if (codeinfoEntity.getCodegenre().equals(Constants.CODE_TYPE_HTML)) {
						srcMap.put(Constants.CODE_TYPE_HTML, codeinfoEntity.getSrc());
						htmlcheck = true;
					}
					if (codeinfoEntity.getCodegenre().equals(Constants.CODE_TYPE_CSS)) {
						srcMap.put(Constants.CODE_TYPE_CSS, codeinfoEntity.getSrc());
						csscheck = true;
					}
					if (htmlcheck && csscheck) {
						break;
					}
				}
			}
			if(htmlname!=null && zipname!=null && srcMap.size()>0) {
				ioService.createHtmlFile(htmlname, srcMap, false);
				ioService.createZipFile(zipname, htmlname, false);				
			}

		}

		return "redirect:/admin";
		// public void createZipFile(String zipName, String htmlName, boolean subCheck)
	}
	
	
	/**
	 * エラーハンドリング
	 * @return
	 */
	@GetMapping("/toError")
	public String toError() {
		// 権限例外を発生させる
		throw errors.errorRole();
	}
}
