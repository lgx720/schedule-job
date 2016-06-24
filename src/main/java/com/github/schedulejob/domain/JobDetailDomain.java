package com.github.schedulejob.domain;

import java.util.Set;

/**
 * 任务抽象业务类
 *
 * @author: lvhao
 * @since: 2016-6-23 20:43
 */
public class JobDetailDomain {

    // job info
    private JobDomain jobDomain;

    // trigger info
    private Set<TriggerDomain> triggerDomainSet;

    public JobDomain getJobDomain() {
        return jobDomain;
    }

    public void setJobDomain(JobDomain jobDomain) {
        this.jobDomain = jobDomain;
    }

    public Set<TriggerDomain> getTriggerDomainSet() {
        return triggerDomainSet;
    }

    public void setTriggerDomainSet(Set<TriggerDomain> triggerDomainSet) {
        this.triggerDomainSet = triggerDomainSet;
    }

    @Override
    public String toString() {
        return "JobDetailPO{" +
                "jobDomain=" + jobDomain +
                ", triggerDomainSet=" + triggerDomainSet +
                '}';
    }
}