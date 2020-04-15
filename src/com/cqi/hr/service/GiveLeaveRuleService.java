package com.cqi.hr.service;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.cqi.hr.dao.AbstractDAO;
import com.cqi.hr.dao.GiveLeaveRuleDAO;
import com.cqi.hr.entity.GiveLeaveRule;

@Service
public class GiveLeaveRuleService extends AbstractService<GiveLeaveRule>{
	@Resource GiveLeaveRuleDAO giveLeaveRuleDAO;
	
	@Override
	protected AbstractDAO<GiveLeaveRule> getDAO() {
		return giveLeaveRuleDAO;
	}

}
