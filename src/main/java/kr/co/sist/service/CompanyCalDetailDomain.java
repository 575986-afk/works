package kr.co.sist.service;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("companyCalDetailDomain")
@Getter
@Setter
@ToString
public class CompanyCalDetailDomain {
	private String calenderNo, title, content, creatorName, companyName;
	private String startDate, startTime, endDate, endTime;
}
