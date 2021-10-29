package com.front.util;

import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

/**
 * 定義クラス
 */
public class Constants {

	/** ZIPファイルの保存先パス */
	public static final String FILE_PATH_ZIP = "src\\main\\resources\\static\\sampleFile\\zip\\";

	/** HTMLファイルの保存先パス */
	public static final String FILE_PATH_HTML = "src\\main\\resources\\static\\sampleFile\\src\\";

	/** ZIPファイルの保存先パス */
	public static final String SUBFILE_PATH_ZIP = "src\\main\\resources\\static\\sampleFile\\subzip\\";

	/** HTMLファイルの保存先パス */
	public static final String SUBFILE_PATH_HTML = "src\\main\\resources\\static\\sampleFile\\subsrc\\";

	/** 拡張子：ZIP */
	public static final String FILE_EXTENSION_ZIP = ".zip";

	/** 拡張子：HTML */
	public static final String FILE_EXTENSION_HTML = ".html";

	/** セキュリティ対象外ファイルリスト */
	public static final String[] STATIC_RESOURCES = { "/**/css/**", "/**/js/**", "/**/images/**" };

	/** 日付フォーマット：uuuuMMddHHmm */
	public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("uuuuMMddHHmm")
			.withResolverStyle(ResolverStyle.STRICT);

	/** ソース種別：HTML */
	public static final String CODE_TYPE_HTML = "html";

	/** ソース種別：CSS */
	public static final String CODE_TYPE_CSS = "css";

	/** ファイル種別：HTML */
	public static final String FILE_TYPE_HTML = "html";

	/** ファイル種別：ZIP */
	public static final String FILE_TYPE_ZIP = "zip";

	/** 投稿ステータス：未承認 */
	public static final int POSTINFO_STATUS_MISHONIN = 1;

	/** 投稿ステータス：承認 */
	public static final int POSTIFNO_STATUS_SHONIN = 2;

	/** 空文字 */
	public static final String EMPTY_TEXT = "";

}