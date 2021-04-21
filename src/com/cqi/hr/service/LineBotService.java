package com.cqi.hr.service;

import java.io.Console;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.dao.AbstractDAO;
import com.cqi.hr.dao.EmergenceOvertimeSignDAO;
import com.cqi.hr.dao.LineImageUrlDAO;
import com.cqi.hr.dao.LineUserDAO;
import com.cqi.hr.dao.SysUserDAO;
import com.cqi.hr.dao.SysUserShiftDAO;
import com.cqi.hr.dao.UserAskForOvertimeDAO;
import com.cqi.hr.entity.AttendanceRecord;
import com.cqi.hr.entity.EmergenceOvertimeSign;
import com.cqi.hr.entity.LineImageUrl;
import com.cqi.hr.entity.LineUser;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.SysUserShift;
import com.cqi.hr.entity.UserAskForOvertime;
import com.cqi.hr.util.DateUtils;
import com.cqi.hr.util.LineUtils;
import com.cqi.hr.util.StringUtils;
import com.cqi.hr.vo.FlexMessageVo;
import com.cqi.hr.vo.LineMessageVo;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.component.Button.ButtonHeight;
import com.linecorp.bot.model.message.flex.component.Button.ButtonStyle;
import com.linecorp.bot.model.message.flex.component.FlexComponent;
import com.linecorp.bot.model.message.flex.component.Image;
import com.linecorp.bot.model.message.flex.component.Image.ImageAspectMode;
import com.linecorp.bot.model.message.flex.component.Image.ImageSize;
import com.linecorp.bot.model.message.flex.component.Separator;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.component.Text.TextWeight;
import com.linecorp.bot.model.message.flex.unit.FlexAlign;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.sun.org.apache.xpath.internal.operations.And;

@Transactional
@Service
public class LineBotService extends AbstractService<LineUser>{

	@Resource LineUserDAO lineUserDAO;
	
	@Resource SysUserDAO sysUserDAO;
	
	@Resource EmergenceOvertimeSignDAO emergenceOvertimeSignDAO;
	
	@Resource LineImageUrlDAO lineImageUrlDAO;
	
	@Resource UserAskForOvertimeDAO userAskForOvertimeDAO;
	
	@Resource AttendanceRecordService attendanceRecordService;
	
	@Resource SysUserShiftDAO sysUserShiftDAO;
	
	@Override
	protected AbstractDAO<LineUser> getDAO() {
		return lineUserDAO;
	}
	
	public void webhook(String webhookData) throws Exception {
		JsonObject  jobject = new Gson().fromJson(webhookData, JsonObject.class);
	    JsonArray jsonArray = jobject.getAsJsonArray("events");
	    if(null!=jsonArray && jsonArray.size()>0) {
	    	for(int i=0;i<jsonArray.size();i++) {
	    		JsonObject eventJson = jsonArray.get(i).getAsJsonObject();
	    		String type = eventJson.get("type").getAsString();
	    		logger.info("type : " + type);
	    		switch (type) {
				case "message":
					getMessageEvent(eventJson);
					break;
				case "follow":
					addFriendEvent(eventJson);
					break;
				case "postback":
					getPostBack(eventJson);
					break;
				default:
					break;
				}
	    	}
	    }
	}
	
	public void addFriendEvent(JsonObject eventJson) throws Exception {
		JsonObject sourceJson = eventJson.getAsJsonObject("source");
		if(sourceJson.get("type").getAsString().equals("user")) {
			UserProfileResponse userProfileResponse = LineUtils.getProfile(sourceJson.get("userId").getAsString());
			if(null!=userProfileResponse) {
				logger.info("getDisplayName : " + userProfileResponse.getDisplayName());
				logger.info("getLanguage : " + userProfileResponse.getLanguage());
				logger.info("getStatusMessage : " + userProfileResponse.getStatusMessage());
				logger.info("getPictureUrl : " + userProfileResponse.getPictureUrl());
				Calendar createDate = Calendar.getInstance();
				createDate.setTimeInMillis(eventJson.get("timestamp").getAsLong());
				lineUserDAO.saveOrUpdate(new LineUser(sourceJson.get("userId").getAsString(), userProfileResponse.getDisplayName(), createDate.getTime(), createDate.getTime(), Constant.STATUS_ENABLE));
			}
		}
	}
	
