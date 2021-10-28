package com.front.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.front.controller.entity.FavoriteEntity;
import com.front.controller.entity.PostinfoEntity;
import com.front.controller.entity.PostinfosubEntity;
import com.front.controller.entity.TypedbEntity;
import com.front.error.ErrorCheck;
import com.front.error.Errors;
import com.front.security.account.Account;
import com.front.security.account.AccountService;
import com.front.service.CodeinfoService;
import com.front.service.CustomService;
import com.front.service.FavoriteinfoService;
import com.front.service.PostinfoService;
import com.front.service.PostinfosubService;
import com.front.service.TypedbService;
import com.front.util.Constants;
import com.front.util.DeleteOpe;
import com.front.util.StringUtil;

/**
 * ホームコントローラー
 */
@Controller
public class HomeController {

	@Autowired
	CodeinfoService codeinfoService;
	@Autowired
	PostinfoService postinfoService;
	@Autowired
	PostinfosubService postinfosubService;
	@Autowired
	TypedbService typedbService;
	@Autowired
	CustomService customService;
	@Autowired
	FavoriteinfoService favoriteinfoService;
	@Autowired
	AccountService accountService;

	@Autowired
	DeleteOpe deleteOpe;
	@Autowired
	Errors errors;
	@Autowired
	ErrorCheck errorCheck;

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
	 * ログイン中アカウントのお気に入り情報を初期化
	 * 
	 * @param account アカウント
	 * @return ログイン中アカウントのお気に入り情報
	 */
	@ModelAttribute("favoriteList")
	List<Object> addAttribute(@AuthenticationPrincipal Account account) {
		List<Object> favoriteList = new ArrayList<>();
		if (account != null) {
			favoriteList = favoriteinfoService.findPostidByAccountid(account.getUserid());
		}
		return favoriteList;

	}

	/**
	 * 全ソースコードデータの準備
	 * 
	 * @return
	 * @throws Exception
	 */
	@ModelAttribute("initMap")
	Map<Integer, Map<Object, List<Object>>> addAttributeinitMap() throws Exception {

		return customService.getInitMap();
	}

	/**
	 * 初期画面表示
	 * 
	 * @param uploadForm アップロードフォーム
	 * @param model      モデル
	 * @return 初期画面
	 * @throws Exception
	 */
	@GetMapping("/")
	public String index(UploadForm uploadForm, @AuthenticationPrincipal Account account, Model model) {
		if (account != null) {
			// お気に入り投稿情報をマップに格納
			Map<Integer, Map<Object, List<Object>>> favoriteMap = favoriteinfoService.getFavoriteMap(account);
			model.addAttribute("favoriteMap", favoriteMap);
			model.addAttribute("accountid", account.getUserid());
		}
		return "index";
	}

	/**
	 * お気に入りボタン押下処理
	 * 
	 * @param postid 投稿ID
	 * @param accountid アカウントID
	 * @return 初期画面リダイレクト
	 */
	@PostMapping("/favorite")
	public String favoriteRegist(@RequestParam("postid") Integer postid, @RequestParam("accountid") Integer accountid) {

		List<FavoriteEntity> favoriteEntityList = favoriteinfoService.findFavorite(postid, accountid);
		
		// 対象の投稿がお気に入りされていない場合
		if (favoriteEntityList.size() == 0) {
			favoriteinfoService.insertFavorite(accountid, postid);
		} else {
	    // 対象の投稿がお気に入りされている場合
			favoriteinfoService.deleteFavorite(favoriteEntityList.get(0));
		}

		return "redirect:/";
	}

	/**
	 * htmlファイルダウンロード処理
	 * @param codehtml htmlソース
	 * @param codecss cssソース
	 * @param response レスポンス
	 * @return htmlファイルダウンロード
	 */
	@PostMapping("/download")
	public String downloadFile(@RequestParam("codehtml") String codehtml, @RequestParam("codecss") String codecss,
			HttpServletResponse response) {
		String sampleHtmlCode = StringUtil.createSampleHtml(codehtml, codecss);
		try {
			response.setContentType("text/html; charset=utf-8");
			response.setCharacterEncoding("utf-8");
			response.getWriter().write(sampleHtmlCode);
			response.setHeader("Content-Disposition", "attachment;filename=\"sample.html\"");

		} catch (Exception e) {
			errors.errorDownload();
		}
		return null;
	}

