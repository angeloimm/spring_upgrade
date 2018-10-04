package it.olegna.spring.upgrade.scan.components;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import it.olegna.spring.upgrade.scan.TenantContextHolder;

@Component
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {
	public static final String DEFAULT_TENANT_ID = "defaultTenantId";
	@Override
	public String resolveCurrentTenantIdentifier() {
		String tenant = TenantContextHolder.getTenant();
		return StringUtils.hasText(tenant) ? tenant : DEFAULT_TENANT_ID;
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		
		return true;
	}

}
