package com.front.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.front.controller.entity.CodeinfoEntity;
import com.front.controller.entity.CodeinfosubEntity;
import com.front.controller.entity.FileinfoEntity;
import com.front.controller.entity.FileinfosubEntity;
import com.front.controller.entity.PostinfoEntity;
import com.front.controller.entity.PostinfosubEntity;
import com.front.controller.entity.TypedbEntity;
import com.front.controller.repository.CodeinfoRepository;
import com.front.controller.repository.CodeinfosubRepository;
import com.front.controller.repository.FileinfoRepository;
import com.front.controller.repository.FileinfosubRepository;
import com.front.controller.repository.PostinfoRepository;
import com.front.controller.repository.PostinfosubRepository;
import com.front.error.ErrorCheck;
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
 * アクセスコントローラー
 */
@Controller
public class ServiceController {
	
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
//	
//	/**
//	 * ホーム画面表示
//	 */
//	@GetMapping("/")
//	public String index(UploadForm uploadForm, Model model) throws Exception {
//		
//		// 項目種別一覧取得
//		List<TypedbEntity> typedbList = typedbDao.selectTypedbAll();
//		model.addAttribute("typedbList",typedbList);
//		
//		// 全ソースコード情報を取得
//		Map<Integer,Map<Integer,List<String>>> indexMap = operationService.getSrcAllOpe();
//		model.addAttribute("codeMap", indexMap);
//		
//		return "index";
//	}
//	
//	/**
//	 * アップロード
//	 */
//	@PostMapping("/upload")
//	public String upload(@Validated UploadForm uploadForm, BindingResult errorResult, Model model) throws Exception  {
//		
//		
//		// カスタムエラーチェック
//		List<String> errorList = new ArrayList();
//		errorList = ErrorCheck.srcCheckHtml(uploadForm, errorList);
//		
//		// バリデーションエラーチェック
//        if (errorResult.hasErrors() || errorList.size()!=0) {
//            for (ObjectError error : errorResult.getAllErrors()) {
//                errorList.add(error.getDefaultMessage());
//            }
//            
//			//バリデーションエラーを設定
//			model.addAttribute("validationError", errorList);
//			
//            // 項目種別一覧取得
//			List<TypedbEntity> typedbList = typedbDao.selectTypedbAll();
//			model.addAttribute("typedbList",typedbList);
//			
//			// 全ソースコード情報を取得
//			Map<Integer,Map<Integer,List<String>>> indexMap = operationService.getSrcAllOpe();
//			model.addAttribute("codeMap", indexMap);
//            return "index";
//        }
//
//		
//		PostinfosubEntity postinfosubEntity = new PostinfosubEntity();
//		CodeinfosubEntity codehtmlsubEntity = new CodeinfosubEntity();
//		CodeinfosubEntity codecsssubEntity = new CodeinfosubEntity();
//		CodeinfosubEntity codejssubEntity = new CodeinfosubEntity();
//		FileinfosubEntity filehtmlsubEntity = new FileinfosubEntity();
//		FileinfosubEntity filezipsubEntity = new FileinfosubEntity();
//		
//		// 投稿情報を設定してにDB登録
//		postinfosubEntity.setTypeid(uploadForm.getTypeSelectValue());
//		postinfosubEntity = postsubRepos.saveAndFlush(postinfosubEntity);
//		
//		// HTMLソースコードを設定
//		codehtmlsubEntity.setCodegenre(Constants.CODE_TYPE_HTML);
//		codehtmlsubEntity.setPostid(postinfosubEntity.getPostid());
//		codehtmlsubEntity.setSrc(uploadForm.getHtmlInputText());
//		codehtmlsubEntity = codesubRepos.saveAndFlush(codehtmlsubEntity);
//		
//		// CSSソースコードを登録
//		codecsssubEntity.setCodegenre(Constants.CODE_TYPE_CSS);
//		codecsssubEntity.setPostid(postinfosubEntity.getPostid());
//		codecsssubEntity.setSrc(uploadForm.getCssInputText());
//		codecsssubEntity = codesubRepos.saveAndFlush(codecsssubEntity);
//		
//		// JSソースコードを登録
//		codejssubEntity.setCodegenre(Constants.CODE_TYPE_JS);
//		codejssubEntity.setPostid(postinfosubEntity.getPostid());
//		codejssubEntity.setSrc(uploadForm.getJsInputText());
//		codejssubEntity = codesubRepos.saveAndFlush(codejssubEntity);
//		
//		// HTMLファイル情報を登録
//		filehtmlsubEntity.setFilename("sample" + postinfosubEntity.getPostid() + Constants.FILE_EXTENSION_HTML);
//		filehtmlsubEntity.setFilegenre(Constants.FILE_TYPE_HTML);
//		filehtmlsubEntity.setPostid(postinfosubEntity.getPostid());
//		filehtmlsubEntity = filesubRepos.saveAndFlush(filehtmlsubEntity);
//		
//		// ZIPファイル情報を登録
//		filezipsubEntity.setFilename("sample" + postinfosubEntity.getPostid() + Constants.FILE_EXTENSION_ZIP);
//		filezipsubEntity.setFilegenre(Constants.FILE_TYPE_ZIP);
//		filezipsubEntity.setPostid(postinfosubEntity.getPostid());
//		filezipsubEntity = filesubRepos.saveAndFlush(filezipsubEntity);
//		
//		// 投稿したソースコード一覧を取得
//		List<CodeinfosubEntity> codeinfoEntityList = codeinfosubDao.findSrcByPostid(postinfosubEntity.getPostid());
//
//		model.addAttribute("postSrc",customDao.searchSrcByPostidFromSub(postinfosubEntity.getPostid(),postinfosubEntity.getTypeid()));
//		model.addAttribute("postid",postinfosubEntity.getPostid());
//
//		List<String> srcList = new ArrayList();
//		srcList.add(StringUtil.convertToEscape(codehtmlsubEntity.getSrc()));
//		srcList.add(StringUtil.convertToEscape(codecsssubEntity.getSrc()));
//		srcList.add(StringUtil.convertToEscape(codejssubEntity.getSrc()));
//		
//		try {
//			// sample{id}.htmlを生成
//			ioService.createHtmlFile(filehtmlsubEntity.getFilename(), srcList, true);
//			
//			// sample{id}.zipを生成
//			ioService.createZipFile(filezipsubEntity.getFilename(), filehtmlsubEntity.getFilename(), true);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "redirect:/";
//		}
//		
//		// ファイルリンクを設定
//		String filehtml = dirname + "/subsrc/" + filehtmlsubEntity.getFilename();
//		model.addAttribute("filehtmlLink",filehtml);
//		
//
//		return "codeCheck";
//	}
//	
//	/**
//	 * パーツ追加確認画面コントローラー
//	 */
//	@PostMapping("/comp")
//	public String comp(@RequestParam(value="postidForRegist") int postid, Model model) throws Exception {
//		
//		LocalDateTime nowDate = LocalDateTime.now();
//		
//		PostinfosubEntity postsubEntity = postinfosubDao.findPostByPostid(postid);
//		FileinfosubEntity fileSubHtml = fileinfosubDao.findFileByPostid(postid, Constants.FILE_TYPE_HTML);
//		FileinfosubEntity fileSubZip = fileinfosubDao.findFileByPostid(postid,Constants.FILE_TYPE_ZIP);
//		CodeinfosubEntity codeSubHtml = codeinfosubDao.searchSrcByPostid(postid, Constants.CODE_TYPE_HTML);
//		CodeinfosubEntity codeSubCss = codeinfosubDao.searchSrcByPostid(postid, Constants.CODE_TYPE_CSS);
//		CodeinfosubEntity codeSubJs = codeinfosubDao.searchSrcByPostid(postid, Constants.CODE_TYPE_JS);
//		
//		PostinfoEntity postinfoEntity = new PostinfoEntity();
//		CodeinfoEntity codehtmlEntity = new CodeinfoEntity();
//		CodeinfoEntity codecssEntity = new CodeinfoEntity();
//		CodeinfoEntity codejsEntity = new CodeinfoEntity();
//		FileinfoEntity filehtmlEntity = new FileinfoEntity();
//		FileinfoEntity filezipEntity = new FileinfoEntity();
//		
//		// 投稿情報を本番データに移行
//		postinfoEntity.setPostid(postsubEntity.getPostid());
//		postinfoEntity.setPostdate(Constants.DATE_FORMAT.format(nowDate));
//		postinfoEntity.setStatus(Constants.POSTINFO_STATUS_MISHONIN);
//		postinfoEntity.setTypeid(postsubEntity.getTypeid());
//		postRepos.saveAndFlush(postinfoEntity);
//		
//		// HTMLソースコードを設定
//		codehtmlEntity.setCodegenre(Constants.CODE_TYPE_HTML);
//		codehtmlEntity.setPostid(postinfoEntity.getPostid());
//		codehtmlEntity.setSrc(codeSubHtml.getSrc());
//		codehtmlEntity = codeRepos.saveAndFlush(codehtmlEntity);
//		
//		// CSSソースコードを登録
//		codecssEntity.setCodegenre(Constants.CODE_TYPE_CSS);
//		codecssEntity.setPostid(postinfoEntity.getPostid());
//		codecssEntity.setSrc(codeSubCss.getSrc());
//		codecssEntity = codeRepos.saveAndFlush(codecssEntity);
//		
//		// JSソースコードを登録
//		codejsEntity.setCodegenre(Constants.CODE_TYPE_JS);
//		codejsEntity.setPostid(postinfoEntity.getPostid());
//		codejsEntity.setSrc(codeSubJs.getSrc());
//		codejsEntity = codeRepos.saveAndFlush(codejsEntity);
//		
//		// HTMLファイル情報を登録
//		filehtmlEntity.setFilename(fileSubHtml.getFilename());
//		filehtmlEntity.setFilegenre(Constants.FILE_TYPE_HTML);
//		filehtmlEntity.setPostid(postinfoEntity.getPostid());
//		filehtmlEntity = fileRepos.saveAndFlush(filehtmlEntity);
//		
//		// ZIPファイル情報を登録
//		filezipEntity.setFilename(fileSubZip.getFilename());
//		filezipEntity.setFilegenre(Constants.FILE_TYPE_ZIP);
//		filezipEntity.setPostid(postinfoEntity.getPostid());
//		filezipEntity = fileRepos.saveAndFlush(filezipEntity);
//				
//		List<String> codeList = new ArrayList<>();
//		codeList.add(StringUtil.convertToEscape(codehtmlEntity.getSrc()));
//		codeList.add(StringUtil.convertToEscape(codecssEntity.getSrc()));
//		codeList.add(StringUtil.convertToEscape(codejsEntity.getSrc()));
//		
//		try {
//			// sample{id}.htmlを生成
//			ioService.createHtmlFile(filehtmlEntity.getFilename(), codeList, false);
//			
//			// sample{id}.zipを生成
//			ioService.createZipFile(filezipEntity.getFilename(), filehtmlEntity.getFilename(), false);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "redirect:/";
//		}
//		operationService.deleteOpeForSub(postid);
//		
//		return "comp";
//	}
//
//	
//	/**
//	 * パーツ削除画面コントローラー
//	 * URL：/delete
//	 */
//	@PostMapping("/delete")
//	public String delete(@RequestParam(value="postidForDelete") Integer postid, Model model) throws Exception {
//		
//		operationService.deleteOpeForSub(postid);
//		
//		return "redirect:/";
//	}
//	
//	/**
//	 * 管理者画面コントローラー
//	 * URL：/admin
//	 */
//	@GetMapping("/admin")
//	public String admin(Model model) {
//		
//		return "admin";
//	}
//	
//	/**
//	 * 管理者パーツリスト画面コントローラー
//	 * URL：/listView
//	 */
//	@GetMapping("/listView")
//	public String listView(Model model) throws Exception {
//		
//		// 項目種別一覧取得
//		List<TypedbEntity> typedbList = typedbDao.selectTypedbAll();
//		model.addAttribute("typedbList",typedbList);
//		
//		// 全ソースコード情報を取得
//		Map<Integer,Map<Integer,List<String>>> indexMap = operationService.getSrcAllForManageOpe();
//		model.addAttribute("codeMap", indexMap);
//		
//		return "listView";
//	}
//	
//	/**
//	 * 管理者パーツリスト画面コントローラー
//	 * URL：/admin/deleteView
//	 */
//	@PostMapping("/deleteview")
//	public String deleteview(@RequestParam(value="postidForDelete") Integer postid
//			, @RequestParam(value="typeidForDelete") Integer typeid, Model model) throws Exception {
//		
//		FileinfoEntity fileinfoEntity = fileinfoDao.findFileByPostid(postid,Constants.FILE_TYPE_HTML);
//		model.addAttribute("typename",typedbDao.findTypeByTypeid(typeid).getTypename());
//		model.addAttribute("htmlsrc",codeinfoDao.findCodeByPostid(postid,Constants.CODE_TYPE_HTML));
//		model.addAttribute("csssrc",codeinfoDao.findCodeByPostid(postid,Constants.CODE_TYPE_CSS));
//		model.addAttribute("jssrc",codeinfoDao.findCodeByPostid(postid,Constants.CODE_TYPE_JS));
//		model.addAttribute("postid",postid);
//		
//		return "deleteView";
//	}
//	
//	/**
//	 * 承認一覧画面コントローラー
//	 * URL：/manageList
//	 */
//	@GetMapping("/manageList")
//	public String manageList(Model model) throws Exception {
//		
//		// 項目種別一覧取得
//		List<TypedbEntity> typedbList = typedbDao.selectTypedbAll();
//		model.addAttribute("typedbList",typedbList);
//		
//		// 未承認の投稿一覧を取得 [postdate,typename,postid,typeid]
//		List<Object[]> postDataList = customDao.findPostByStatus();
//		
//		model.addAttribute("postDataList",postDataList);
//		
//		return "manageList";
//	}
//	
//	/**
//	 * 承認詳細画面コントローラー
//	 * URL：/manageDetail
//	 */
//	@PostMapping("/manageDetail")
//	public ModelAndView manageDetail(@RequestParam(value="postidForManage") Integer postid, 
//			@RequestParam(value="typeidForManage") Integer typeid,ModelAndView  mav) {
//		
//		FileinfoEntity fileinfoEntity = fileinfoDao.findFileByPostid(postid,Constants.FILE_TYPE_HTML);
//		mav.addObject("typename",typedbDao.findTypeByTypeid(typeid).getTypename());
//		mav.addObject("htmlsrc",codeinfoDao.findCodeByPostid(postid,Constants.CODE_TYPE_HTML));
//		mav.addObject("csssrc",codeinfoDao.findCodeByPostid(postid,Constants.CODE_TYPE_CSS));
//		mav.addObject("jssrc",codeinfoDao.findCodeByPostid(postid,Constants.CODE_TYPE_JS));
//		mav.addObject("htmllink",dirname + "/src/" + fileinfoEntity.getFilename());
//		mav.addObject("postid",postid);
//		mav.setViewName("manageDetail");
//		return mav;
//	}
//	
//	@PostMapping("/regist")
//	public String postRegist(@RequestParam(value="postidForRegist") Integer postid, Model model) throws Exception {
//		PostinfoEntity postinfoEntity = postinfoDao.findPostByPostid(postid);
//		postinfoEntity.setStatus(2);
//		postRepos.saveAndFlush(postinfoEntity);
//		
//		return "redirect:/manageList";
//		
//	}
//	@PostMapping("/filter")
//	public String postFilter(@RequestParam(value="item_type") int typeid, Model model) throws Exception {
//		
//		// 項目種別一覧取得
//		List<TypedbEntity> typedbList = typedbDao.selectTypedbAll();
//		model.addAttribute("typedbList",typedbList);
//		
//		List<Object[]> postDataList = new ArrayList();
//		// すべてが選択されている場合
//		if(typeid==0) {
//			// 未承認の投稿一覧を取得 [postdate,typename,postid,typeid]
//			postDataList = customDao.findPostByStatus();
//		} else {
//			// 指定したパーツ種別の未承認の投稿一覧を取得 [postdate,typename,postid,typeid]
//			postDataList = customDao.findPostByStatusFilter(typeid);			
//		}
//		
//		model.addAttribute("postDataList",postDataList);
//		
//		return "manageList";
//		
//	}
//	
//	/**
//	 * 承認詳細画面コントローラー
//	 * URL：/manageDetail
//	 */
//	@PostMapping("/reject")
//	public String reject(@RequestParam(value="postidForDelete") int postid, Model model) throws Exception {
//		// 投稿IDに紐づく情報をすべて削除
//		operationService.deleteOpe(postid);
//		
//		// 項目種別一覧取得
//		List<TypedbEntity> typedbList = typedbDao.selectTypedbAll();
//		model.addAttribute("typedbList",typedbList);
//		
//		// 未承認の投稿一覧を取得 [postdate,typename,postid,typeid]
//		List<Object[]> postDataList = customDao.findPostByStatus();
//		
//		model.addAttribute("postDataList",postDataList);
//		return "manageList";
//	}
//	
//	/**
//	 * 管理画面削除コントローラー
//	 * URL：/manageDetail
//	 */
//	@PostMapping("/manageDelete")
//	public String manageDelete(@RequestParam(value="postidForDelete") int postid, Model model) throws Exception {
//		// 投稿IDに紐づく情報をすべて削除
//		operationService.deleteOpe(postid);
//		
//		// 項目種別一覧取得
//		List<TypedbEntity> typedbList = typedbDao.selectTypedbAll();
//		model.addAttribute("typedbList",typedbList);
//		
//		// 全ソースコード情報を取得
//		Map<Integer,Map<Integer,List<String>>> indexMap = operationService.getSrcAllForManageOpe();
//		model.addAttribute("codeMap", indexMap);
//		
//		return "redirect:/listView";
//	}
//	

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
