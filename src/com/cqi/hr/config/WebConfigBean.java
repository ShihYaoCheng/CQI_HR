package com.cqi.hr.config;


public class WebConfigBean {
	
	/** export CSV檔 文字編碼*/
	private String exportCsvCharset ="UTF-8";
	private String logoPath = "/resources/img/index_03.png";
	private String loginLogoPath = "/resources/img/index_03.png";
	/** 檔案圖片上傳路徑 **/
	private String uploadPath = "C:/temp";
	/** 圖片URL **/
	private String picPath = "/image";
	/** CronJob是否執行中 **/
	private boolean execute = false;
	
	public String getExportCsvCharset() {
		return exportCsvCharset;
	}
	public String getLogoPath() {
		return logoPath;
	}
	public String getLoginLogoPath() {
		return loginLogoPath;
	}
	
	public void setExportCsvCharset(String exportCsvCharset) {
		this.exportCsvCharset = exportCsvCharset;
	}
	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}
	public void setLoginLogoPath(String loginLogoPath) {
		this.loginLogoPath = loginLogoPath;
	}
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public String getUploadPath() {
		return uploadPath;
	}
	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}
	public boolean getExecute() {
		return execute;
	}
	public void setExecute(boolean execute) {
		this.execute = execute;
	}

	
}
