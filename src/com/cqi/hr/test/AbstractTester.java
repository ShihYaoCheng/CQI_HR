package com.cqi.hr.test;

import java.io.FileNotFoundException;
import org.apache.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Log4jConfigurer;

@RunWith(SpringJUnit4ClassRunner.class)
//@TransactionConfiguration(defaultRollback=true)
@ContextConfiguration(locations={"classpath:appContext.xml"})
//@Transactional
public abstract class AbstractTester {
	protected Logger logger = Logger.getLogger(this.getClass());
	public AbstractTester() {
		try {
			Log4jConfigurer.initLogging("classpath:log4j.properties");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
