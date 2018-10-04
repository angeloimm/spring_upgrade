package it.olegna.spring.upgrade.hibernate.cache.region;
import javax.cache.Cache;

import org.hibernate.cache.jcache.internal.JCacheRegionFactory;



public class CacheRegionFactory extends JCacheRegionFactory {

	private static final long serialVersionUID = -6473897909385135030L;

	@Override
	protected Cache<Object, Object> createCache(String regionName) {
		throw new IllegalArgumentException("Cache di hibernate non conosciuta: " + regionName);
	}
}