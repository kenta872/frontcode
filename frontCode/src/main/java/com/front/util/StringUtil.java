package com.front.util;

public class StringUtil {
	
	/** コンストラクタ */
	public StringUtil() {}
	
	
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

}
