package com.front.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.front.controller.entity.PostinfoEntity;
import com.front.controller.entity.TypedbEntity;
import com.front.error.Errors;
import com.front.service.CodeinfoService;
import com.front.service.CustomService;
import com.front.service.PostinfoService;
import com.front.service.TypedbService;
import com.front.util.Constants;
import com.front.util.DeleteOpe;
import com.front.util.StringUtil;

/**
 * アドミンコントローラ
 */
@Controller
public class AdminController {

	@Autowired
	CodeinfoService codeinfoService;
	@Autowired
	PostinfoService postinfoService;
	@Autowired
	TypedbService typedbService;
	@Autowired
	CustomService customService;
	@Autowired
	DeleteOpe deleteOpe;
	
	@Autowired
	Errors errors;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * パーツ種別プルダウンの初期化
	 * 
	 * @return
	 * @throws Exception
	 */
	@ModelAttribute("typedbList")
	List<TypedbEntity> addAttributeTypedb() throws Exception {
		List<TypedbEntity> typedbList = typedbService.selectTypedbAll();
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

		Map<Integer, Map<Object, List<Object>>> initMap = new HashMap<>();
		Map<Object,List<Object>> postMap = new HashMap<>();
		
		List<TypedbEntity> typedbEntityList = typedbService.selectTypedbAll();
		List<Object[]> sqlResultList = customService.findMisyoninMap();
		// パーツ種別の数ループ
		for (TypedbEntity typedbEntity : typedbEntityList) {
			// 公開する投稿情報の数ループ
			for (Object[] sqlResultObj : sqlResultList) {
				List<Object> initList = Arrays.asList(sqlResultObj);

				// 投稿情報一覧とパーツ種別が一致した場合にマップに格納
				// sql結果　0：パーツ種別 1：投稿ID 2：htmlソース 3：cssソース
				if ((Integer) initList.get(0) == typedbEntity.getTypeid()) {
					List<Object> outList = new ArrayList<>();
					outList.add(initList.get(2));
					outList.add(initList.get(3));
					outList.add(StringUtil.createIframe((String)initList.get(2), (String)initList.get(3)));
					outList.add(initList.get(1));
					postMap.put(initList.get(1), outList);
				} else if ((Integer) initList.get(0) > typedbEntity.getTypeid()) {
				// sqlでパーツ種別ソートしているため、この条件を採用
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
		
		// DBに該当の投稿が存在しない場合
		PostinfoEntity postinfoEntity = postinfoService.findPostByPostid(postid);
		if (postinfoEntity == null) {
			throw errors.errorDbIllegal();
		}
		model.addAttribute("typename", typedbService.findTypeByTypeid(postinfoEntity.getTypeid()).getTypename());
		model.addAttribute("htmlsrc", codeinfoService.findCodeByPostid(postid, Constants.CODE_TYPE_HTML));
		model.addAttribute("csssrc", codeinfoService.findCodeByPostid(postid, Constants.CODE_TYPE_CSS));
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
		deleteOpe.deleteOpe(postid);

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
		List<Object[]> postDataList = customService.findPostByStatus();

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

		PostinfoEntity postinfoEntity = postinfoService.findPostByPostid(postid);
		// DBに該当の投稿が存在しない場合
		if (postinfoEntity == null) {
			throw errors.errorDbIllegal();
		}
		mav.addObject("typename", typedbService.findTypeByTypeid(postinfoEntity.getTypeid()).getTypename());
		mav.addObject("htmlsrc", codeinfoService.findCodeByPostid(postid, Constants.CODE_TYPE_HTML));
		mav.addObject("csssrc", codeinfoService.findCodeByPostid(postid, Constants.CODE_TYPE_CSS));
		mav.addObject("iframeData",StringUtil.createIframe(codeinfoService.findCodeByPostid(postid, Constants.CODE_TYPE_HTML), 
				codeinfoService.findCodeByPostid(postid, Constants.CODE_TYPE_CSS)));
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
		PostinfoEntity postinfoEntity = postinfoService.findPostByPostid(postid);
		postinfoService.chageStatus(postinfoEntity);

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
			postDataList = customService.findPostByStatus();
		} else {
			// 指定したパーツ種別の未承認の投稿一覧を取得 [postdate,typename,postid,typeid]
			postDataList = customService.findPostByStatusFilter(typeid);
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
		deleteOpe.deleteOpe(postid);

		return "redirect:/admin/unapprovedList";
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
