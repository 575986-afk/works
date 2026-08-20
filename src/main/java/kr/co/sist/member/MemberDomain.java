package kr.co.sist.member;

import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("memberDomain")
@Getter
@Setter
@ToString
public class MemberDomain {

    private String userNo;
    private String userName;
    private String userId;
    private String email;
    private String phone;
    private String status;
    private String profileImage;
    private String companyNo;
    
    private String positionNo;
    private String positionName;
    private String rankNo;
    private String rankName;
    
    private String organizationNo;
    private String organizationName;
    
    private String workplace;
    private String jobtask;
    
    private List<String> organizationNoList;
    private List<String> organizationNameList;
}