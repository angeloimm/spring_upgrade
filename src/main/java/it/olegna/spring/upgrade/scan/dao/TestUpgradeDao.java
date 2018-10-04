package it.olegna.spring.upgrade.scan.dao;

import org.springframework.stereotype.Repository;

import it.olegna.spring.upgrade.hibernate.models.TestUpgrade;
@Repository
public class TestUpgradeDao extends AbstractDao<String, TestUpgrade> {

	@Override
	protected Class<TestUpgrade> getPersistentClass() {
		
		return TestUpgrade.class;
	}

}
