package it.olegna.spring.upgrade.scan.components;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ConfigurableMultitenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

	private static final long serialVersionUID = -6334709226415098864L;
	@Autowired
	@Qualifier("dataSourcesMtApp")
	private Map<String, DataSource> tenantDatasources;
	@Override
	protected DataSource selectAnyDataSource() {
		
		return tenantDatasources.values().iterator().next();
	}

	@Override
	protected DataSource selectDataSource(String tenantIdentifier) {
		if( !StringUtils.hasText(tenantIdentifier) )
		{
			throw new IllegalArgumentException("Impossibile recuperare il datasource. Id tenant nullo o vuoto");
		}
		if( !tenantDatasources.containsKey(tenantIdentifier) )
		{
			throw new IllegalArgumentException("Nessun datasource trovato per tenant id "+tenantIdentifier);
		}
		return tenantDatasources.get(tenantIdentifier);
	}
}
