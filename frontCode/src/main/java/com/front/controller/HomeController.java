package com.front.controller;

import java.time.LocalDateTime;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.front.entity.CodeinfoEntity;
import com.front.entity.CodeinfosubEntity;
import com.front.entity.FileinfoEntity;
import com.front.entity.FileinfosubEntity;
import com.front.entity.PostinfoEntity;
import com.front.entity.PostinfosubEntity;
import com.front.entity.TypedbEntity;
import com.front.error.ErrorCheck;
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
	 * @return
	 * @throws Exception
	 */
	@ModelAttribute("initMap")
	Map<Integer,Map<Integer,List<String>>> addAttributeinitMap() throws Exception {
		
		// 項目種別一覧取得
		List<TypedbEntity> typedbList = typedbDao.selectTypedbAll();
		// 未承認投稿一覧を取得
		List<PostinfoEntity> postinfoList = postinfoDao.findPostByStatus(2);
		// ソースコード一覧取得
		List<CodeinfoEntity> codeinfoList = codeinfoDao.selectCodeAll();
		// ファイル一覧を取得
		List<FileinfoEntity> fileinfoList = fileinfoDao.selectFileAll();
		
		Map<Integer,Map<Integer,List<String>>> initMap = new HashMap<>();
		
		// パーツ種別の数だけループ
		for(int typeidIndex=0;typeidIndex<typedbList.size();typeidIndex++) {
			int typeid = typedbList.get(typeidIndex).getTypeid();
			Map<Integer,List<String>> codeMap = new HashMap<>();
			// 投稿されている数だけループ
			for(int i=0;i<postinfoList.size();i++) {
				// パーツ種別に紐づく投稿の場合
				if(typeid == postinfoList.get(i).getTypeid()) {
					int postid = postinfoList.get(i).getPostid();
					String codehtml = null;
					String codecss = null;
					String codejs = null;
					String filehtml = dirname + "/src/";
					String filezip = dirname + "/zip/";
					// ソースコードの数だけループ
					for(int codeinfoIndex=0;codeinfoIndex<codeinfoList.size();codeinfoIndex++) {
						boolean htmlHit = false;
						boolean cssHit = false;
						boolean jsHit = false;
						// 投稿IDに紐づくソースコードがある場合
						if(codeinfoList.get(codeinfoIndex).getPostid() == postid) {
							if(codeinfoList.get(codeinfoIndex).getCodegenre().equals(Constants.CODE_TYPE_HTML)) {
								codehtml = codeinfoList.get(codeinfoIndex).getSrc();
								htmlHit = true;
							} else if(codeinfoList.get(codeinfoIndex).getCodegenre().equals(Constants.CODE_TYPE_CSS)) {
								codecss = codeinfoList.get(codeinfoIndex).getSrc();
								cssHit = true;
							} else if(codeinfoList.get(codeinfoIndex).getCodegenre().equals(Constants.CODE_TYPE_JS)) {
								codejs = codeinfoList.get(codeinfoIndex).getSrc();
								jsHit = true;
							}
						}
						if(htmlHit==true && cssHit==true && jsHit==true) {
							break;
						}
					}
					
					// ファイル一覧の数だけループ
					for(int fileinfoIndex=0;fileinfoIndex<fileinfoList.size();fileinfoIndex++) {
						boolean htmlHit = false;
						boolean zipHit = false;
						// 投稿IDと紐づくファイルがあった場合
						if(fileinfoList.get(fileinfoIndex).getPostid() == postid) {
							if(fileinfoList.get(fileinfoIndex).getFilegenre().equals(Constants.FILE_TYPE_HTML)) {
								filehtml = filehtml + fileinfoList.get(fileinfoIndex).getFilename();
								htmlHit = true;
							}
							if(fileinfoList.get(fileinfoIndex).getFilegenre().equals(Constants.FILE_TYPE_ZIP)) {
								filezip = filezip + fileinfoList.get(fileinfoIndex).getFilename();
								zipHit = true;
							}
						}
						if(htmlHit == true && zipHit == true) {
							break;
						}
					}
					List<String> outlist = new ArrayList<>();
					outlist.add(codehtml);
					outlist.add(codecss);
					outlist.add(codejs);
					outlist.add(filehtml);
					outlist.add(filezip);
					codeMap.put(postid, outlist);
				}
				initMap.put(typeid, codeMap);
			}
		}
		return initMap;
	}
	
	/**
	 * 初期画面表示
	 * @param uploadForm アップロードフォーム
	 * @param model モデル
	 * @return 初期画面
	 * @throws Exception
	 */
	@GetMapping("/")
	public String index(UploadForm uploadForm, Model model) throws Exception {
		
		return "index";
	}
	
	/**
	 * アップロードコントローラ
	 * @param redirectAttributes フラッシュスコープ
	 * @param uploadForm アップロードフォーム
	 * @param errorResult エラーリスト
	 * @param model モデル
	 * @return checkコントローラにリダイレクト
	 * @throws Exception
	 */
	@PostMapping("/upload")
	public String uploadDataCheck(RedirectAttributes redirectAttributes, @Validated UploadForm uploadForm
			, BindingResult errorResult, Model model) throws Exception {
		
		// バリデーションエラーチェック
        if (errorResult.hasErrors()) {
        	List<String> errorList = new ArrayList<>();
            for (ObjectError error : errorResult.getAllErrors()) {
                errorList.add(error.getDefaultMessage());
            }
			//バリデーションエラーを設定
			model.addAttribute("validationError", errorList);
			
            return "index";
        }
        
		// カスタムエラーチェック
		List<String> customErrorList = new ArrayList<>();
		// htmlソースに「http」が仕込まれていないかチェック
		customErrorList = ErrorCheck.srcCheckHtml(uploadForm, customErrorList);
		// TODO uploadForm.getTypeSelectValue()が半角数字であるかをチェックする必要あり
		
		if(customErrorList.size() > 0) {
			//バリデーションエラーを設定
			model.addAttribute("validationError", customErrorList);
			return "index";
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
		

		List<String> srcList = new ArrayList<>();
		srcList.add(uploadForm.getHtmlInputText());
		srcList.add(uploadForm.getCssInputText());
		srcList.add(uploadForm.getJsInputText());
		
		// sample{id}.htmlを生成
		ioService.createHtmlFile(filehtmlsubEntity.getFilename(), srcList, true);
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
	 * @param model モデル
	 * @return アップロード内容確認画面
	 * @throws Exception
	 */
	@GetMapping("/check")
	public String checkCode(Model model) throws Exception  {
		
		// アップロードフォームデータを受け取る
		UploadForm uploadForm = (UploadForm)model.getAttribute("uploadForm");
		// 仮登録情報を受け取る
		PostinfosubEntity postinfosubEntity = (PostinfosubEntity)model.getAttribute("postinfosubEntity");
		
		// htmlのリンクを設定
		String filehtml = dirname + "/subsrc/" + fileinfosubDao.findFileByPostid(postinfosubEntity.getPostid(), Constants.FILE_TYPE_HTML).getFilename();
		model.addAttribute("filehtmlLink", filehtml);
		// 選択種別を設定
		model.addAttribute("selecType",typedbDao.findTypeByTypeid(postinfosubEntity.getTypeid()).getTypename());
		// ソースコードを設定
		model.addAttribute("inputHtml", uploadForm.getHtmlInputText());
		model.addAttribute("inputCss", uploadForm.getCssInputText());
		model.addAttribute("inputJs", uploadForm.getJsInputText());
		model.addAttribute("postid", postinfosubEntity.getPostid());
		
		// TODO リロードされた場合はエラーページにメッセージを出力する（メッセージ内容を考える）
		return "codeCheck";
	}
	
	

	/**
	 * アップロード内容保存
	 * @param postid 投稿ID
	 * @param inputHtml htmlコード
	 * @param inputCss cssコード
	 * @param inputJs jsコード
	 * @param model モデル
	 * @return アップロード完了画面にリダイレクト
	 * @throws Exception
	 */
	@PostMapping("/save")
	public String saveCode(@RequestParam(value="postidForRegist") int postid
			, @RequestParam(value="htmlForRegist") String inputHtml
			, @RequestParam(value="cssForRegist") String inputCss
			, @RequestParam(value="jsForRegist") String inputJs
			, Model model) throws Exception {
		
		LocalDateTime nowDate = LocalDateTime.now();
		// TODO DB自動設定で今日の日付を設定できないか検討
		
		
		PostinfosubEntity postsubEntity = postinfosubDao.findPostByPostid(postid);
		FileinfosubEntity fileSubHtml = fileinfosubDao.findFileByPostid(postid, Constants.FILE_TYPE_HTML);
		FileinfosubEntity fileSubZip = fileinfosubDao.findFileByPostid(postid,Constants.FILE_TYPE_ZIP);

		PostinfoEntity postinfoEntity = new PostinfoEntity();
		CodeinfoEntity codehtmlEntity = new CodeinfoEntity();
		CodeinfoEntity codecssEntity = new CodeinfoEntity();
		CodeinfoEntity codejsEntity = new CodeinfoEntity();
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
		codehtmlEntity.setSrc(inputHtml);
		codehtmlEntity = codeRepos.saveAndFlush(codehtmlEntity);
		
		// CSSソースコードを登録
		codecssEntity.setCodegenre(Constants.CODE_TYPE_CSS);
		codecssEntity.setPostid(postinfoEntity.getPostid());
		codecssEntity.setSrc(inputCss);
		codecssEntity = codeRepos.saveAndFlush(codecssEntity);
		
		// JSソースコードを登録
		codejsEntity.setCodegenre(Constants.CODE_TYPE_JS);
		codejsEntity.setPostid(postinfoEntity.getPostid());
		codejsEntity.setSrc(inputJs);
		codejsEntity = codeRepos.saveAndFlush(codejsEntity);
		
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
				
		List<String> codeList = new ArrayList<>();
		codeList.add(StringUtil.convertToEscape(codehtmlEntity.getSrc()));
		codeList.add(StringUtil.convertToEscape(codecssEntity.getSrc()));
		codeList.add(StringUtil.convertToEscape(codejsEntity.getSrc()));
		
		// sample{id}.htmlを生成
		ioService.createHtmlFile(filehtmlEntity.getFilename(), codeList, false);
		// sample{id}.zipを生成
		ioService.createZipFile(filezipEntity.getFilename(), filehtmlEntity.getFilename(), false);
		
		operationService.deleteOpeForSub(postid);
		return "redirect:/comp";
	}
	
	/**
	 * アップロード完了画面表示
	 * @param model モデル
	 * @return アップロード完了画面
	 */
	@GetMapping("/comp")
	public String complete(Model model) {
		return "comp";
	}

	
	/**
	 * 投稿キャンセルコントローラー
	 * @param postid 投稿ID
	 * @param model モデル
	 * @return 初期画面にリダイレクト
	 * @throws Exception
	 */
	@PostMapping("/cancel")
	public String delete(@RequestParam(value="postidForDelete") Integer postid, Model model) throws Exception {
		
		operationService.deleteOpeForSub(postid);
		
		return "redirect:/";
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