	/**
	 * アップロードコントローラ
	 * 
	 * @param redirectAttributes フラッシュスコープ
	 * @param uploadForm         アップロードフォーム
	 * @param errorResult        エラーリスト
	 * @param model              モデル
	 * @return checkコントローラにリダイレクト
	 * @throws Exception
	 */
	@PostMapping("/upload")
	public String uploadDataCheck(RedirectAttributes redirectAttributes, @Validated UploadForm uploadForm,
			BindingResult errorResult, Model model) {

		// バリデーションエラーチェック
		if (errorResult.hasErrors()) {
			List<String> errorList = new ArrayList<>();
			for (ObjectError error : errorResult.getAllErrors()) {
				errorList.add(error.getDefaultMessage());
			}
			model.addAttribute("validationError", errorList);
			return "index";
		}

		List<String> customErrorList = new ArrayList<>();
		// カスタムチェック：外部リンクが仕込まれていないかチェック
		customErrorList = errorCheck.srcCheckHtml(uploadForm, customErrorList);
		// javascriptが仕込まれていないかチェック
		customErrorList = errorCheck.srcCheckScript(uploadForm, customErrorList);

		// エラーがある場合ホーム画面に戻る
		if (customErrorList.size() > 0) {
			// バリデーションエラーを設定
			model.addAttribute("validationError", customErrorList);
			return "index";
		}

		// パーツ種別IDチェック
		if (!StringUtil.isValidNumber(uploadForm.getTypeSelectValue())) {
			throw errors.errorIllegal();
		}

		
		// 投稿情報をDB登録
		PostinfosubEntity postinfosubEntity = postinfosubService.insertPostinfo(Integer.parseInt(uploadForm.getTypeSelectValue()));

		// リダイレクト先にアップロードフォームを渡す
		redirectAttributes.addFlashAttribute("uploadForm", uploadForm);
		// リダイレクト先に仮登録情報を渡す
		redirectAttributes.addFlashAttribute("postinfosubEntity", postinfosubEntity);

		return "redirect:/check";
	}

	/**
	 * アップロード内容確認画面表示
	 * 
	 * @param model モデル
	 * @return アップロード内容確認画面
	 * @throws Exception
	 */
	@GetMapping("/check")
	public String checkCode(@ModelAttribute("uploadForm") UploadForm uploadForm, Model model) {

		// 仮登録情報を受け取る
		PostinfosubEntity postinfosubEntity = (PostinfosubEntity) model.getAttribute("postinfosubEntity");

		// 選択種別を設定
		model.addAttribute("selecType", typedbService.findTypeByTypeid(postinfosubEntity.getTypeid()).getTypename());
		// ソースコードを設定
		model.addAttribute("postid", postinfosubEntity.getPostid());
		model.addAttribute("inputHtml", uploadForm.getHtmlInputText());
		model.addAttribute("inputCss", uploadForm.getCssInputText());
		model.addAttribute("iframeCode",
				StringUtil.createIframe(uploadForm.getHtmlInputText(), uploadForm.getCssInputText()));

		return "postCheck";
	}


