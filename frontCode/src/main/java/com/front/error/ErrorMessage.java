package com.front.error;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:errormessage_ja.properties", encoding="UTF-8")
public class ErrorMessage {
	
	/*
	 * 例外
	 */
	
	// 不正URL例外
	@Value("${error.exception.noturl}")
    public String ERROR_EXCEPTION_URL;
	// 不正操作例外
	@Value("${error.exception.illegal}")
    public String ERROR_EXCEPTION_ILLEGAL;
	// DB例外
	@Value("${error.exception.dberror}")
    public String ERROR_EXCEPTION_DBILLEGAL;
	// 閲覧権限例外
	@Value("${error.exception.loginerror}")
	public String ERROR_EXCEPTION_ROLEAUTHO;
	// ダウンロード例外
	@Value("${error.exception.downloaderror}")
	public String ERROR_EXCEPTION_DOWNLOAD;
    
	
	/*
	 * エラー 
	 */
	// httpチェックエラー
    @Value("${error.message.nothttp}")
    public String ERROR_MESSAGE_NOTHTTP;
    // htmlスクリプトチェックエラー
    @Value("${error.message.htmlscript}")
    public String ERROR_MESSAGE_NOTHTMLSCRIPT;
    // cssスクリプトチェックエラー
    @Value("${error.message.cssscript}")
    public String ERROR_MESSAGE_NOTCSSSCRIPT;
    // ユーザーネーム重複エラー
    @Value("${error.message.userduplicate}")
    public String ERROR_MESSAGE_USER_DUPLICATE;
    // メールアドレス重複エラー
    @Value("${error.message.mailduplicate}")
    public String ERROR_MESSAGE_MAIL_DUPLICATE;

}
