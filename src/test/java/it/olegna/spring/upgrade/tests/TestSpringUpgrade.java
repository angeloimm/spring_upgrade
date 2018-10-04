package it.olegna.spring.upgrade.tests;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.olegna.spring.upgrade.configuration.DBConfig;
import it.olegna.spring.upgrade.hibernate.models.TestUpgrade;
import it.olegna.spring.upgrade.scan.svc.ITestSvc;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DBConfig.class })
public class TestSpringUpgrade {
	private static final Logger logger = LoggerFactory.getLogger(TestSpringUpgrade.class.getName());
	@Autowired
	private ITestSvc svc;
	@Test
	public void testCreazione() {
		try {
			TestUpgrade tu = new TestUpgrade();
			tu.setCreatoDa("test");
			tu.setDataCreazione(new Date());
			tu.setCol("col");
			tu.setColonna("colonna");
			svc.createTest(tu);
		} catch (Exception e) {
			logger.error("Creation error", e);
		}
	}
}
