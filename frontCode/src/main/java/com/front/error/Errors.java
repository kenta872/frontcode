package com.front.error;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Errors {

	@Autowired
	ErrorMessage errorMessage;
	
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // 例外
    ///////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 不正URLアクセス時に例外を発生させる
	 * @return 不正URLアクセス例外
	 */
    public ApplicationException errorUrl() {
    	return new ApplicationException(errorMessage.ERROR_EXCEPTION_URL);
    }
    
    /**
     * 不正操作時の例外を発生させる
     * @return 不正操作例外
     */
    public ApplicationException errorIllegal() {
    	return new ApplicationException(errorMessage.ERROR_EXCEPTION_ILLEGAL);
    }
    
    /**
     * データベースに不整合が発生したときに例外を発生させる
     * @return データベース不整合例外
     */
    public ApplicationException errorDbIllegal() {
    	return new ApplicationException(errorMessage.ERROR_EXCEPTION_DBILLEGAL);
    }
    
    /**
     * 権限がないページにアクセスしたときに例外を発生させる
     * @return 権限例外
     */
    public ApplicationException errorRole() {
    	return new ApplicationException(errorMessage.ERROR_EXCEPTION_ROLEAUTHO);
    }
    
    /**
     * ファイルダウンロードが失敗したときに例外を発生させる
     * @return ダウンロード例外
     */
    public ApplicationException errorDownload() {
    	return new ApplicationException(errorMessage.ERROR_EXCEPTION_DOWNLOAD);
    }
	
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // エラーメッセージ
    ///////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 入力フォームに「http」が含まれている場合にエラーを出力する
     * @return 不正入力エラーメッセージ
     */
    public String notHttp() {
    	return errorMessage.ERROR_MESSAGE_NOTHTTP;
    }
	
    /**
     * 入力フォームにスクリプトタグが含まれている場合にエラーを出力する
     * @return 入力フォームスクリプトエラー
     */
    public String notHtmlScript() {
    	return errorMessage.ERROR_MESSAGE_NOTHTMLSCRIPT;
    }
    
    /**
     * 入力フォームにスクリプトタグが含まれている場合にエラーを出力する
     * @return 入力フォームスクリプトエラー
     */
    public String notCssScript() {
    	return errorMessage.ERROR_MESSAGE_NOTCSSSCRIPT;
    }
    
    
    public String userDuplicate() {
    	return errorMessage.ERROR_MESSAGE_USER_DUPLICATE;
    }
    
    public String mailDuplicate() {
    	return errorMessage.ERROR_MESSAGE_MAIL_DUPLICATE;
    }

}
