package eu.activelogic.spring.batch.common;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * A simple Quartz based scheduler Job starter.
 * 
 * @author Aleksandr Panzin (alex from activelogic.eu)
 */
public class BasicJobLauncherDetails extends QuartzJobBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(BasicJobLauncherDetails.class);
	
	private JobLocator jobLocator;
	private JobLauncher jobLauncher;
	private String jobName;

	@Override
	protected void executeInternal(JobExecutionContext context) {
		LOGGER.info("Quartz trigger firing with Spring Batch jobName = " + jobName);
		try {
			synchronized (BasicJobLauncherDetails.class) {
				JobParameters jobParameters = getJobParameters();
			    jobLauncher.run(jobLocator.getJob(jobName), jobParameters);
			}
		} catch (JobExecutionException e) {
			LOGGER.error("Could not execute job.", e); 
		}
	}
	
	/**
	 * Copy parameters that are of the correct type over to
	 * {@link JobParameters}.
	 * 
	 * @return a {@link JobParameters} instance
	 */
	private JobParameters getJobParameters() throws JobExecutionException {
		JobParametersBuilder builder = new JobParametersBuilder();
        return builder.toJobParameters();
	}

	// setters and getters.
	public JobLocator getJobLocator() {
		return jobLocator;
	}
	
	public void setJobLocator(JobLocator jobLocator) {
		this.jobLocator = jobLocator;
	}
	
	public JobLauncher getJobLauncher() {
		return jobLauncher;
	}
	
	public void setJobLauncher(JobLauncher jobLauncher) {
		this.jobLauncher = jobLauncher;
	}
	
	public String getJobName() {
		return jobName;
	}
	
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

}
