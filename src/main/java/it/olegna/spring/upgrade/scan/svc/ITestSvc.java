package it.olegna.spring.upgrade.scan.svc;

import java.util.List;

import it.olegna.spring.upgrade.hibernate.models.TestUpgrade;

public interface ITestSvc {
	
	void createTest(TestUpgrade t);
	List<TestUpgrade> findAll();
}