	public void getMessageEvent(JsonObject eventJson) throws Exception {
		JsonObject sourceJson = eventJson.getAsJsonObject("source");
		UserProfileResponse userProfileResponse = LineUtils.getProfile(sourceJson.get("userId").getAsString());
		if(null!=userProfileResponse) {
			logger.info("getDisplayName : " + userProfileResponse.getDisplayName());
			logger.info("getLanguage : " + userProfileResponse.getLanguage());
			logger.info("getStatusMessage : " + userProfileResponse.getStatusMessage());
			logger.info("getPictureUrl : " + userProfileResponse.getPictureUrl());
		}
		JsonObject messageJson = eventJson.getAsJsonObject("message");
		String messageType = messageJson.get("type").getAsString();
		switch (messageType) {
		case "text":
			logger.info("message : " + messageJson.get("text").getAsString());
			if(messageJson.get("text").getAsString().contains("審核") || messageJson.get("text").getAsString().contains("勤務")) {
				SysUser user = sysUserDAO.getByLineId(sourceJson.get("userId").getAsString());
				if(user.getDepartmentMaster()!=null && user.getDepartmentMaster().equals(Constant.STATUS_ENABLE)) {
					buildListVo(Constant.LINE_EMERGENCE_LEVEL_DEPARTMENT, eventJson.get("replyToken").getAsString());
				}else if(user.getAdministrationManager()!=null && user.getAdministrationManager().equals(Constant.STATUS_ENABLE)) {
					buildListVo(Constant.LINE_EMERGENCE_LEVEL_ADMINISTRATION, eventJson.get("replyToken").getAsString());
				}else if(user.getFinanceMaster()!=null && user.getFinanceMaster().equals(Constant.STATUS_ENABLE)) {
					buildListVo(Constant.LINE_EMERGENCE_LEVEL_FINANCE, eventJson.get("replyToken").getAsString());
				}else if(user.getCompanyGod()!=null && user.getCompanyGod().equals(Constant.STATUS_ENABLE)) {
					buildListVo(Constant.LINE_EMERGENCE_LEVEL_COMPANY, eventJson.get("replyToken").getAsString());
				}
			}else if (messageJson.get("text").getAsString().contains("測試")) {
				replyTestMessage("", eventJson.get("replyToken").getAsString());
			}else if (messageJson.get("text").getAsString().contains("綁定")) {
				//BindLineIdToSysUser(sourceJson.get("userId").getAsString(),messageJson.get("text").getAsString(), eventJson.get("replyToken").getAsString());
			}
			
			break;
		default:
			break;
		}
	}
	
	public void replyTestMessage(String string, String replyToken) throws Exception {
		
		SysUser targetUser = sysUserDAO.get("1198842813042872");


		
		//flexMessageVo.setBody(emergenceListBody(emergenceList));
		//flexMessageVo.setFooter(emergenceListFooter(targetUser.getLineId(), level));
		
		
		logger.info("replyToken : " + replyToken);
		LineMessageVo lineMessageVo = new LineMessageVo();
		lineMessageVo.setTargetId(targetUser.getLineId());
		lineMessageVo.setAltText("TestAltText");
		lineMessageVo.setHeader(testHeader());
		//lineMessageVo.setBody(dataVo.getBody());
		//lineMessageVo.setFooter(dataVo.getFooter());
		if(StringUtils.hasText(replyToken)) {
			LineUtils.sendReplyMessage(replyToken, lineMessageVo);
		}else {
			LineUtils.sendFlexMessage(lineMessageVo);
		}
	}

