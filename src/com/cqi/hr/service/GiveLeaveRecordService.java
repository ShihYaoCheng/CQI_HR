package com.cqi.hr.service;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.cqi.hr.dao.AbstractDAO;
import com.cqi.hr.dao.GiveLeaveRecordDAO;
import com.cqi.hr.entity.GiveLeaveRecord;

@Service
public class GiveLeaveRecordService extends AbstractService<GiveLeaveRecord>{
	@Resource GiveLeaveRecordDAO giveLeaveRecordDAO;
	
	@Override
	protected AbstractDAO<GiveLeaveRecord> getDAO() {
		return giveLeaveRecordDAO;
	}

}
