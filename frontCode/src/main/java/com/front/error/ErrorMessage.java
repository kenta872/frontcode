package com.front.error;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:errormessage_ja.properties", encoding="UTF-8")
public class ErrorMessage {
	
	// 例外
	@Value("${error.exception.noturl}")
    public String ERROR_EXCEPTION_URL;
	@Value("${error.exception.illegal}")
    public String ERROR_EXCEPTION_ILLEGAL;
	@Value("${error.exception.dberror}")
    public String ERROR_EXCEPTION_DBILLEGAL;
	@Value("${error.exception.loginerror}")
	public String ERROR_EXCEPTION_ROLEAUTHO;
    
	// エラーメッセージ
    @Value("${error.message.nothttp}")
    public String ERROR_MESSAGE_NOTHTTP;
    
    @Value("${error.message.htmlscript}")
    public String ERROR_MESSAGE_NOTHTMLSCRIPT;
    
    @Value("${error.message.cssscript}")
    public String ERROR_MESSAGE_NOTCSSSCRIPT;
    
    @Value("${error.message.userduplicate}")
    public String ERROR_MESSAGE_USER_DUPLICATE;
    
    @Value("${error.message.mailduplicate}")
    public String ERROR_MESSAGE_MAIL_DUPLICATE;

}
