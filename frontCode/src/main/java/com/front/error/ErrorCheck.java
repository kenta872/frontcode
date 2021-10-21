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
	
	public List<String> srcCheckHtml(UploadForm uploadForm, List<String> errorList) {
		// aタグURLエラー
		if(uploadForm.getHtmlInputText().contains(PROTOCOL_HTTP)) {
			errorList.add(errors.notHttp());
		}
		return errorList;
	}
	
	public List<String> srcCheckScript(UploadForm uploadForm, List<String> errorList) {
		
		// scriptチェック
		if(uploadForm.getHtmlInputText().contains(SCRIPT_TAG)) {
			errorList.add(errors.notHtmlScript());
		}
		if(uploadForm.getCssInputText().contains(SCRIPT_TAG)) {
			errorList.add(errors.notCssScript());
		}
		return errorList;
	}


}
