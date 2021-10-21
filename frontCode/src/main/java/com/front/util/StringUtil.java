package com.front.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class StringUtil {

	/** 半角数字のパターン */
	static final String HALF_WIDTH_DIGIT_PATTERN = "^[0-9]+$";
	
	/** コンストラクタ */
	private StringUtil(){}
	
	
	public static String convertToEscape(String src) {
		String escText = src;
		escText = escText.replace("&", "&amp;");
		escText = src.replace("\"", "&quot;");
		escText = escText.replace("<", "&lt;");
		escText = escText.replace(">", "&gt;");
		return escText;
	}
	
	public static String convertToNoEscape(String escText) {
		String noEscText = escText;
		noEscText = noEscText.replace("&amp;","&");
		noEscText = noEscText.replace("&quot;","\"");
		noEscText = noEscText.replace("&lt;","<");
		noEscText = noEscText.replace("&gt;",">");
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
			System.out.println(Integer.parseInt(value));
			return true;
		} catch (NumberFormatException e) {
			System.out.println("NG");
			return false;
		}
	}


}
