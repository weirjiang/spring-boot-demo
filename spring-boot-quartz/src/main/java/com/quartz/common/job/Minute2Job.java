package com.quartz.common.job;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class Minute2Job implements Job {
	Logger logger = LogManager.getLogger(getClass());

	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("JobName2: {}", context.getJobDetail().getKey().getName());
	}

}
