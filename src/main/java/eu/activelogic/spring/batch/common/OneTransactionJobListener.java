package eu.activelogic.spring.batch.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

/**
 * A listener that starts the transaction at the job level.
 * 
 * Steps have to have MANDATORY transaction attribute for this to actually work.
 * 
 * @author Aleksandr Panzin (alex from activelogic.eu)
 */
public class OneTransactionJobListener implements JobExecutionListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(OneTransactionJobListener.class);
	
	private PlatformTransactionManager transactionManager;
	
	protected TransactionDefinition transactionDefinition;
	
	private boolean commitAlways = false;
	
	@Override
	public void beforeJob(JobExecution execution) {
		TransactionStatus status = transactionManager.getTransaction(transactionDefinition);
		LOGGER.debug("Starting transaction "+status);
	}
	
	@Override
	public void afterJob(JobExecution execution) {
		TransactionStatus status = transactionManager.getTransaction(transactionDefinition);
		if(commitAlways || status.isNewTransaction()){
			LOGGER.debug("Commiting transaction "+status);
			transactionManager.commit(status);
		} else {
			if(status.isRollbackOnly() || execution.getStatus() == BatchStatus.FAILED){
				LOGGER.debug("Rolling back transaction "+status);
				transactionManager.rollback(status);
			} else {
				LOGGER.debug("Commiting transaction "+status);
				transactionManager.commit(status);
			}
		}
	}

	@Required
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	@Required
	public void setTransactionDefinition(TransactionDefinition transactionDefinition) {
		this.transactionDefinition = transactionDefinition;
	}
	
	public void setCommitAlways(boolean commitAlways) {
		this.commitAlways = commitAlways;
	}
	
	public boolean isCommitAlways() {
		return commitAlways;
	}
	
}
