package com.front.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

import com.front.controller.entity.CodeinfoEntity;
import com.front.controller.entity.FileinfoEntity;
import com.front.controller.entity.FileinfosubEntity;
import com.front.controller.entity.PostinfoEntity;
import com.front.controller.entity.PostinfosubEntity;
import com.front.controller.entity.TypedbEntity;
import com.front.controller.repository.CodeinfoRepository;
import com.front.controller.repository.FileinfoRepository;
import com.front.controller.repository.FileinfosubRepository;
import com.front.controller.repository.PostinfoRepository;
import com.front.controller.repository.PostinfosubRepository;
import com.front.error.ErrorCheck;
import com.front.error.Errors;
import com.front.security.account.Account;
import com.front.security.account.AccountDao;
import com.front.security.account.AccountRepository;
import com.front.service.CodeinfoDao;
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
public class HomeController {

	@Autowired
	CodeinfoDao codeinfoDao;
	@Autowired
	FileinfoDao fileinfoDao;
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
	FileinfoRepository fileRepos;
	@Autowired
	FileinfosubRepository filesubRepos;
	@Autowired
	IoService ioService;
	@Autowired
	OperationService operationService;
	@Autowired
	Errors errors;
	@Autowired
	ErrorCheck errorCheck;
	
	@Autowired
	AccountRepository accountRepos;
	@Autowired
	AccountDao accountDao;

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
	Map<Integer, Map<Object, List<Object>>> addAttributeinitMap() throws Exception {

		List<Object[]> sqlResultList = customDao.findInitMap();

		Map<Integer, Map<Object, List<Object>>> initMap = new HashMap<>();
		Map<Object,List<Object>> postMap = new HashMap<>();
		
		Object typeid = null;

		for(Object[] sqlResultObj : sqlResultList) {
			List<Object> initList = Arrays.asList(sqlResultObj);
			
			if(typeid == null) {
				typeid = initList.get(0);
			} else if(typeid != initList.get(0)) {
				initMap.put((Integer)typeid, postMap);
				postMap = new HashMap<>();
				typeid = (Integer)initList.get(0);
			}
			
			List<Object> outList = new ArrayList<>();
			outList.add(initList.get(2));
			outList.add(initList.get(3));
			outList.add(dirname + "/src/" + initList.get(4));
			outList.add(dirname + "/zip/" + initList.get(5));
			postMap.put(initList.get(1), outList);
		}
		return initMap;
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
	public String index(UploadForm uploadForm, Model model) throws Exception {
		return "index";
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
			BindingResult errorResult, Model model) throws Exception {

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
		if(!StringUtil.isValidNumber(uploadForm.getTypeSelectValue())) {
			throw errors.errorIllegal();
		}

		PostinfosubEntity postinfosubEntity = new PostinfosubEntity();
		FileinfosubEntity filehtmlsubEntity = new FileinfosubEntity();
		FileinfosubEntity filezipsubEntity = new FileinfosubEntity();

		// 投稿情報をDB登録
		postinfosubEntity.setTypeid(Integer.parseInt(uploadForm.getTypeSelectValue()));
		postinfosubEntity = postsubRepos.saveAndFlush(postinfosubEntity);

		// HTMLファイル情報を登録
		filehtmlsubEntity.setFilename("sample" + postinfosubEntity.getPostid() + Constants.FILE_EXTENSION_HTML);
		filehtmlsubEntity.setFilegenre(Constants.FILE_TYPE_HTML);
		filehtmlsubEntity.setPostid(postinfosubEntity.getPostid());
		filehtmlsubEntity = filesubRepos.saveAndFlush(filehtmlsubEntity);

		// ZIPファイル情報を登録
		filezipsubEntity.setFilename("sample" + postinfosubEntity.getPostid() + Constants.FILE_EXTENSION_ZIP);
		filezipsubEntity.setFilegenre(Constants.FILE_TYPE_ZIP);
		filezipsubEntity.setPostid(postinfosubEntity.getPostid());
		filezipsubEntity = filesubRepos.saveAndFlush(filezipsubEntity);

		Map<String,String> srcMap = new HashMap<>();
		srcMap.put(Constants.CODE_TYPE_HTML, uploadForm.getHtmlInputText());
		srcMap.put(Constants.CODE_TYPE_CSS, uploadForm.getCssInputText());

		// sample{id}.htmlを生成
		ioService.createHtmlFile(filehtmlsubEntity.getFilename(), srcMap, true);
		// sample{id}.zipを生成
		ioService.createZipFile(filezipsubEntity.getFilename(), filehtmlsubEntity.getFilename(), true);

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
	public String checkCode(@ModelAttribute("uploadForm") UploadForm uploadForm, Model model) throws Exception {

		System.out.println(uploadForm.getHtmlInputText());
		// 仮登録情報を受け取る
		PostinfosubEntity postinfosubEntity = (PostinfosubEntity) model.getAttribute("postinfosubEntity");

		// htmlのリンクを設定
		String filehtml = dirname + "/subsrc/" + fileinfosubDao
				.findFileByPostid(postinfosubEntity.getPostid(), Constants.FILE_TYPE_HTML).getFilename();
		model.addAttribute("filehtmlLink", filehtml);
		// 選択種別を設定
		model.addAttribute("selecType", typedbDao.findTypeByTypeid(postinfosubEntity.getTypeid()).getTypename());
		// ソースコードを設定
		model.addAttribute("postid", postinfosubEntity.getPostid());
		model.addAttribute("inputHtml",uploadForm.getHtmlInputText());
		model.addAttribute("inputCss",uploadForm.getCssInputText());

		return "postCheck";
	}

	/**
	 * アップロード内容保存
	 * 
	 * @param postid    投稿ID
	 * @param inputHtml htmlコード
	 * @param inputCss  cssコード
	 * @param model     モデル
	 * @return アップロード完了画面にリダイレクト
	 * @throws Exception
	 */
	@PostMapping("/save")
	public String saveCode(@RequestParam(value = "postidForRegist") int postid,
			@ModelAttribute("uploadForm") UploadForm uploadForm,
			Model model) throws Exception {
		
		System.out.println(uploadForm.getHtmlInputText());

		LocalDateTime nowDate = LocalDateTime.now();
		// TODO DB自動設定で今日の日付を設定できないか検討

		PostinfosubEntity postsubEntity = postinfosubDao.findPostByPostid(postid);
		FileinfosubEntity fileSubHtml = fileinfosubDao.findFileByPostid(postid, Constants.FILE_TYPE_HTML);
		FileinfosubEntity fileSubZip = fileinfosubDao.findFileByPostid(postid, Constants.FILE_TYPE_ZIP);

		PostinfoEntity postinfoEntity = new PostinfoEntity();
		CodeinfoEntity codehtmlEntity = new CodeinfoEntity();
		CodeinfoEntity codecssEntity = new CodeinfoEntity();
		FileinfoEntity filehtmlEntity = new FileinfoEntity();
		FileinfoEntity filezipEntity = new FileinfoEntity();

		// 投稿情報を本番データに移行
		postinfoEntity.setPostid(postsubEntity.getPostid());
		postinfoEntity.setPostdate(Constants.DATE_FORMAT.format(nowDate));
		postinfoEntity.setStatus(Constants.POSTINFO_STATUS_MISHONIN);
		postinfoEntity.setTypeid(postsubEntity.getTypeid());
		postRepos.saveAndFlush(postinfoEntity);

		// HTMLソースコードを設定
		codehtmlEntity.setCodegenre(Constants.CODE_TYPE_HTML);
		codehtmlEntity.setPostid(postinfoEntity.getPostid());
		codehtmlEntity.setSrc(uploadForm.getHtmlInputText());
		codehtmlEntity = codeRepos.saveAndFlush(codehtmlEntity);

		// CSSソースコードを登録
		codecssEntity.setCodegenre(Constants.CODE_TYPE_CSS);
		codecssEntity.setPostid(postinfoEntity.getPostid());
		codecssEntity.setSrc(uploadForm.getCssInputText());
		codecssEntity = codeRepos.saveAndFlush(codecssEntity);

		// HTMLファイル情報を登録
		filehtmlEntity.setFilename(fileSubHtml.getFilename());
		filehtmlEntity.setFilegenre(Constants.FILE_TYPE_HTML);
		filehtmlEntity.setPostid(postinfoEntity.getPostid());
		filehtmlEntity = fileRepos.saveAndFlush(filehtmlEntity);

		// ZIPファイル情報を登録
		filezipEntity.setFilename(fileSubZip.getFilename());
		filezipEntity.setFilegenre(Constants.FILE_TYPE_ZIP);
		filezipEntity.setPostid(postinfoEntity.getPostid());
		filezipEntity = fileRepos.saveAndFlush(filezipEntity);

		Map<String,String> srcMap = new HashMap<>();
		srcMap.put(Constants.CODE_TYPE_HTML, codehtmlEntity.getSrc());
		srcMap.put(Constants.CODE_TYPE_CSS, codecssEntity.getSrc());

		// sample{id}.htmlを生成
		ioService.createHtmlFile(filehtmlEntity.getFilename(), srcMap, false);
		// sample{id}.zipを生成
		ioService.createZipFile(filezipEntity.getFilename(), filehtmlEntity.getFilename(), false);

		operationService.deleteOpeForSub(postid);
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
	public String delete(@RequestParam(value = "postidForDelete") Integer postid, Model model) throws Exception {

		operationService.deleteOpeForSub(postid);

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
            @RequestParam(value = "logout", required = false) String logout,
            Model model, HttpSession session) {

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
    
    
    @GetMapping("/createAccount")
    public String createAccount(@ModelAttribute("account") Account account) {
    	
    	return "createAccount";
    }
    
    @PostMapping("/accountRegist")
    public String registAccount(@Validated @ModelAttribute("account")Account account, BindingResult errorResult
    		, Model model) {
    	
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
		if(accountDao.findAccountByName(account.getUsername()).size()>0) {
			errorList.add(errors.userDuplicate());
		}
		// メールアドレス重複チェック
		if(accountDao.findAccountByMail(account.getMail()).size()>0) {
			errorList.add(errors.mailDuplicate());
		}
		if(errorList.size()>0) {
			model.addAttribute("validationError", errorList);
			return "createAccount";
		}
		

		// パスワードエンコード
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String hashPass = bCryptPasswordEncoder.encode(account.getPassword());
		
		Account newAccount = new Account();
		newAccount.setUsername(account.getUsername());
		newAccount.setPassword(hashPass);
		newAccount.setMail(account.getMail());
		newAccount.setRole(newAccount.getRoleUser());
    	accountRepos.saveAndFlush(newAccount);
    	logger.info("アカウントを登録しました：" + newAccount.getUsername());
    	
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
