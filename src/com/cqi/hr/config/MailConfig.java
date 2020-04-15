package com.cqi.hr.config;


public class MailConfig {
	private String uploadFileRootFolder = "/";
	private String downloadFileRootPath = "/";
	private String skipQuartzFolder = "/";
	private String doQuartzIp = "10.100.2.2";
	/** mail server */
	private String mailServer = "msa.hinet.net";

	/** mail server's port */
	private String mailServerPort = "25";

	/** mail protocol */
	private String mailTransportProtocol = "smtp";
	
	/** mail 寄件者 */
	private String mailFrom = "homehealth@ebti.com.tw";
	
	/** mail 預設郵件標題 */
	private String defaultSubject = "Notification";
	
	/** mail 郵件內預設連結網址*/
	private String linkAddress = "";

	public String getUploadFileRootFolder() {
		return uploadFileRootFolder;
	}

	public String getDownloadFileRootPath() {
		return downloadFileRootPath;
	}

	public String getSkipQuartzFolder() {
		return skipQuartzFolder;
	}

	public String getDoQuartzIp() {
		return doQuartzIp;
	}

	public String getMailServer() {
		return mailServer;
	}

	public String getMailServerPort() {
		return mailServerPort;
	}

	public String getMailTransportProtocol() {
		return mailTransportProtocol;
	}

	public String getMailFrom() {
		return mailFrom;
	}

	public String getDefaultSubject() {
		return defaultSubject;
	}

	public String getLinkAddress() {
		return linkAddress;
	}

	public void setUploadFileRootFolder(String uploadFileRootFolder) {
		this.uploadFileRootFolder = uploadFileRootFolder;
	}

	public void setDownloadFileRootPath(String downloadFileRootPath) {
		this.downloadFileRootPath = downloadFileRootPath;
	}

	public void setSkipQuartzFolder(String skipQuartzFolder) {
		this.skipQuartzFolder = skipQuartzFolder;
	}

	public void setDoQuartzIp(String doQuartzIp) {
		this.doQuartzIp = doQuartzIp;
	}

	public void setMailServer(String mailServer) {
		this.mailServer = mailServer;
	}

	public void setMailServerPort(String mailServerPort) {
		this.mailServerPort = mailServerPort;
	}

	public void setMailTransportProtocol(String mailTransportProtocol) {
		this.mailTransportProtocol = mailTransportProtocol;
	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	public void setDefaultSubject(String defaultSubject) {
		this.defaultSubject = defaultSubject;
	}

	public void setLinkAddress(String linkAddress) {
		this.linkAddress = linkAddress;
	}

	
	
}
