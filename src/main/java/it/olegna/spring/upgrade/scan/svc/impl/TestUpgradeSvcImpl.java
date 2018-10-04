package it.olegna.spring.upgrade.scan.svc.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.olegna.spring.upgrade.hibernate.models.TestUpgrade;
import it.olegna.spring.upgrade.scan.dao.TestUpgradeDao;
import it.olegna.spring.upgrade.scan.svc.ITestSvc;

@Service
public class TestUpgradeSvcImpl implements ITestSvc {
	@Autowired
	private TestUpgradeDao repo;
	@Override
	@Transactional(transactionManager = "hibernateTxMgr", rollbackFor = Exception.class, readOnly = false) 
	public void createTest(TestUpgrade t) {
		repo.persist(t);
	}

	@Override
	@Transactional(transactionManager = "hibernateTxMgr", rollbackFor = Exception.class, readOnly = true)
	public List<TestUpgrade> findAll() {
		return repo.findAll();
	}

}
