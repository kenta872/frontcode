package com.front.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class StringUtil {

	/** 半角数字のパターン */
	static final String HALF_WIDTH_DIGIT_PATTERN = "^[0-9]+$";
	
	/** コンストラクタ */
	private StringUtil(){}
	
	@Autowired
	FileIo ioService;
	
	
	public static String convertToEscape(String src) {
		String escText = src;
		escText = escText.replace("&", "&amp;");
		escText = src.replace("\"", "&quot;");
		return escText;
	}
	
	public static String convertToNoEscape(String escText) {
		String noEscText = escText;
		noEscText = noEscText.replace("&amp;","&");
		noEscText = noEscText.replace("&quot;","\"");
		return noEscText;
	}
	
	

	/**
	 * 有効な数値かどうかの判定<br>
	 * （半角数字、かつIntegerに変換可能）
	 * @param value チェック対象値
	 * @return 有効な数値の場合true、null、空文字、その他はfalse
	 */
	public static boolean isValidNumber(String value) {
		
	    Pattern numberPatter = Pattern.compile(HALF_WIDTH_DIGIT_PATTERN); // 正規表現パターンの読み込み
	    Matcher m1 = numberPatter.matcher(value); // パターンと検査対象文字列の照合
	    boolean result1 = m1.matches(); // 照合結果をtrueまたはfalseで取得する
		if(StringUtils.isEmpty(value) || !result1) {
			// null、空文字、または半角数字でない場合
			return false;
		}
		try {
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * Iframeデータを作成する
	 * @param htmlCode htmlソース
	 * @param CssCode cssソース
	 * @return iframeデータ
	 */
	public static String createIframe(String htmlCode,String cssCode) {
		String iframeCode = "<!doctype html>";
		iframeCode+= "<html lang=\"ja\">";
		iframeCode+= "<head>";
		iframeCode+= "<meta charset=\"utf-8\">";
		iframeCode+= "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">";
		iframeCode+= "<title>sample</title>";
		iframeCode+= "<style>";
		iframeCode+= cssCode;
		iframeCode+= "</style>";
		iframeCode+= "</head>";
		iframeCode+= "<body>";
		iframeCode+= htmlCode;
		iframeCode+= "</body>";
		iframeCode+= "</html>";
	       
	    return iframeCode;
	}
	
	/**
	 * サンプルhtml用のソース作成
	 * @param htmlCode
	 * @param cssCode
	 * @return
	 */
	public static String createSampleHtml(String htmlCode,String cssCode) {
		
		String outputCode = "<!doctype html>";
		outputCode+= "<html lang=\"ja\">";
		outputCode+= "<head>";
		outputCode+= "<meta charset=\"utf-8\">";
		outputCode+= "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">";
		outputCode+= "<title>sample</title>";
		outputCode+= "<style>";
		outputCode+= cssCode;
		outputCode+= "</style>";
		outputCode+= "</head>";
		outputCode+= "<body>";
		outputCode+= htmlCode;
		outputCode+= "</body>";
		outputCode+= "</html>";
		
		return outputCode;
	}

}
