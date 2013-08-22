package eu.activelogic.spring.batch.common;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.NamedQuery;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;

/**
 * A generic HQL and named query executor {@link Tasklet}. Will execute HQL using
 * Hibernate session or execute named query from {@link Session}.
 * 
 * Only one of two can be set, with HQL query taking precedence.
 * 
 * Will delegate the actual execution to {@link SQLExecutorTasklet}
 * 
 * @author Aleksandr Panzin (alex from activelogic.eu)
 */
public class HQLExecutorTasklet extends SQLExecutorTasklet {

	private String hqlQuery;
	private String namedQuery;

	public HQLExecutorTasklet() {
		LOGGER = LoggerFactory.getLogger(SQLExecutorTasklet.class);
	}

	/**
	 * Builds a {@link Query} or {@link NamedQuery} object for execution.
	 */
	@Override
	protected Query buildQuery() {
		Session session = getSession();
		Query q = null;
		if (hqlQuery != null) {
			q = session.createQuery(hqlQuery);
		} else if (namedQuery != null) {
			q = session.getNamedQuery(namedQuery);
		}
		return q;
	}

	public void setHqlQuery(String hqlQuery) {
		this.hqlQuery = hqlQuery;
	}

	public void setNamedQuery(String namedQuery) {
		this.namedQuery = namedQuery;
	}

}
