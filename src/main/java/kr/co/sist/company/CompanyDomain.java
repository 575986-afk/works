package kr.co.sist.company;

import java.sql.Date;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("companyDomain")
@Setter
@Getter
@ToString
public class CompanyDomain {
	private String companyNo, companyName, companyTel;
	private Date inputDate;
}
