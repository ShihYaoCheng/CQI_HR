package com.cqi.hr.util;

import org.apache.log4j.Logger;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.vo.LineMessageVo;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.container.Bubble.BubbleBuilder;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;


public class LineUtils {

	protected static Logger logger = Logger.getLogger(LineUtils.class.getClass());

	public static void postToLine(String msg, String group) {
		try {
			final LineMessagingClient client = LineMessagingClient.builder(Constant.LINE_CHANNEL_ACCESS_TOKEN).build();
			Message message = new TextMessage(msg);
			PushMessage pushMessage = new PushMessage(group, message);
			BotApiResponse apiResponse = client.pushMessage(pushMessage).get();
			logger.info("TestGOgogogrgergrg : " + apiResponse.getMessage());
		} catch (Exception e) {
			logger.error("postToLine Exception : ", e);
		}
	}
	
	public static void sendFlexMessage(LineMessageVo lineMessageVo) {
		try {
			final LineMessagingClient client = LineMessagingClient.builder(Constant.LINE_CHANNEL_ACCESS_TOKEN).build();
			BubbleBuilder messageBuuble = Bubble.builder();
			if(null!=lineMessageVo.getHeader()) {
				messageBuuble.header(lineMessageVo.getHeader());
			}
			if(null!=lineMessageVo.getBody()) {
				messageBuuble.body(lineMessageVo.getBody());
			}
			if(null!=lineMessageVo.getFooter()) {
				messageBuuble.footer(lineMessageVo.getFooter());
			}
			Message messages = new FlexMessage(lineMessageVo.getAltText(), messageBuuble.build());
			PushMessage pushMessage = new PushMessage(lineMessageVo.getTargetId(), messages);
			BotApiResponse apiResponse = client.pushMessage(pushMessage).get();
			logger.info("sendFlexMessage : " + apiResponse.getMessage() + ";targetId: " +lineMessageVo.getTargetId());
		} catch (Exception e) {
			logger.error("sendFlexMessage Exception : ", e);
		}
	}
	
	public static void sendReplyMessage(String replyToken, String msg) {
		try {
			final LineMessagingClient client = LineMessagingClient.builder(Constant.LINE_CHANNEL_ACCESS_TOKEN).build();
			Message message = new TextMessage(msg);
			ReplyMessage replyMessage = new ReplyMessage(replyToken, message);
			BotApiResponse apiResponse = client.replyMessage(replyMessage).get();
			logger.info("TestGOgogogrgergrg : " + apiResponse.getMessage());
		} catch (Exception e) {
			logger.error("sendReplyMessage : ", e);
		}
	}
	
	public static void sendReplyMessage(String replyToken, LineMessageVo lineMessageVo) {
		try {
			final LineMessagingClient client = LineMessagingClient.builder(Constant.LINE_CHANNEL_ACCESS_TOKEN).build();
			BubbleBuilder messageBuuble = Bubble.builder();
			if(null!=lineMessageVo.getHeader()) {
				messageBuuble.header(lineMessageVo.getHeader());
			}
			if(null!=lineMessageVo.getBody()) {
				messageBuuble.body(lineMessageVo.getBody());
			}
			if(null!=lineMessageVo.getFooter()) {
				messageBuuble.footer(lineMessageVo.getFooter());
			}
			Message messages = new FlexMessage(lineMessageVo.getAltText(), messageBuuble.build());
			ReplyMessage replyMessage = new ReplyMessage(replyToken, messages);
			BotApiResponse apiResponse = client.replyMessage(replyMessage).get();
			logger.info("TestGOgogogrgergrg : " + apiResponse.getMessage());
		} catch (Exception e) {
			logger.error("sendReplyMessage : ", e);
		}
	}
	
	public static UserProfileResponse getProfile(String userId) {
		final LineMessagingClient client = LineMessagingClient.builder(Constant.LINE_CHANNEL_ACCESS_TOKEN).build();
		final UserProfileResponse userProfileResponse;
		try {
		    userProfileResponse = client.getProfile(userId).get();
		    return userProfileResponse;
		} catch (Exception e) {
		    logger.error("getProfile Exception : ", e);
		    return null;
		}
	}
}
