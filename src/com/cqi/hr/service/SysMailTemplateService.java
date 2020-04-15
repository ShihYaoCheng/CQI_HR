package com.cqi.hr.service;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.cqi.hr.config.MailConfig;

@Service
public class SysMailTemplateService {
	protected Logger logger = Logger.getLogger(this.getClass());
	@Resource private JavaMailSender mailSender;
	@Resource MessageSource messageSource;
	
	public JavaMailSender getMailSender() {
		return mailSender;
	}
	
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Resource MailConfig mailConfig;
	/**寄出EMAIL*/
	public boolean sendMail(String subject,String content,String fromMail,String targeMail)  {
		logger.info("sendMail to add:" + targeMail);
		try {
			final MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelp = new MimeMessageHelper(message,true,"UTF-8");
			try{
				String sender = "警示通知系統";
				messageHelp.setFrom(new InternetAddress(fromMail, sender));
			}catch(Exception e){
				messageHelp.setFrom(fromMail);
			}
			messageHelp.setTo(targeMail);
			messageHelp.setSubject(subject);
			//套版
			StringBuilder sb = new StringBuilder();
			sb.append(content);
			messageHelp.setText(sb.toString(),true);
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						mailSender.send(message);
					} catch (Exception e) {
						logger.error("SysMailTemplateService sendMail Thread error", e);
					}
				}
			}).start();
			
			return true;
		} catch (MessagingException e) {
			logger.error("googleSendMailServer e : " ,e);
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean googleSendMailServer(String subject,String content,String targeMail){
		logger.info("googleSendMailServer");
		try {
			MimeMessage message = mailSender.createMimeMessage();
			logger.info("send to: " + targeMail);
			MimeMessageHelper messageHelp = new MimeMessageHelper(message,true,"UTF-8");
			String sender = "Manga Chat";
			messageHelp.setFrom(new InternetAddress(mailConfig.getMailFrom(), sender));
			messageHelp.setTo(targeMail);
			messageHelp.setSubject(subject);
			
			StringBuilder sb = new StringBuilder();
			
			sb.append("<html>");
			sb.append("<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
			sb.append("</head>");
			sb.append("<body style=\"background-color:#F0F0F0\">");
			sb.append("<div style=\"background-color:#fff;width:550px;margin: 0px auto;box-shadow: 1px 1px 5px #999999;border: 1px solid #CCCCCC; padding-bottom:5px;\">");
			sb.append("<div style=\"background-color:#FFD800;text-align:center; padding-top:7px;\">");
			sb.append("<img style=\"width:30px;\" src=\"http://203.64.84.29:8080/resources/img/chat_bubble.png\"/>");
			sb.append("<span style=\"color: #333333; font-size:30px; font-weight:bold;\">Manga Chat</span>");
			sb.append("</div>");
			sb.append("<div style=\"text-align:center; line-height:24px;color: #3b3b3b; font-size:18px; margin-left:20px; padding-top:10px; padding-left:10px; \">");
			sb.append(content);
			sb.append("</div>");
			sb.append("<div style=\"text-align:center;\"><span style=\"font-size:10px; color:#999999; text-align:center;\">");
			sb.append("Manga Chat Co., Ltd.");
			sb.append("</span>");
			sb.append("</div>");
			sb.append("</div>");
			sb.append("</body>");
			sb.append("</html>");
			messageHelp.setText(sb.toString(),true);
			mailSender.send(message);
			return true;
		} catch (MessagingException e) {
			logger.error("googleSendMailServer e : " ,e);
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}
