package it.olegna.spring.upgrade.configuration;
import static it.olegna.spring.upgrade.scan.components.CurrentTenantIdentifierResolverImpl.DEFAULT_TENANT_ID;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.SessionFactory;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import it.olegna.spring.upgrade.hibernate.cache.region.CacheRegionFactory;
import it.olegna.spring.upgrade.scan.components.ConfigurableMultitenantConnectionProvider;
import it.olegna.spring.upgrade.scan.components.CurrentTenantIdentifierResolverImpl;
@Configuration
@EnableTransactionManagement
@PropertySource(value= {"classpath:commonServices.properties"}, encoding="UTF-8", ignoreResourceNotFound=false)
@Import(value= {HibDbCacheConfig.class, PwdEncoders.class})
@ComponentScan(basePackages= {"it.olegna.spring.upgrade.scan"})
public class DBConfig {
	private static final Logger logger = LoggerFactory.getLogger(DBConfig.class.getName());
	@Autowired
	private Environment env;
	private Properties hibProps()
	{
		Properties props = new Properties();

		props.put(org.hibernate.cfg.Environment.DIALECT, env.getProperty("config.db.hibernate.dialect"));
		props.put(org.hibernate.cfg.Environment.SHOW_SQL, Boolean.valueOf(env.getProperty("config.db.hibernate.show.sql")));
		props.put(org.hibernate.cfg.Environment.GENERATE_STATISTICS, Boolean.valueOf(env.getProperty("config.db.hibernate.generate.statistics")));
		props.put(org.hibernate.cfg.Environment.FORMAT_SQL, Boolean.valueOf(env.getProperty("config.db.hibernate.format.sql")));
		props.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, env.getProperty("config.db.hibernate.ddl.auto"));
		props.put(org.hibernate.cfg.Environment.USE_SECOND_LEVEL_CACHE, Boolean.valueOf(env.getProperty("config.db.hibernate.use.second.cache")));
		props.put(org.hibernate.cfg.Environment.USE_QUERY_CACHE, Boolean.valueOf(env.getProperty("config.db.hibernate.use.query.cache")));
		props.put(org.hibernate.cfg.Environment.CACHE_REGION_FACTORY, CacheRegionFactory.class.getName());
		props.put(org.hibernate.cfg.Environment.STATEMENT_BATCH_SIZE, Integer.valueOf(env.getProperty("config.db.hibernate.batch.size")));
		props.put(org.hibernate.cfg.Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
		props.put(org.hibernate.cfg.Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider());
		props.put(org.hibernate.cfg.Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolver());
		props.put("hibernate.javax.cache.provider", "org.ehcache.jsr107.EhcacheCachingProvider");
		if( logger.isDebugEnabled() )
		{
			logger.debug("Valorizzate hibernate properties {}", props);
		}
		return props;
	}
	@Primary
	@Bean(name = "dataSourcesMtApp")
	public Map<String, DataSource> tenantDatasources()
	{
		Map<String, DataSource> result = new HashMap<>();
		DataSource ds = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
		result.put(DEFAULT_TENANT_ID, ds);
		return result;
	}
	@Bean(initMethod="start",destroyMethod="stop")
	public org.h2.tools.Server h2WebConsonleServer () throws SQLException {
		return org.h2.tools.Server.createWebServer("-web","-webAllowOthers","-webDaemon","-webPort", "8082");
	}
	@Bean
	public MultiTenantConnectionProvider multiTenantConnectionProvider() {
		return new ConfigurableMultitenantConnectionProvider();
	}

    @Bean
    public CurrentTenantIdentifierResolver currentTenantIdentifierResolver() {
        return new CurrentTenantIdentifierResolverImpl();
    }

	@Bean
	public LocalSessionFactoryBean sessionFactory()
	{
		LocalSessionFactoryBean lsfb = new LocalSessionFactoryBean();
		lsfb.setPackagesToScan(new String[]{"it.olegna.spring.upgrade.hibernate.models"});
		lsfb.setHibernateProperties(hibProps());
		return lsfb;
	}
	@Bean(name="hibernateTxMgr")
	public PlatformTransactionManager transactionManager(@Autowired SessionFactory s){
		HibernateTransactionManager result = new HibernateTransactionManager();
		result.setSessionFactory(s);
		return result;
	}
}