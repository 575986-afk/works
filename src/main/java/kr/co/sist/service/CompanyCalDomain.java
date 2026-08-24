package kr.co.sist.service;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("companyCalDomain")
@Getter
@Setter
@ToString
public class CompanyCalDomain {

	private String calendarNo;
	private String title;
	private String startDate;
	private String startTime;
	private String endDate;
	private String endTime;

}