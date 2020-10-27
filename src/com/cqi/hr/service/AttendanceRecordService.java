package com.cqi.hr.service;


import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.dao.AbstractDAO;
import com.cqi.hr.dao.AttendanceRecordDAO;
import com.cqi.hr.dao.SysUserDAO;
import com.cqi.hr.entity.AttendanceRecord;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.util.DateUtils;
import com.cqi.hr.util.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class AttendanceRecordService extends AbstractService<AttendanceRecord>{
	@Resource AttendanceRecordDAO attendanceRecordDAO;
	
	@Resource SysUserDAO sysUserDAO;
	
	@Override
	protected AbstractDAO<AttendanceRecord> getDAO() {
		return attendanceRecordDAO;
	}

	@SuppressWarnings({ "resource", "incomplete-switch" })
	@Transactional
	public void parser26FloorExcelFile(MultipartFile multipartFile) throws Exception {
		InputStream inputStream = multipartFile.getInputStream();
		POIFSFileSystem fs = new POIFSFileSystem(inputStream);
		
		Workbook inWorkbook = new HSSFWorkbook(fs.getRoot(), true);
		//get sheet what index of 0
		Sheet sheet = inWorkbook.getSheetAt(0);
		/*
        Map<Integer, List<String>> map = new HashMap<Integer, List<String>>(); //第一個參數表示行數 第二個List保存該行的cell數據
        int i = 0;
        for(Row row : sheet){
            map.put(i, new ArrayList<String>());
            for(Cell cell : row){ // 遍歷當前行的所有cell
                switch(cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        map.get(i).add(cell.getRichStringCellValue().getString()); // 如果是字符串則保存
                        break;
                    case Cell.CELL_TYPE_BLANK:
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        map.get(i).add(cell.getNumericCellValue()+""); //將數值轉換為字符串
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                        break;
                    case Cell.CELL_TYPE_FORMULA:
                        break;
                    case Cell.CELL_TYPE_ERROR:
                        break;
                }
            }
            i++;
        }
        Set<Integer> keys = map.keySet(); // 以下為遍歷 Map看解析結果
        Iterator<Integer> it = keys.iterator();
        while(it.hasNext()){
            List<String> list = map.get(it.next());
            for(String s : list){
                System.out.print(s+"      ");
            }
            System.out.println();
        }
        */
		int i=0;
		AttendanceRecord attendanceRecord = new AttendanceRecord();
		
		int firstIndex = -1;
		
		int yearIndexI = -1;
		int yearIndexJ = -1;
		int year = 0;
		
		int nameIndexI = -1;
		int nameIndexJ = -1;
		
		String sysUserIdTemp = "";
		
        for(Row row : sheet){
            int j=0;
            for(Cell cell : row){ // 遍歷當前行的所有cell
                switch(cell.getCellType()) {
                    case STRING:
                        //logger.info("CELL_TYPE_STRING i : " + i + ", j : " + j + ", value : " + cell.getRichStringCellValue().getString());
                        if("出勤報表".equals(cell.getRichStringCellValue().getString())) {
                        	firstIndex = i;
                        }
                        
                        if("查詢區間".equals(cell.getRichStringCellValue().getString())) {
                        	yearIndexI = i;
                        	yearIndexJ = j;
                        }
                        if(yearIndexI==i && (yearIndexJ+1)==j) {
                        	year = Integer.parseInt(cell.getRichStringCellValue().getString().split("/")[0]);
                        	yearIndexI = -1;
                        	yearIndexJ = -1;
                        }
                        
                        if("員工姓名".equals(cell.getRichStringCellValue().getString())) {
                        	nameIndexI = i;
                        	nameIndexJ = j;
                        }
                        if(nameIndexI==i && j==(nameIndexJ+2)) {
                        	SysUser sysUser = sysUserDAO.getOneByOriginalNameAndDepartment(cell.getRichStringCellValue().getString(), Constant.DEPARTMENT_26F);
                        	if(null!=sysUser) {
                        		sysUserIdTemp = sysUser.getSysUserId();
                            	nameIndexI = -1;
                            	nameIndexJ = -1;
                        	}
                        }
                        
                        if("人員簽名".equals(cell.getRichStringCellValue().getString())) {
                        	sysUserIdTemp = "";
                        	firstIndex = -1;
                        }
                        
                        if(firstIndex!=-1 && firstIndex + 8 <= i) {
                        	//日期
                        	if(j==0) {
                        		attendanceRecord = new AttendanceRecord();
                            	attendanceRecord.setSysUserId(sysUserIdTemp);
                        		String dateString = cell.getRichStringCellValue().getString().split("\\(")[0];
                        		Calendar attendanceDate = Calendar.getInstance();
                        		attendanceDate.set(Calendar.YEAR, year);
                        		attendanceDate.set(Calendar.MONTH, Integer.parseInt(dateString.split("/")[0]) - 1);
                        		attendanceDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateString.split("/")[1]));
                        		attendanceRecord.setAttendanceDate(DateUtils.clearTime(attendanceDate.getTime()));
                        	}
                        	if(j==1) {
                        		if(StringUtils.hasText(cell.getRichStringCellValue().getString())) {
                        			attendanceRecord.setArriveTime(cell.getRichStringCellValue().getString());
                        		}
                        	}
                        	if(j==6) {
                        		if(StringUtils.hasText(cell.getRichStringCellValue().getString())) {
                        			attendanceRecord.setLeaveTime(cell.getRichStringCellValue().getString());
                        			saveData(attendanceRecord);
                        		}
                        	}
                        }
                        
                        break;
                    case BLANK:
                        break;
                    case NUMERIC:
                    	logger.info("CELL_TYPE_NUMERIC i : " + i + ", j : " + j + ", value : " + cell.getNumericCellValue());
                        break;
                    case BOOLEAN:
                        break;
                    case FORMULA:
                        break;
                    case ERROR:
                        break;
                }
                j++;
            }
            i++;
        }
	}
	
	@SuppressWarnings("resource")
	@Transactional
	public void parser25FloorExcelFile(MultipartFile multipartFile) throws Exception {
		InputStream inputStream = multipartFile.getInputStream();
		
		POIFSFileSystem fs = new POIFSFileSystem(inputStream);
		
		Workbook inWorkbook = new HSSFWorkbook(fs.getRoot(), true);
		//get sheet what index of 0
		Sheet sheet = inWorkbook.getSheetAt(0);
		Calendar calendar = Calendar.getInstance();
		boolean setReportDate = false;
		int i=0;
				
		int nameIndexI = -1;
		int nameIndexJ = -1;
		
		String sysUserIdTemp = "";
		for (Row row : sheet) {
			int j=0;
		    for (Cell cell : row) {
		        switch (cell.getCellType()) {
		            case STRING:
		                if(!setReportDate) {
		                	String textValue = cell.getRichStringCellValue().getString();
		                	if(textValue.indexOf("考勤日期：")>=0 && textValue.indexOf("～")>=0) {
		                		Date recordDate = DateUtils.castDate(textValue.split("：")[1].split("～")[0]);
		                		calendar.setTime(recordDate);
		                		setReportDate = true;
		                	}
		                }
		                if(setReportDate) {
		                	String textValue = cell.getRichStringCellValue().getString();
		                	if(textValue.indexOf("工號")>-1) {
			                	sysUserIdTemp = "";
			                	nameIndexI = -1;
			                	nameIndexJ = -1;
			                }
			                if(textValue.indexOf("姓名：")>-1) {
			                	logger.debug("set nameIndex i j ");
			                	nameIndexI = i;
			                	nameIndexJ = j;
			                }
			                if(nameIndexI==i && nameIndexJ==(j-1)) {
			                	logger.debug("getName : " + cell.getRichStringCellValue().getString());
			                	SysUser sysUser = sysUserDAO.getOneByOriginalNameAndDepartment(cell.getRichStringCellValue().getString(), Constant.DEPARTMENT_25F);
			                	if(sysUser!=null) {
			                		sysUserIdTemp = sysUser.getSysUserId();
			                	}
			                }
			                if(StringUtils.hasText(sysUserIdTemp)) {
			                	if(nameIndexI>0 && nameIndexI<=(i-2)) {
			                		//日期: j+1號
			                		AttendanceRecord attendanceRecord = new AttendanceRecord();
			                		attendanceRecord.setSysUserId(sysUserIdTemp);
			                		Calendar attendanceDate = Calendar.getInstance();
			                		attendanceDate.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
			                		attendanceDate.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
			                		attendanceDate.set(Calendar.DAY_OF_MONTH, (j+1));
			                		attendanceRecord.setAttendanceDate(DateUtils.clearTime(attendanceDate.getTime()));
			                		attendanceRecord.setOriginalData(cell.getRichStringCellValue().getString());
			                		String[] timeArray = cell.getRichStringCellValue().getString().split("\n");
			                		logger.info("timeArray.length : " + timeArray.length);
			                		if(timeArray.length==2) {
				                		String timeString2 = Pattern.compile("\\s+").matcher(timeArray[1]).replaceAll("");
				                		if(StringUtils.hasText(timeString2)) {
				                			attendanceRecord.setArriveTime(timeArray[0]);
					                		attendanceRecord.setLeaveTime(timeArray[1]);
				                		}else {
				                			if(timeArray[0].compareTo("07:00")<0) {
				                				AttendanceRecord updateData = new AttendanceRecord();
				                				updateData.setSysUserId(sysUserIdTemp);
				                				updateData.setLeaveTime(timeArray[0]);
				                				Calendar updateCalendar = Calendar.getInstance();
				                				updateCalendar.setTime(attendanceDate.getTime());
				                				updateCalendar.add(Calendar.DAY_OF_MONTH, -1);
				                				updateData.setOriginalData(cell.getRichStringCellValue().getString());
				                				updateData.setAttendanceDate(DateUtils.clearTime(updateCalendar.getTime()));
				                				updateAttendanceTime(updateData);
				                			}else if(timeArray[0].compareTo("17:00")<0) {
				                				attendanceRecord.setArriveTime(timeArray[0]);
				                			}else {
				                				attendanceRecord.setLeaveTime(timeArray[0]);
				                			}
				                		}
			                		}else {
										logger.error("25樓的打卡時間長度例外");
									}
			                		saveData(attendanceRecord);
			                	}
			                }
		                }
		                break;
		            case NUMERIC:
//		                if (DateUtil.isCellDateFormatted(cell)) {
//		                    System.out.println("DateUtil NUMERIC : " + cell.getDateCellValue());
//		                } else {
//		                    System.out.println("Else NUMERIC : " + cell.getNumericCellValue());
//		                }
		                break;
		            case BOOLEAN:
//		                System.out.println("BOOLEAN : " + cell.getBooleanCellValue());
		                break;
		            case FORMULA:
//		                System.out.println("FORMULA : " + cell.getCellFormula());
		                break;
		            case BLANK:
//		                System.out.println();
		                break;
		            default:
//		                System.out.println();
		        }
		        j++;
		    }
		    i++;
		}
	}
	
	@Transactional
	public void saveData(AttendanceRecord attendanceRecord) throws Exception {
		if(attendanceRecord!=null) {
			AttendanceRecord dbData = attendanceRecordDAO.getOneByUserIdAndDate(attendanceRecord.getSysUserId(), attendanceRecord.getAttendanceDate());
			if(dbData!=null) {
				if(StringUtils.hasText(attendanceRecord.getArriveTime())) {
					dbData.setArriveTime(attendanceRecord.getArriveTime());
				}
				if(StringUtils.hasText(attendanceRecord.getLeaveTime())) {
					dbData.setLeaveTime(attendanceRecord.getLeaveTime());
				}
				dbData.setUpdateDate(new Date());
				attendanceRecordDAO.update(dbData);
			}else {
				attendanceRecord.setStatus(Constant.STATUS_ENABLE);
				attendanceRecord.setCreateDate(new Date());
				attendanceRecordDAO.persist(attendanceRecord);
			}
		}else {
			logger.info("saveData AttendanceRecord null");
		}
	}
	
	@Transactional
	public void updateAttendanceTime(AttendanceRecord attendanceRecord) throws Exception {
		if(attendanceRecord!=null) {
			AttendanceRecord dbData = attendanceRecordDAO.getOneByUserIdAndDate(attendanceRecord.getSysUserId(), attendanceRecord.getAttendanceDate());
			if(dbData!=null) {
				if(StringUtils.hasText(attendanceRecord.getArriveTime())) {
					dbData.setArriveTime(attendanceRecord.getArriveTime());
				}
				if(StringUtils.hasText(attendanceRecord.getLeaveTime())) {
					dbData.setLeaveTime(attendanceRecord.getLeaveTime());
				}
				if(dbData.getOriginalData().indexOf(attendanceRecord.getOriginalData())<0) {
					dbData.setOriginalData(dbData.getOriginalData() + "\r\n" + attendanceRecord.getOriginalData());
				}
				dbData.setUpdateDate(new Date());
				attendanceRecordDAO.update(dbData);
				
			}
		}
	}
	
	@Transactional
	public JSONArray getMonthlyData(Date start, Date end, String userId) throws Exception {
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'");
		JSONArray dataArray = new JSONArray();
		List<AttendanceRecord> attendanceRecordList = attendanceRecordDAO.getMonthlyData(start, end, userId);
		for(AttendanceRecord data:attendanceRecordList) {
			JSONObject jsonObject = new JSONObject();
			//For Test
			//jsonObject.put("title", getLeaveCalendarTitleForTest(mappingUser.get(data.getSysUserId()), data, mappingLeave));
			//Production
			jsonObject.put("title", getAttendanceCalendarTitle(data));
			jsonObject.put("start", datetimeFormat.format(data.getAttendanceDate()) + (StringUtils.hasText(data.getArriveTime())?StringUtils.showText(data.getArriveTime()):StringUtils.showText(data.getLeaveTime())));
			if(StringUtils.hasText(data.getLeaveTime()) && data.getLeaveTime().compareTo("07:00") < 0) {
				Calendar nextDay = Calendar.getInstance();
				nextDay.setTime(data.getAttendanceDate());
				nextDay.add(Calendar.DAY_OF_MONTH, +1);
				jsonObject.put("end", datetimeFormat.format(nextDay.getTime()) + StringUtils.showText(data.getLeaveTime()));
			}else {
				jsonObject.put("end", datetimeFormat.format(data.getAttendanceDate()) + (StringUtils.hasText(data.getLeaveTime())?StringUtils.showText(data.getLeaveTime()):StringUtils.showText(data.getArriveTime())));
			}
			dataArray.add(jsonObject);
		}
		return dataArray;
	}
	
	@Transactional
	public AttendanceRecord getUserToday(SysUser sysUser) throws Exception {
		return attendanceRecordDAO.getOneByUserIdAndDate(sysUser.getSysUserId(), DateUtils.clearTime(new Date()));
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public Map<String, AttendanceRecord> getMapToday() throws Exception{
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("attendanceDate", DateUtils.getTodayWithoutHourMinSec());
		logger.info("Tst : " + DateUtils.getTodayWithoutHourMinSec());
		return (Map<String, AttendanceRecord>) getDAO().queryToMap("sysUserId", criteria);
	}
	
	public static String getAttendanceCalendarTitle(AttendanceRecord attendanceRecord) {
		StringBuilder attendanceName = new StringBuilder();
		attendanceName.append("[打卡]");
		attendanceName.append(" ");
		attendanceName.append(StringUtils.showText(attendanceRecord.getArriveTime()));
		attendanceName.append("~");
		attendanceName.append(StringUtils.showText(attendanceRecord.getLeaveTime()));
        return attendanceName.toString();
	}
}