	private Box testHeader() {
		List<FlexComponent> headerContents = new ArrayList<FlexComponent>();
		headerContents.add(Text.builder().text("測試").size(FlexFontSize.LG).color("#2FC032").weight(TextWeight.BOLD).align(FlexAlign.START).build());
		headerContents.add(Text.builder().text("訊息推送時間：" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())).size(FlexFontSize.XS).align(FlexAlign.START).build());
		headerContents.add(Separator.builder().build());
        
        return Box.builder().layout(FlexLayout.VERTICAL).contents(headerContents).build();
	}

	public void getPostBack(JsonObject eventJson) throws Exception {
		JsonObject sourceJson = eventJson.getAsJsonObject("source");
		SysUser signUser = sysUserDAO.getByLineId(sourceJson.get("userId").getAsString());
		if(null!=signUser) {
			JsonObject postbackJson = eventJson.getAsJsonObject("postback");
			String data = postbackJson.get("data").getAsString();
			String[] params = data.split("&");
		    Map<String, String> map = new HashMap<String, String>();
		    for (String param : params) {
		        String name = param.split("=")[0];
		        String value = param.split("=")[1];
		        map.put(name, value);
		    }
		    EmergenceOvertimeSign signData = emergenceOvertimeSignDAO.getOneByToken(map.get("token"));
		    if(null!=signData) {
		    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		    	Calendar calendar = Calendar.getInstance();
		    	switch (map.get("level")) {
				case Constant.LINE_EMERGENCE_LEVEL_PROJECT:
					if(signData.getProjectSignerId().equals(signUser.getSysUserId()) && null==signData.getProjectSignTime()) {
			    		signData.setProjectSignerLineId(signUser.getLineId());
		    			signData.setProjectSignTime(calendar.getTime());
			    		if(!map.get("response").equals(Constant.LINE_EMERGENCE_CONFIRM)) {
			    			signData.setStatus(Constant.LINE_EMERGENCE_REJECT_BY_PROJECT);
			    		}
			    		signData.setUpdateTime(calendar.getTime());
			    		emergenceOvertimeSignDAO.update(signData);
			    		LineUtils.sendReplyMessage(eventJson.get("replyToken").getAsString(), "系統已收到您的選擇：" + map.get("response"));
			    		if(map.get("response").equals(Constant.LINE_EMERGENCE_CONFIRM)) {
			    			buildListVo(Constant.LINE_EMERGENCE_LEVEL_DEPARTMENT, null);
			    			//buildConfirmVo(signData, Constant.LINE_EMERGENCE_LEVEL_DEPARTMENT);
			    		}else {
			    			//推播給申請者
			    			UserAskForOvertime userAskForOvertime = userAskForOvertimeDAO.get(signData.getAskForOvertimeId());
			    			userAskForOvertime.setStatus(Constant.STATUS_DISABLE);
			    			userAskForOvertimeDAO.update(userAskForOvertime);
			    			SysUser requestUser = sysUserDAO.get(userAskForOvertime.getSysUserId());
			    			if(StringUtils.hasText(requestUser.getLineId())) {
			    				LineUtils.postToLine("您於" + sdf.format(signData.getCreateTime()) + "的申請已遭" + Constant.LINE_EMERGENCE_LEVEL_PROJECT + "退回", requestUser.getLineId());
			    			}
			    		}
			    	}
					break;
//				case Constant.LINE_EMERGENCE_LEVEL_DEPARTMENT:
//					if(signData.getDepartmentSignerId().equals(signUser.getSysUserId()) && null==signData.getDepartmentSignTime()
//			    			&& null!=signData.getProjectSignerId() && null!=signData.getProjectSignTime()) {
//			    		signData.setDepartmentSignerLineId(signUser.getLineId());
//		    			signData.setDepartmentSignTime(calendar.getTime());
//			    		if(!map.get("response").equals(Constant.LINE_EMERGENCE_CONFIRM)) {
//			    			signData.setStatus(Constant.LINE_EMERGENCE_REJECT_BY_DEPARTMENT);
//			    		}
//			    		signData.setUpdateTime(calendar.getTime());
//			    		emergenceOvertimeSignDAO.update(signData);
//			    		if(map.get("response").equals(Constant.LINE_EMERGENCE_CONFIRM)) {
//			    			//buildConfirmVo(signData, Constant.LINE_EMERGENCE_LEVEL_COMPANY);
//			    		}else {
//			    			//推播給申請者
//			    			UserAskForOvertime userAskForOvertime = userAskForOvertimeDAO.get(signData.getAskForOvertimeId());
//			    			SysUser requestUser = sysUserDAO.get(userAskForOvertime.getSysUserId());
//			    			if(StringUtils.hasText(requestUser.getLineId())) {
//			    				LineUtils.postToLine("您於" + sdf.format(signData.getCreateTime()) + "的申請已遭" + Constant.LINE_EMERGENCE_LEVEL_DEPARTMENT + "退回", requestUser.getLineId());
//			    			}
//			    		}
//			    	}
//					break;
//				case Constant.LINE_EMERGENCE_LEVEL_COMPANY:
//					if(signData.getCompanySignerId().equals(signUser.getSysUserId()) && null==signData.getCompanySignTime()
//			    			&& null!=signData.getProjectSignerId() && null!=signData.getProjectSignTime()
//			    			&& null!=signData.getDepartmentSignerId() && null!=signData.getDepartmentSignTime()) {
//			    		signData.setCompanySignerLineId(signUser.getLineId());
//		    			signData.setCompanySignTime(calendar.getTime());
//			    		if(!map.get("response").equals(Constant.LINE_EMERGENCE_CONFIRM)) {
//			    			signData.setStatus(Constant.LINE_EMERGENCE_REJECT_BY_COMPANY);
//			    		}
//			    		signData.setUpdateTime(calendar.getTime());
//			    		emergenceOvertimeSignDAO.update(signData);
//			    		
//			    		UserAskForOvertime userAskForOvertime = userAskForOvertimeDAO.get(signData.getAskForOvertimeId());
//			    		SysUser requestUser = sysUserDAO.get(userAskForOvertime.getSysUserId());
//			    		LineUtils.postToLine("您於" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(userAskForOvertime.getCreateDate()) 
//			    				+ " 申請的災害處理單已完成審核", requestUser.getLineId());
//			    	}
//					break;
				default:
					break;
				}
		    }
		}
	}
	
	public void monthlyEmergenceLineBot() throws Exception {
		Calendar calendar = Calendar.getInstance();
		//calendar.add(Calendar.MONTH, -1);
		List<EmergenceOvertimeSign> dataList = emergenceOvertimeSignDAO.findByDate(
				DateUtils.getFirstDateByYearAndMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)), DateUtils.getFirstDateByYearAndMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1));
		if(dataList.size()>0) {
			//buildListVo(Constant.LINE_EMERGENCE_LEVEL_DEPARTMENT);
			buildListVo(Constant.LINE_EMERGENCE_LEVEL_FINANCE, null);
			//buildListVo(Constant.LINE_EMERGENCE_LEVEL_ADMINISTRATION);
			//buildListVo(Constant.LINE_EMERGENCE_LEVEL_COMPANY);
			if(calendar.get(Calendar.DAY_OF_MONTH)<25) {
				
			}else {
				
			}
		}
	}
	
	public void buildConfirmVo(EmergenceOvertimeSign signData, String level) throws Exception {
		FlexMessageVo flexMessageVo = new FlexMessageVo();
		flexMessageVo.setAltText(Constant.LINE_FLEX_MESSAGE_ALT_TEXT_EMERGENCE_REQUEST);
		List<LineImageUrl> imageUrlList = lineImageUrlDAO.getTypeList(level);
		int random = (int) (Math.random()*imageUrlList.size());
		flexMessageVo.setImageUrl(imageUrlList.get(random).getImageUrl());
		switch (level) {
		case Constant.LINE_EMERGENCE_LEVEL_PROJECT:
			flexMessageVo.setTargetUser(sysUserDAO.get(signData.getProjectSignerId()));
			break;
		case Constant.LINE_EMERGENCE_LEVEL_DEPARTMENT:
			flexMessageVo.setTargetUser(sysUserDAO.get(signData.getDepartmentSignerId()));
			break;
		case Constant.LINE_EMERGENCE_LEVEL_FINANCE:
			flexMessageVo.setTargetUser(sysUserDAO.get(signData.getDepartmentSignerId()));
			break;
		case Constant.LINE_EMERGENCE_LEVEL_ADMINISTRATION:
			flexMessageVo.setTargetUser(sysUserDAO.get(signData.getAdministrationSignerId()));
			break;
		case Constant.LINE_EMERGENCE_LEVEL_COMPANY:
			flexMessageVo.setTargetUser(sysUserDAO.get(signData.getCompanySignerId()));
			break;
		default:
			break;
		}
		UserAskForOvertime userAskForOvertime = userAskForOvertimeDAO.get(signData.getAskForOvertimeId());
		flexMessageVo.setSysUser(sysUserDAO.get(userAskForOvertime.getSysUserId()));
		flexMessageVo.setUserAskForOvertime(userAskForOvertime);
		flexMessageVo.setConfirmParam(LineBotService.getEmergenceConfirmParameter(signData.getToken(), level));
		flexMessageVo.setRejectParam(LineBotService.getEmergenceRejectParameter(signData.getToken(), level));
		flexMessageVo.setHeader(emergenceSignHeader(flexMessageVo));
		flexMessageVo.setBody(emergenceSignBody(flexMessageVo));
		flexMessageVo.setFooter(emergenceSignFooter(flexMessageVo));
		sendFlexMessage(flexMessageVo, null);
	}
	
	public void sendFlexMessage(FlexMessageVo dataVo, String replyToken) {
		logger.info("replyToken : " + replyToken);
		LineMessageVo lineMessageVo = new LineMessageVo();
		lineMessageVo.setTargetId(dataVo.getTargetUser().getLineId());
		lineMessageVo.setAltText(dataVo.getAltText());
		lineMessageVo.setHeader(dataVo.getHeader());
		lineMessageVo.setBody(dataVo.getBody());
		lineMessageVo.setFooter(dataVo.getFooter());
		if(StringUtils.hasText(replyToken)) {
			LineUtils.sendReplyMessage(replyToken, lineMessageVo);
		}else {
			LineUtils.sendFlexMessage(lineMessageVo);
		}
	}
	
	public Box emergenceSignHeader(FlexMessageVo dataVo) {
		List<FlexComponent> headerContents = new ArrayList<FlexComponent>();
		headerContents.add(Text.builder().text("災害處理單申請表").size(FlexFontSize.LG).color("#2FC032").weight(TextWeight.BOLD).align(FlexAlign.START).build());
		headerContents.add(Text.builder().text("申請時間：" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(dataVo.getUserAskForOvertime().getCreateDate())).size(FlexFontSize.XS).align(FlexAlign.START).build());
		
        return Box.builder().layout(FlexLayout.VERTICAL).contents(headerContents).build();
	}
	
	public Box emergenceSignBody(FlexMessageVo dataVo) {
		List<FlexComponent> bodyContents = new ArrayList<FlexComponent>();
		bodyContents.add(Image.builder().url(URI.create(dataVo.getImageUrl())).size(ImageSize.FULL_WIDTH).aspectMode(ImageAspectMode.Cover).build());
		bodyContents.add(Separator.builder().build());
		bodyContents.add(Text.builder().text(dataVo.getSysUser().getOriginalName()).size(FlexFontSize.LG).color("#2FC032").weight(TextWeight.BOLD).align(FlexAlign.START).build());
		bodyContents.add(Separator.builder().build());
		
		List<FlexComponent> textContents = new ArrayList<FlexComponent>();
		List<FlexComponent> textRightContents = new ArrayList<FlexComponent>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		textContents.add(Text.builder().text("申請時段：").size(FlexFontSize.Md).flex(1).align(FlexAlign.START).build());
		textRightContents.add(Text.builder().text(sdf.format(dataVo.getUserAskForOvertime().getStartTime())).size(FlexFontSize.Md).flex(1).align(FlexAlign.END).build());
		textRightContents.add(Text.builder().text(sdf.format(dataVo.getUserAskForOvertime().getEndTime())).size(FlexFontSize.Md).flex(1).align(FlexAlign.END).build());
		textContents.add(Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).flex(2).contents(textRightContents).build());
		bodyContents.add(Box.builder().layout(FlexLayout.HORIZONTAL).spacing(FlexMarginSize.SM).contents(textContents).build());
		bodyContents.add(Separator.builder().build());
		
		List<FlexComponent> textLevelContents = new ArrayList<FlexComponent>();
		textLevelContents.add(Text.builder().text("申請級別：").size(FlexFontSize.Md).flex(1).align(FlexAlign.START).build());
		textLevelContents.add(Text.builder().text(Constant.getEmergenceLevel().get(dataVo.getUserAskForOvertime().getSpendTime().intValue())).size(FlexFontSize.Md).flex(1).align(FlexAlign.END).build());
		bodyContents.add(Box.builder().layout(FlexLayout.HORIZONTAL).spacing(FlexMarginSize.SM).contents(textLevelContents).build());
		bodyContents.add(Separator.builder().build());
		
		bodyContents.add(Text.builder().text("申請事由：").size(FlexFontSize.Md).align(FlexAlign.START).build());
		bodyContents.add(Text.builder().text(dataVo.getUserAskForOvertime().getDescription()).size(FlexFontSize.Md).align(FlexAlign.START).wrap(true).build());
        return Box.builder().layout(FlexLayout.VERTICAL).contents(bodyContents).build();
	}
	
	public Box emergenceSignFooter(FlexMessageVo dataVo) {
	    final Button buttonConfirm = Button.builder().style(ButtonStyle.PRIMARY).height(ButtonHeight.SMALL)
	                    .action(new PostbackAction("Confirm", dataVo.getConfirmParam(), "Confirm"))
	                    .build();
	    final Button buttonReject = Button.builder().style(ButtonStyle.SECONDARY).height(ButtonHeight.SMALL)
                .action(new PostbackAction("Reject", dataVo.getRejectParam(), "Reject"))
                .build();
	    List<FlexComponent> buttonContents = new ArrayList<FlexComponent>();
	    buttonContents.add(buttonConfirm);
	    buttonContents.add(buttonReject);
	    return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).contents(buttonContents).build();
	}
	
	public void buildListVo(String level, String replyToken) throws Exception {
		FlexMessageVo flexMessageVo = new FlexMessageVo();
		flexMessageVo.setAltText(Constant.LINE_FLEX_MESSAGE_ALT_TEXT_EMERGENCE_REQUEST);
		SysUser targetUser;
		switch (level) {
		case Constant.LINE_EMERGENCE_LEVEL_DEPARTMENT:
			targetUser = sysUserDAO.getDepartmentMaster();
			break;
		case Constant.LINE_EMERGENCE_LEVEL_FINANCE:
			targetUser = sysUserDAO.getFinanceMaster();
			break;
		case Constant.LINE_EMERGENCE_LEVEL_ADMINISTRATION:
			targetUser = sysUserDAO.getAdministrationManager();
			break;
		case Constant.LINE_EMERGENCE_LEVEL_COMPANY:
			targetUser = sysUserDAO.getCompanyGold();
			break;
		default:
			targetUser = new SysUser();
			break;
		}
		if(null != targetUser && StringUtils.hasText(targetUser.getLineId())) {
			List<EmergenceOvertimeSign> emergenceList = emergenceOvertimeSignDAO.findByLevel(level);
			if(emergenceList.size()>0) {
				flexMessageVo.setTargetUser(targetUser);
				flexMessageVo.setHeader(emergenceListHeader());
				flexMessageVo.setBody(emergenceListBody(emergenceList));
				flexMessageVo.setFooter(emergenceListFooter(targetUser.getLineId(), level));
				sendFlexMessage(flexMessageVo, replyToken);
			}else {
				LineUtils.sendReplyMessage(replyToken, "本月尚無災害處理單資料");
			}
		}
	}
	
	public Box emergenceListHeader() {
		List<FlexComponent> headerContents = new ArrayList<FlexComponent>();
		headerContents.add(Text.builder().text("災害處理單申請表清單").size(FlexFontSize.LG).color("#2FC032").weight(TextWeight.BOLD).align(FlexAlign.START).build());
		headerContents.add(Text.builder().text("訊息推送時間：" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())).size(FlexFontSize.XS).align(FlexAlign.START).build());
		headerContents.add(Separator.builder().build());
        return Box.builder().layout(FlexLayout.VERTICAL).contents(headerContents).build();
	}
	
	@SuppressWarnings("unchecked")
	public Box emergenceListBody(List<EmergenceOvertimeSign> dataList) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		List<FlexComponent> bodyContents = new ArrayList<FlexComponent>();
		
		Map<String, SysUser> userMap = (Map<String, SysUser>) sysUserDAO.queryToMap("sysUserId");
		for(EmergenceOvertimeSign signData:dataList) {
			List<FlexComponent> overtime1Contents = new ArrayList<FlexComponent>();
			UserAskForOvertime userAskForOvertime = userAskForOvertimeDAO.get(signData.getAskForOvertimeId());
			overtime1Contents.add(Text.builder().text(userMap.get(userAskForOvertime.getSysUserId()).getOriginalName()).size(FlexFontSize.LG).color("#2FC032").weight(TextWeight.BOLD).align(FlexAlign.START).build());
			overtime1Contents.add(Text.builder().text(sdf.format(userAskForOvertime.getStartTime())).size(FlexFontSize.Md).align(FlexAlign.END).build());
			bodyContents.add(Box.builder().layout(FlexLayout.HORIZONTAL).spacing(FlexMarginSize.SM).contents(overtime1Contents).build());
			
			List<FlexComponent> overtime2Contents = new ArrayList<FlexComponent>();
			overtime2Contents.add(Text.builder().text(Constant.getEmergenceLevel().get(userAskForOvertime.getSpendTime().intValue())).size(FlexFontSize.LG).color("#2FC032").weight(TextWeight.BOLD).align(FlexAlign.START).build());
			overtime2Contents.add(Text.builder().text(sdf.format(userAskForOvertime.getEndTime())).size(FlexFontSize.Md).align(FlexAlign.END).build());
			bodyContents.add(Box.builder().layout(FlexLayout.HORIZONTAL).spacing(FlexMarginSize.SM).contents(overtime2Contents).build());
			
//			bodyContents.add(Text.builder().text("申請事由：").size(FlexFontSize.Md).align(FlexAlign.START).build());
//			bodyContents.add(Text.builder().text(userAskForOvertime.getDescription()).size(FlexFontSize.Md).align(FlexAlign.START).wrap(true).build());
//			
			bodyContents.add(Separator.builder().build());
		}
		
        return Box.builder().layout(FlexLayout.VERTICAL).contents(bodyContents).build();
	}
	
	public Box emergenceListFooter(String targetLineId, String level) throws Exception {
		logger.info("gogogogoggoogogogog========" + level);
		List<FlexComponent> buttonContents = new ArrayList<FlexComponent>();
		SysUser companyUser = sysUserDAO.getCompanyGold();
		final Button buttonWeb = Button.builder().style(ButtonStyle.PRIMARY).height(ButtonHeight.SMALL)
	            .action(new URIAction("登入審核", new URI("https://36.230.174.3/CQIHRManage/loginLine?lineId=" + targetLineId), null))
	            .build();
		buttonContents.add(buttonWeb);
		switch (level) {
		case Constant.LINE_EMERGENCE_LEVEL_FINANCE:
			logger.info("gogogogoggoogogogog");
			final Button buttonBoss = Button.builder().style(ButtonStyle.PRIMARY).height(ButtonHeight.SMALL)
		            .action(new MessageAction("要給老闆的連結", "https://36.230.174.3/CQIHRManage/loginLine?lineId=" + companyUser.getLineId()))
		            .build();
			buttonContents.add(buttonBoss);
			logger.info("wwwwwwwwwwwwwwwwwwwwwww");
			break;
		case Constant.LINE_EMERGENCE_LEVEL_ADMINISTRATION:
			
			break;
		case Constant.LINE_EMERGENCE_LEVEL_COMPANY:
			
			break;
		default:
			break;
		}
	    return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).contents(buttonContents).build();
	}

	
	
	public static String getEmergenceConfirmParameter(String token, String level) {
		return "token=" + token + "&level=" + level + "&response=" + Constant.LINE_EMERGENCE_CONFIRM;
	}
	
	public static String getEmergenceRejectParameter(String token, String level) {
		return "token=" + token + "&level=" + level + "&response=" + Constant.LINE_EMERGENCE_REJECT;
	}

	
	public void NoPunchRemind() throws Exception {
		Date nowDate = new Date();
		String nowShift = nowDate.getHours() == 9 ? "09:00":"10:00" ;
		logger.info("nowShift: " + nowShift);
		
		//test
		//nowShift = "09:00";
		
		List<SysUser> userList = sysUserDAO.getEnableRole2LineUser();
		for (SysUser sysUser : userList) {
			logger.info(sysUser.getUserName());
			AttendanceRecord attendanceRecord = attendanceRecordService.getUserToday(sysUser);
			SysUserShift sysUserShift = sysUserShiftDAO.getOneByIdAndDate(sysUser.getSysUserId(), nowDate);
			if (attendanceRecord == null && sysUserShift != null) {
				if (nowShift.equals(sysUserShift.getBoardTime())) {
					logger.info("line "+sysUser.getUserName());
					
					LineMessageVo lineMessageVo = new LineMessageVo();
					lineMessageVo.setTargetId(sysUser.getLineId());
					lineMessageVo.setAltText("未打卡提醒");
					lineMessageVo.setHeader(NoPunchHeader(nowShift));
					//lineMessageVo.setBody(dataVo.getBody());
					lineMessageVo.setFooter(NoPunchFooter());
					LineUtils.sendFlexMessage(lineMessageVo);
				}
			}
		}
		
	}
	
	public Box NoPunchHeader(String nowShift) {
		List<FlexComponent> headerContents = new ArrayList<FlexComponent>();
		headerContents.add(Text.builder().text(nowShift.substring(0,2)+"點未打卡提醒").size(FlexFontSize.LG).color("#2FC032").weight(TextWeight.BOLD).align(FlexAlign.START).build());
		headerContents.add(Text.builder().text("訊息推送時間：" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())).size(FlexFontSize.XS).align(FlexAlign.START).build());
		headerContents.add(Separator.builder().build());
        return Box.builder().layout(FlexLayout.VERTICAL).contents(headerContents).build();
	}
	public Box NoPunchFooter() throws URISyntaxException {
		logger.info("===NoPunchFooter=====");
		List<FlexComponent> buttonContents = new ArrayList<FlexComponent>();
		final Button buttonWeb = Button.builder().style(ButtonStyle.PRIMARY).height(ButtonHeight.SMALL)
	            .action(new URIAction("today", new URI("https://hr.cqiserv.com/today"), null))
	            .build();
		buttonContents.add(buttonWeb);
		
	    return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).contents(buttonContents).build();
	}
}
