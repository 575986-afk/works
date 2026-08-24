package kr.co.sist.service;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("searchCompanyCalDTO")
@Getter
@Setter
@ToString
public class SearchCompanyCalDTO {
	private String companyNo;
	private String year;
	private String month;
}
