package com.front.controller;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadForm {
	
	@NotEmpty(message = "パーツ種別を選択してください")
	String typeSelectValue;
	
	@NotBlank(message = "HTMLを入力してください")
	String htmlInputText;
	
	@NotBlank(message="CSSを入力してください")
	String cssInputText;
	
	String jsInputText;

}
