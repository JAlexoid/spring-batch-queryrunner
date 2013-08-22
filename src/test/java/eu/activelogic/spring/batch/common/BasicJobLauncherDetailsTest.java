package eu.activelogic.spring.batch.common;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRestartException;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

public class BasicJobLauncherDetailsTest {

	BasicJobLauncherDetails launcher;

	private JobLocator jobLocator;
	private JobLauncher jobLauncher;
	private Job job;
	JobExecutionContext jec;

	@Before
	public void setUp() throws Exception {
		jobLocator = mock(JobLocator.class);
		jobLauncher = mock(JobLauncher.class);
		job = mock(Job.class);
		jec = mock(JobExecutionContext.class);

		launcher = new BasicJobLauncherDetails();
		launcher.setJobName("JOB");
		launcher.setJobLauncher(jobLauncher);
		launcher.setJobLocator(jobLocator);
	}

	@Test
	public final void testExecuteInternalJobExecutionContext() throws Exception {
		when(jobLocator.getJob("JOB")).thenReturn(job);
		when(jobLauncher.run(any(Job.class), any(JobParameters.class))).thenReturn(null);

		launcher.executeInternal(jec);
		verify(jobLauncher).run(any(Job.class), any(JobParameters.class));
	}

	@Test
	public final void testExecuteInternalJobExecutionContextJobError()
			throws Exception {
		when(jobLocator.getJob("JOB")).thenReturn(job);
		when(jobLauncher.run(any(Job.class), any(JobParameters.class))).thenThrow(new JobRestartException(""));

		launcher.executeInternal(jec);
		
		verify(jobLauncher).run(any(Job.class), any(JobParameters.class));
	}

}
