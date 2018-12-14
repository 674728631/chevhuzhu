package com.zccbh.demand.controller.quartz;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class QuartzJobInfo implements Serializable {
	private static final long serialVersionUID = 352871503794872330L;
	private byte type;
	private String jobName;
	private Map<String, Object> params;
	private String cronj;
	private Date fireDate;

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public String getCronj() {
		return cronj;
	}

	public void setCronj(String cronj) {
		this.cronj = cronj;
	}

	public Date getFireDate() {
		return fireDate;
	}

	public void setFireDate(Date fireDate) {
		this.fireDate = fireDate;
		String[] cronArr = new String[7];
		for (int i = 0; i < cronArr.length; i++) {
			cronArr[i] = "";
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fireDate);
		int second = calendar.get(Calendar.SECOND);
		int minute = calendar.get(Calendar.MINUTE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);

		cronArr[0] = second + "";
		cronArr[1] = minute + "";
		cronArr[2] = hour + "";

		cronArr[3] = day + "";
		cronArr[4] = month + "";
		cronArr[5] = "?";
		cronArr[6] = year + "";

		String cron = StringUtils.join(cronArr," ").trim();
		this.setCronj(cron);
	}

	@Override
	public String toString() {
		return "QuartzJobInfo [type=" + type + ", jobName=" + jobName + ", params=" + params + ", cronj=" + cronj
				+ ", fireDate=" + fireDate + "]";
	}
}
