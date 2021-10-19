package com.front.error;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.front.controller.UploadForm;
import com.front.util.Constants;

public class ErrorCheck {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static List<String> srcCheckHtml(UploadForm uploadForm, List<String> errorList) {
		// aタグURLエラー
		if(uploadForm.getHtmlInputText().contains(Constants.PROTOCOL_HTTP)) {
			errorList.add(ErrorMessage.ERROR_MESSAGE_E10001);
		}
		
		return errorList;
	}


}
