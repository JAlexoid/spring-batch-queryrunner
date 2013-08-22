package eu.activelogic.spring.batch.common;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class OneTransactionJobListenerTest {

	OneTransactionJobListener listener;
	
	private PlatformTransactionManager transactionManager;
	private TransactionStatus status;
	
	protected TransactionDefinition transactionDefinition;
	
	private JobExecution jobExecution;
	
	@Before
	public void setUp() throws Exception {
		transactionManager = mock(PlatformTransactionManager.class);
		status = mock(TransactionStatus.class);
		transactionDefinition = new DefaultTransactionDefinition();
	
		jobExecution = new JobExecution(1l);
		
		listener = new OneTransactionJobListener();
		listener.setTransactionDefinition(transactionDefinition);
		listener.setTransactionManager(transactionManager);
	}

	@Test
	public final void testBeforeJob() {
		
		when(transactionManager.getTransaction(transactionDefinition)).thenReturn(status);
		
		listener.beforeJob(jobExecution);
		
		verify(transactionManager).getTransaction(transactionDefinition);
		
	}

	@Test
	public final void testAfterJob() {
		
		when(status.isNewTransaction()).thenReturn(false);
		when(status.isRollbackOnly()).thenReturn(false);
		
		when(transactionManager.getTransaction(transactionDefinition)).thenReturn(status);

		
		jobExecution.setStatus(BatchStatus.COMPLETED);	
		
		listener.afterJob(jobExecution);
		
		verify(transactionManager).commit(status);
		
	}
	
	@Test
	public final void testAfterJobCommitAlways() {
		when(transactionManager.getTransaction(transactionDefinition)).thenReturn(status);
		listener.setCommitAlways(true);

		listener.afterJob(jobExecution);
		
		verify(transactionManager).commit(status);
		
	}
	
	@Test
	public final void testAfterJobNoTX() {
		when(transactionManager.getTransaction(transactionDefinition)).thenReturn(status);
		when(status.isNewTransaction()).thenReturn(true);

		listener.afterJob(jobExecution);

		verify(transactionManager).commit(status);
	}
	
	@Test
	public final void testAfterJobFail() {
		when(transactionManager.getTransaction(transactionDefinition)).thenReturn(status);
		when(status.isNewTransaction()).thenReturn(false);
		when(status.isRollbackOnly()).thenReturn(false);
		
		jobExecution.setStatus(BatchStatus.FAILED);
		
		
		listener.afterJob(jobExecution);
		
		verify(transactionManager).rollback(status);
	}
	
	@Test
	public final void testAfterJobRollback() {
		when(transactionManager.getTransaction(transactionDefinition)).thenReturn(status);
		when(status.isNewTransaction()).thenReturn(false);
		when(status.isRollbackOnly()).thenReturn(true);
		
		jobExecution.setStatus(BatchStatus.COMPLETED);
		
		
		listener.afterJob(jobExecution);
		
		verify(transactionManager).rollback(status);
	}

}
