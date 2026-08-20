package kr.co.sist.company;

import java.sql.Date;

import org.apache.ibatis.type.Alias;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("companyDTO")
@Getter
@Builder
@ToString
public class CompanyDTO {
	private String companyName, companyTel;
	private Date inputDate;
}
