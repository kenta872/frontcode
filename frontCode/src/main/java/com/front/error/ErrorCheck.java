package com.front.error;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.front.controller.UploadForm;

@Component
public class ErrorCheck {

	@Autowired
	Errors errors;

	/** エラーチェック用：HTTP */
	public static final String PROTOCOL_HTTP = "http";

	/** エラーチェック用：script */
	public static final String SCRIPT_TAG = "script";

	// ロギング
	Logger logger = LoggerFactory.getLogger(this.getClass());

	
	
	/**
	 * htmlアップロード情報に"http"情報が含まれているかをチェック
	 * @param uploadForm アップロード情報
	 * @param errorList エラー一覧
	 * @return エラー一覧
	 */
	public List<String> srcCheckHtml(UploadForm uploadForm, List<String> errorList) {
		if (uploadForm.getHtmlInputText().contains(PROTOCOL_HTTP)) {
			errorList.add(errors.notHttp());
		}
		return errorList;
	}

	
	/**
	 * スクリプトタグチェック
	 * @param uploadForm アップロード情報
	 * @param errorList エラー一覧
	 * @return エラー一覧
	 */
	public List<String> srcCheckScript(UploadForm uploadForm, List<String> errorList) {

		// htmlをチェック
		if (uploadForm.getHtmlInputText().contains(SCRIPT_TAG)) {
			errorList.add(errors.notHtmlScript());
		}
		// cssをチェ区
		if (uploadForm.getCssInputText().contains(SCRIPT_TAG)) {
			errorList.add(errors.notCssScript());
		}
		return errorList;
	}

}
