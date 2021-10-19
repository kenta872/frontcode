package com.front.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.front.entity.CodeinfoEntity;
import com.front.entity.FileinfoEntity;
import com.front.entity.PostinfoEntity;
import com.front.entity.TypedbEntity;
import com.front.repository.CodeinfoRepository;
import com.front.repository.CodeinfosubRepository;
import com.front.repository.FileinfoRepository;
import com.front.repository.FileinfosubRepository;
import com.front.repository.PostinfoRepository;
import com.front.repository.PostinfosubRepository;
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
	 * 全ソースコードデータの準備
	 * 
	 * @return
	 * @throws Exception
	 */
	@ModelAttribute("initMap")
	Map<Integer, Map<Integer, List<String>>> addAttributeinitMap() throws Exception {

		// 項目種別一覧取得
		List<TypedbEntity> typedbList = typedbDao.selectTypedbAll();
		// 未承認投稿一覧を取得
		List<PostinfoEntity> postinfoList = postinfoDao.findPostByStatus(2);
		// ソースコード一覧取得
		List<CodeinfoEntity> codeinfoList = codeinfoDao.selectCodeAll();
		// ファイル一覧を取得
		List<FileinfoEntity> fileinfoList = fileinfoDao.selectFileAll();

		Map<Integer, Map<Integer, List<String>>> initMap = new HashMap<>();
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
					List<String> outlist = new ArrayList<>();
					outlist.add(codehtml);
					outlist.add(codecss);
					outlist.add(codejs);
					outlist.add(filehtml);
					outlist.add(String.valueOf(postid));
					outlist.add(String.valueOf(typeid));
					codeMap.put(postid, outlist);
				}
				initMap.put(typeid, codeMap);
			}
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
	@GetMapping("/listView")
	public String listView(Model model) throws Exception {

		return "listView";
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
	@PostMapping("/deleteView")
	public String deleteview(@RequestParam(value = "postidForDelete") Integer postid,
			@RequestParam(value = "typeidForDelete") Integer typeid, Model model) throws Exception {

		FileinfoEntity fileinfoEntity = fileinfoDao.findFileByPostid(postid, Constants.FILE_TYPE_HTML);
		model.addAttribute("typename", typedbDao.findTypeByTypeid(typeid).getTypename());
		model.addAttribute("htmlsrc", codeinfoDao.findCodeByPostid(postid, Constants.CODE_TYPE_HTML));
		model.addAttribute("csssrc", codeinfoDao.findCodeByPostid(postid, Constants.CODE_TYPE_CSS));
		model.addAttribute("jssrc", codeinfoDao.findCodeByPostid(postid, Constants.CODE_TYPE_JS));
		model.addAttribute("postid", postid);

		return "deleteView";
	}

	/**
	 * 管理画面削除コントローラー URL：/manageDetail
	 */
	@PostMapping("/manageDelete")
	public String manageDelete(@RequestParam(value = "postidForDelete") int postid, Model model) throws Exception {
		// 投稿IDに紐づく情報をすべて削除
		operationService.deleteOpe(postid);

		return "redirect:/listView";
	}

	/**
	 * 未承認投稿一覧画面表示
	 * 
	 * @param model モデル
	 * @return 未承認投稿一覧画面
	 * @throws Exception
	 */
	@GetMapping("unapprovedList")
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
	@PostMapping("/manageDetail")
	public ModelAndView manageDetail(@RequestParam(value = "postidForManage") Integer postid,
			@RequestParam(value = "typeidForManage") Integer typeid, ModelAndView mav) {

		FileinfoEntity fileinfoEntity = fileinfoDao.findFileByPostid(postid, Constants.FILE_TYPE_HTML);
		mav.addObject("typename", typedbDao.findTypeByTypeid(typeid).getTypename());
		mav.addObject("htmlsrc", codeinfoDao.findCodeByPostid(postid, Constants.CODE_TYPE_HTML));
		mav.addObject("csssrc", codeinfoDao.findCodeByPostid(postid, Constants.CODE_TYPE_CSS));
		mav.addObject("jssrc", codeinfoDao.findCodeByPostid(postid, Constants.CODE_TYPE_JS));
		mav.addObject("htmllink", dirname + "/src/" + fileinfoEntity.getFilename());
		mav.addObject("postid", postid);
		mav.setViewName("manageDetail");
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
	@PostMapping("/regist")
	public String postRegist(@RequestParam(value = "postidForRegist") Integer postid, Model model) throws Exception {
		PostinfoEntity postinfoEntity = postinfoDao.findPostByPostid(postid);
		postinfoEntity.setStatus(2);
		postRepos.saveAndFlush(postinfoEntity);

		return "redirect:/unapprovedList";

	}

	/**
	 * 未承認投稿一覧絞り込み
	 * 
	 * @param typeid 投稿ID
	 * @param model  モデル
	 * @return 未承認投稿一覧絞り込み結果
	 * @throws Exception
	 */
	@PostMapping("/filter")
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
	@PostMapping("/reject")
	public String reject(@RequestParam(value = "postidForDelete") int postid, Model model) throws Exception {

		// 投稿IDに紐づく情報をすべて削除
		operationService.deleteOpe(postid);

		return "redirect:/unapprovedList";
	}

//	/**
//	 * ログイン画面コントローラー
//	 * 
//	 * @param error
//	 * @param logout
//	 * @param model
//	 * @param session
//	 * @return
//	 */
//    @GetMapping("/login")
//    public String login(@RequestParam(value = "error", required = false) String error,
//            @RequestParam(value = "logout", required = false) String logout,
//            Model model, HttpSession session) {
//
//        model.addAttribute("showErrorMsg", false);
//        model.addAttribute("showLogoutedMsg", false);
//        if (error != null) {
//            if (session != null) {
//                AuthenticationException ex = (AuthenticationException) session
//                        .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
//                if (ex != null) {
//                    model.addAttribute("showErrorMsg", true);
//                    model.addAttribute("errorMsg", ex.getMessage());
//                }
//            }
//        } else if (logout != null) {
//            model.addAttribute("showLogoutedMsg", true);
//        }
//        return "login";
//    }

}
