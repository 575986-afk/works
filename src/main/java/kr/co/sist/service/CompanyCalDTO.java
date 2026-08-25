package kr.co.sist.service;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("companyCalDTO")
@Getter
@Setter
@ToString
public class CompanyCalDTO {

	private String calenderNo;
	private String title;
	private String content;
	private String startDate;
	private String startTime;
	private String endDate;
	private String endTime;

	private String companyNo, userNo, organizationNo;
}