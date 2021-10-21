package com.front.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.front.util.Constants;


/**
 * IOに関する機能提供
 */
@Service
public class IoService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * HTMLファイルを作成する
	 * @param filename 作成ファイル名
	 * @param srcList HTML,CSSのファイルソースリスト
	 * @throws Exception ファイル操作に失敗した場合
	 */
	public void createHtmlFile(String filename, List<String> srcList, boolean subcheck) throws Exception {
		// sample{id}.htmlの内容
		String srccode = "<!doctype html>\r\n";
		       srccode+= "<html lang=\"ja\">\r\n";
		       srccode+= "<head>\r\n";
		       srccode+= "<meta charset=\"utf-8\">\r\n";
		       srccode+= "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n";
		       srccode+= "<title>sample</title>\r\n";
		       srccode+= "<style>\r\n";
		       srccode+= srcList.get(1);
		       srccode+= "</style>\r\n";
		       srccode+= "</head>\r\n";
		       srccode+= "<body>\r\n";
		       srccode+= srcList.get(0);
		       srccode+= "</body>\r\n";
		       srccode+= "</html>";
		       
		try {
			File htmlFile = null;
			if(subcheck == true) {
				htmlFile = new File(Constants.SUBFILE_PATH_HTML + filename); 				
			} else {
				htmlFile = new File(Constants.FILE_PATH_HTML + filename); 				
			}
			
			// ファイル生成
			htmlFile.createNewFile();
			
	        try(FileWriter fw = new FileWriter(htmlFile)) {
	            fw.write(srccode);
	            fw.flush();
	        } catch(IOException e) {
	            throw e;
	        }
		} catch(IOException e) {
			throw e;
		}

	}
	
	/**
	 * ZIPファイルを作成する
	 * @param zipName作成するZIPファイル名
	 * @param htmlName ZIP化対象ファイル
	 * @throws Exception ファイル操作に失敗した場合
	 */
	public void createZipFile(String zipName, String htmlName, boolean subCheck) throws Exception {

		try {
			File zipFile = null; 
			if(subCheck == true) {
				zipFile = new File(Constants.SUBFILE_PATH_ZIP + zipName);
			} else {
				zipFile = new File(Constants.FILE_PATH_ZIP + zipName);
			}
	        
			try( // 作成するzipファイル用の出力ストリーム 
			     ZipOutputStream zostr = new ZipOutputStream(new FileOutputStream(zipFile));) {

				File targetFile = null;
				// 圧縮対象設定
				if(subCheck == true) {
					targetFile = new File(Constants.SUBFILE_PATH_HTML + htmlName);
				} else {
					targetFile = new File(Constants.FILE_PATH_HTML + htmlName);
				}
		        
		        // ファイルの圧縮を行う 
		        compressFile(zostr, targetFile);
			}  catch (Exception e) {
				throw e;
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * ZIP圧縮処理
	 * @param zostr ZIPストリーム
	 * @param file ファイル名
	 * @throws Exception ファイル操作に失敗した場合
	 */
	private void compressFile( ZipOutputStream zostr , File file) throws Exception {
	        
	        byte[] buf = new byte[1024]; 
	        int len;
	        try {
		        if (file.isDirectory()) {
		            // 対象がディレクトリの場合 
		            // 作成するzipファイルにディレクトリのエントリの設定を行う 
		            zostr.putNextEntry(new ZipEntry(file.getPath() + "/"));
		            
		            // ディレクトリ内のファイル一覧を取得する 
		            File[] childFileList = file.listFiles();
		            
		            for (File childFile: childFileList) {
		                
		                //ディレクトリ内のファイルにて再帰呼び出しする
		                compressFile(zostr, childFile);
		            }
		            
		        } else {
		            // 対象がファイルの場合
		            // 作成するzipファイルにエントリの設定を行う
		            zostr.putNextEntry(new ZipEntry(file.getPath()));
		            
		            // 圧縮するファイル用の入力ストリーム 
		            BufferedInputStream bistr = new BufferedInputStream(
		                new FileInputStream(file));
		                
		            // 圧縮するファイルを読み込みながら、
		            //zipファイル用の出力ストリームへ書き込みをする
		            while ((len = bistr.read(buf, 0, buf.length)) != -1) {
		                zostr.write(buf, 0, len);
		            }
		                
		            // 圧縮するファイル用の入力ストリームを閉じる 
		            bistr.close();
		            
		            // エントリを閉じる 
		            zostr.closeEntry();
		        }
	        } catch (Exception e) {
	        	throw e;
	        }
	    }
	
	/**
	 * ファイル削除
	 * @param filename 削除対象ファイル
	 * @throws Exception ファイル操作に失敗した場合
	 */
	public void deleteFile(String filename, boolean subCheck) throws Exception {
		
		try {
			// HTMLの場合
			if(filename.contains(Constants.FILE_EXTENSION_HTML)) {
				File htmlFile = null;
				if(subCheck == true) {
					htmlFile = new File(Constants.SUBFILE_PATH_HTML + filename);
				} else {
					htmlFile = new File(Constants.FILE_PATH_HTML + filename);
				}
				// ファイル削除
				htmlFile.delete();
			} else if(filename.contains(Constants.FILE_EXTENSION_ZIP)) {
			// ZIPの場合
				File zipFile = null;
				if(subCheck == true) {
					zipFile = new File(Constants.SUBFILE_PATH_ZIP + filename);	
				} else {
					zipFile = new File(Constants.FILE_PATH_ZIP + filename);					
				}
				// ファイル削除
				zipFile.delete();
			}
		} catch(SecurityException  e) {
			throw e;
		}

	}

}
