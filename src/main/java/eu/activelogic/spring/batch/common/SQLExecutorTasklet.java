package eu.activelogic.spring.batch.common;


import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Required;

/**
 * A generic SQL executor {@link Tasklet}. Will execute SQL using Hibernate's
 * connection.
 * 
 * A query can be a select or update/delete. It has to be explicitly set by
 * {@link #isSelect}, defaults to update. Will throw an error if the flag is not
 * appropriately set.
 * 
 * @author Aleksandr Panzin (alex from activelogic.eu)
 */
public class SQLExecutorTasklet  implements Tasklet {

	protected static Logger LOGGER = LoggerFactory.getLogger(SQLExecutorTasklet.class);

	private String sqlQuery;
	private boolean isSelect = false;
    private SessionFactory sessionFactory;
    

    protected Session getSession(){
    	return sessionFactory.getCurrentSession();
    }
	
	
	/**
	 * Build a {@link Query} object for execution by the tasklet.
	 * 
	 * @return an appropriate Hibernate {@link Query} object
	 */
	protected Query buildQuery() {
		return getSession().createSQLQuery(sqlQuery);
	}

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
		Query q = buildQuery();

		int value = -1;
		if (isSelect) {
			value = q.list().size();
		} else {
			value = q.executeUpdate();
		}

		if (value >= 0) {
			LOGGER.info("Query returned " + value);
		} else {
			LOGGER.info("Query no values ");
		}

		return RepeatStatus.FINISHED;
	}

	public void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}
	
	@Required
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}
