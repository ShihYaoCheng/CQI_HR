package com.cqi.hr.dao;

import org.springframework.stereotype.Repository;
import com.cqi.hr.entity.LineUser;

@Repository
public class LineUserDAO extends AbstractDAO<LineUser> {
	@Override
	protected Class<LineUser> getEntityClass() {
		return LineUser.class;
	}
	
}

