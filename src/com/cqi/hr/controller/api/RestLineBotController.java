package com.cqi.hr.controller.api;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.controller.AbstractController;
import com.cqi.hr.entity.LineUser;
import com.cqi.hr.service.LineBotService;
import com.cqi.hr.service.SysUserService;
import com.cqi.hr.service.UserAskForLeaveService;
import com.cqi.hr.service.UserLeaveService;


@RestController
@RequestMapping("/restful/lineBot")
public class RestLineBotController extends AbstractController<LineUser> {	
	@Resource SysUserService sysUserService;
	@Resource UserLeaveService userLeaveService;
	@Resource UserAskForLeaveService userAskForLeaveService;
	@Resource LineBotService lineBotService;
	private static String FUNCTION_NAME = "Resful Line Bot";
	
	@RequestMapping(method=RequestMethod.POST, value="webhook")
	public void ajaxPostWebhook(HttpServletRequest req, HttpServletResponse resp, @RequestBody String requestData){
		logger.info(FUNCTION_NAME + " ajaxPostWebhook");
		try{
			String channelSecret = Constant.LINE_CHANNEL_SECRET; // Channel secret string
			String httpRequestBody = requestData; // Request body string
			SecretKeySpec key = new SecretKeySpec(channelSecret.getBytes(), "HmacSHA256");
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(key);
			byte[] source = httpRequestBody.getBytes("UTF-8");
			String signature = Base64.encodeBase64String(mac.doFinal(source));
			logger.info("signature : " + signature);
			// Compare X-Line-Signature request header string and the signature
			logger.info("X-Line-Signature : " + req.getHeader("X-Line-Signature"));
			
			logger.info("data : " + requestData);
			if(signature.equals(req.getHeader("X-Line-Signature"))) {
				lineBotService.webhook(requestData);
			}
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxPostWebhook error: ", e);
		}
	}
}
