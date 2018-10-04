package it.olegna.spring.upgrade.scan.dao;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import it.olegna.spring.upgrade.hibernate.models.AbstractModel;
public abstract class AbstractDao<PK extends Serializable, T extends AbstractModel> {
	private static final Logger logger = LoggerFactory.getLogger(AbstractDao.class.getName());
	@Autowired
	private SessionFactory sf;
	@Value("${config.db.hibernate.batch.size}")
	private int dimensioneBatch;
	protected abstract Class<T> getPersistentClass();
	protected Session getSession()
	{
		return sf.getCurrentSession();
	}
	protected CriteriaBuilder createCriteriaBuilder()
	{
		return getSession().getCriteriaBuilder();
	}
	public List<T> findAll()
	{
		CriteriaBuilder cb = createCriteriaBuilder();
		CriteriaQuery<T> rootQery = cb.createQuery(getPersistentClass());
		Root<T> rootEntry = rootQery.from(getPersistentClass());
		CriteriaQuery<T> all = rootQery.select(rootEntry);
		TypedQuery<T> allQuery = getSession().createQuery(all);
		return allQuery.getResultList();
	}
	public Long count()
	{
		CriteriaBuilder qb = createCriteriaBuilder();
		CriteriaQuery<Long> cq = qb.createQuery(Long.class);
		cq.select(qb.count(cq.from(getPersistentClass())));
		return getSession().createQuery(cq).getSingleResult();
	}
	public Long count(DetachedCriteria dc) {
		if( logger.isDebugEnabled() )
		{
			logger.debug("Aggiungo projection rowcount");
		}
		dc.setProjection(Projections.rowCount());
		Session session = getSession();
		Number numb = (Number)dc.getExecutableCriteria(session).uniqueResult();
		return numb.longValue();
	}
	public T getByKey(PK key)
	{
		return (T) getSession().get(getPersistentClass(), key);
	}
	public T loadByKey(PK key)
	{
		return (T) getSession().load(getPersistentClass(), key);
	}
	public void persist(T entity)
	{
		getSession().persist(entity);
	}
	public void persistFlush(T entity)
	{
		getSession().persist(entity);
		getSession().flush();
	}
	public void update(T entity)
	{
		getSession().update(entity);
	}
	public void delete(T entity)
	{
		getSession().delete(entity);
	}
	public void persist(List<T> entities)
	{
		Session sessione = getSession();
		int i = 0;
		for (T t : entities)
		{
			sessione.persist(t);
			i++;
			if( i%dimensioneBatch == 0 )
			{
				sessione.flush();
				sessione.clear();
			}
		}
	}
	@SuppressWarnings("unchecked")
	public List<T> findByDetachedCriteria(DetachedCriteria dc, int offset, int maxRecordNum) {
		if( logger.isDebugEnabled() )
		{
			logger.debug("DETACHED CRITERIA [{}] OFFSET [{}] NUMERO MASSIMO DI RECORD [{}]", dc, offset, maxRecordNum);
		}
		Criteria criteria = dc.getExecutableCriteria(getSession());
		if( offset >= 0 && maxRecordNum > 0 )
		{
			criteria.setFirstResult(offset);
			criteria.setMaxResults(maxRecordNum);
		}
		return criteria.list();
	}

	public Integer executeJpqlStatement(String jpql, Map<String, Object> jpqlParams) {
		if( !StringUtils.hasText(jpql) )
		{
			throw new IllegalArgumentException("Impossibile eseguire lo statement JPQL; query nulla o vuota <"+jpql+">");
		}
		if( logger.isDebugEnabled() )
		{
			logger.debug("ESECUZIONE JPQL [{}] con PARAMETRI [{}]", jpql, jpqlParams);
		}
		Query q = getSession().createQuery(jpql);
		if( jpqlParams != null && !jpqlParams.isEmpty() )
		{
			jpqlParams.keySet().stream().forEach(key ->{
				q.setParameter(key, jpqlParams.get(key));
			});
		}
		int result = q.executeUpdate();
		return result;
	}
}