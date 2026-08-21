package kr.co.sist.organization;

import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("organizationDomain")
@Setter
@Getter
@ToString
public class OrganizationDomain {

    private String organizationNo, organizationName, description,organizationDesc, companyNo, userNo, leaderName;
    private Timestamp inputDate;
    private int memberCount;
}
