package com.cqi.hr.dao;

import org.springframework.stereotype.Repository;

import com.cqi.hr.entity.LineWebhookLog;

@Repository
public class LineWebhookLogDAO extends AbstractDAO<LineWebhookLog> {
	@Override
	protected Class<LineWebhookLog> getEntityClass() {
		return LineWebhookLog.class;
	}
	
}

