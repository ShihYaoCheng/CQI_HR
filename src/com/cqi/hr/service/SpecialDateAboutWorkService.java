package com.cqi.hr.service;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cqi.hr.dao.AbstractDAO;
import com.cqi.hr.dao.SpecialDateAboutWorkDAO;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SpecialDateAboutWork;
import com.cqi.hr.util.DateUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class SpecialDateAboutWorkService extends AbstractService<SpecialDateAboutWork>{
	@Resource SpecialDateAboutWorkDAO specialDateAboutWorkDAO;
	
	@Override
	protected AbstractDAO<SpecialDateAboutWork> getDAO() {
		return specialDateAboutWorkDAO;
	}
	
	@Transactional
	public SpecialDateAboutWork getOneByDate(Date theDay) throws Exception{
		return specialDateAboutWorkDAO.getOneByDate(theDay);
	}
	
	@Transactional
	public List<SpecialDateAboutWork> getListBetweenDate(Date startDate, Date endDate) throws Exception{
		return specialDateAboutWorkDAO.getListBetweenDate(startDate, endDate);
	}
	
	@Transactional
	public PagingList<SpecialDateAboutWork> getListByPage(Integer page) throws Exception {
		return specialDateAboutWorkDAO.getListByPage(page);
	}
	
	@Transactional
	public void readSpecialDate(MultipartFile multipartFile) throws Exception {
		String stringData = new String(multipartFile.getBytes());
		logger.info("data : " + stringData);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		JSONArray jsonArray = JSONArray.fromObject(stringData);
		Calendar nowCal = Calendar.getInstance();
		for(int i=0;i<jsonArray.size();i++) {
			JSONObject jsonObject = jsonArray.optJSONObject(i);
			SpecialDateAboutWork data = new SpecialDateAboutWork();
			data.setDayDesc(jsonObject.optString("Subject"));
			data.setTheDay(sdf.parse(jsonObject.optString("Start Date")));
			if(data.getDayDesc().contains("上班")) {
				data.setIsWorkDay(1);
			}else {
				data.setIsWorkDay(0);
			}
			SpecialDateAboutWork dbDate = specialDateAboutWorkDAO.getOneByDate(data.getTheDay());
			if(null == dbDate) {
				data.setCreateDate(nowCal.getTime());
				data.setStatus(1);
				specialDateAboutWorkDAO.persist(data);
			}else {
				dbDate.setDayDesc(data.getDayDesc());
				dbDate.setTheDay(data.getTheDay());
				dbDate.setUpdateDate(nowCal.getTime());
				dbDate.setIsWorkDay(data.getIsWorkDay());
				dbDate.setStatus(1);
				specialDateAboutWorkDAO.update(dbDate);
			}
		}
	}
	
	@Transactional
	public JSONArray getCalendarSimpleData(Date start, Date end) throws Exception{
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		JSONArray dataArray = new JSONArray();
		List<SpecialDateAboutWork> dataList = specialDateAboutWorkDAO.getListBetweenDate(start, end);
		for(SpecialDateAboutWork data:dataList) {
			JSONObject jsonObject = new JSONObject();
			//For Test
			//jsonObject.put("title", getLeaveCalendarTitleForTest(mappingUser.get(data.getSysUserId()), data, mappingLeave));
			//Production
			jsonObject.put("title", (data.getIsWorkDay().equals(1)?"[補班] ":"[放假] ") + data.getDayDesc());
			jsonObject.put("start", datetimeFormat.format(DateUtils.clearTime(data.getTheDay())));
			jsonObject.put("end", datetimeFormat.format(DateUtils.maxTime(data.getTheDay())));
			dataArray.add(jsonObject);
		}
		return dataArray;
	}
	
	@Transactional
	public boolean isWorkDay() throws Exception {
		Calendar today = DateUtils.clearTime(Calendar.getInstance());
		SpecialDateAboutWork specialDate = specialDateAboutWorkDAO.getOneByDate(today.getTime());
		return (today.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY && today.get(Calendar.DAY_OF_WEEK)!=Calendar.SATURDAY && null==specialDate) 
				|| (null!=specialDate && specialDate.getIsWorkDay()==1);
	}
}
