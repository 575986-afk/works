package kr.co.sist.organization;

import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Alias("organizationDTO")
@Getter
@Builder
@ToString
public class OrganizationDTO {
	private String organizationNo, organizationName, organizationDescription, companyNo, userNo;
	private Timestamp inputDate;
}