	/**
	 * アップロード情報をDBに保存
	 * 
	 * @param postid 投稿ID
	 * @param uploadForm アップロードフォーム
	 * @param model モデル
	 * @return アップロード完了画面
	 */
	@PostMapping("/save")
	public String saveCode(@RequestParam(value = "postidForRegist") int postid,
			@ModelAttribute("uploadForm") UploadForm uploadForm, Model model) {

		// 仮登録情報を取得
		PostinfosubEntity postsubEntity = postinfosubService.findPostByPostid(postid);

		// 投稿情報を本番データに移行
		PostinfoEntity postinfoEntity = postinfoService.insertPostinfo(postsubEntity.getPostid(), postsubEntity.getTypeid());

		// HTMLソースコードを登録
		codeinfoService.insertCodeinfo(Constants.CODE_TYPE_HTML, postinfoEntity.getPostid(), 
				uploadForm.getHtmlInputText());
		
		// CSSソースコードを登録
		codeinfoService.insertCodeinfo(Constants.CODE_TYPE_CSS, postinfoEntity.getPostid(),
				uploadForm.getCssInputText());

		// 仮登録投稿を削除
		deleteOpe.deleteOpeForSub(postid);
		return "redirect:/comp";
	}

	/**
	 * アップロード完了画面表示
	 * 
	 * @param model モデル
	 * @return アップロード完了画面
	 */
	@GetMapping("/comp")
	public String complete(Model model) {
		return "comp";
	}

	/**
	 * 投稿キャンセルコントローラー
	 * 
	 * @param postid 投稿ID
	 * @param model  モデル
	 * @return 初期画面にリダイレクト
	 * @throws Exception
	 */
	@PostMapping("/cancel")
	public String delete(@RequestParam(value = "postidForDelete") Integer postid, Model model) {

		deleteOpe.deleteOpeForSub(postid);

		return "redirect:/";
	}

	/**
	 * ログイン画面コントローラー
	 * 
	 * @param error
	 * @param logout
	 * @param model
	 * @param session
	 * @return
	 */
	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, Model model, HttpSession session) {

		model.addAttribute("showErrorMsg", false);
		model.addAttribute("showLogoutedMsg", false);
		if (error != null) {
			if (session != null) {
				AuthenticationException ex = (AuthenticationException) session
						.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
				if (ex != null) {
					model.addAttribute("showErrorMsg", true);
					model.addAttribute("errorMsg", ex.getMessage());
				}
			}
		} else if (logout != null) {
			model.addAttribute("showLogoutedMsg", true);
		}
		return "login";
	}

	/**
	 * アカウント情報画面表示
	 * @param account アカウント
	 * @return アカウント情報画面
	 */
	@GetMapping("/createAccount")
	public String createAccount(@ModelAttribute("account") Account account) {

		return "createAccount";
	}

	/**
	 * アカウント登録処理
	 * @param account アカウント
	 * @param errorResult バリデーションチェック結果
	 * @param model モデル
	 * @return アカウント登録後、初期画面に遷移
	 */
	@PostMapping("/accountRegist")
	public String registAccount(@Validated @ModelAttribute("account") Account account, BindingResult errorResult,
			Model model) {

		// バリデーションエラーチェック
		if (errorResult.hasErrors()) {
			List<String> errorList = new ArrayList<>();
			for (ObjectError error : errorResult.getAllErrors()) {
				errorList.add(error.getDefaultMessage());
			}
			model.addAttribute("validationError", errorList);
			return "createAccount";
		}

		// カスタムエラーチェック
		List<String> errorList = new ArrayList<>();
		// ユーザーネーム重複チェック
		if (accountService.findAccountByName(account.getUsername()).size() > 0) {
			errorList.add(errors.userDuplicate());
		}
		// メールアドレス重複チェック
		if (accountService.findAccountByMail(account.getMail()).size() > 0) {
			errorList.add(errors.mailDuplicate());
		}
		if (errorList.size() > 0) {
			model.addAttribute("validationError", errorList);
			return "createAccount";
		}

		// アカウント登録
		accountService.insertAccount(account.getUsername(), account.getPassword(), account.getMail());

		return "redirect:/";
	}

//	@PostConstruct
//	public void init() throws Exception{
//		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//		Account userdata = new Account();
//		userdata.setUsername("test");
//		String hashPass = bCryptPasswordEncoder.encode("test");
//		userdata.setPassword(hashPass);
//		userdata.setMail("test@mail.com");
//		userdata.setRole("USER");
//		repos.saveAndFlush(userdata);
//	}

}
