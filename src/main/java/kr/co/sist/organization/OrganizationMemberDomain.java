package kr.co.sist.organization;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("organizationMemberDomain")
@Getter
@Setter
@ToString
public class OrganizationMemberDomain {

    private String organizationNo;
    private String userNo;
    private String userName;
    private String email;

    private String rankName;
    private String positionName;

    private int isLeader;
}
