package com.front.error;

public class ApplicationException extends RuntimeException {
	
	/** バージョン管理 */
	private static final long serialVersionUID = 1L;

	/** エラーメッセージ */
	private final String errorMessage;

	/** コンストラクタ */
	public ApplicationException(String msg) {
		super(msg);
		this.errorMessage = msg;
	}

	/**
	 * エラーメッセージを取得
	 * @return エラーメッセージ
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

}
