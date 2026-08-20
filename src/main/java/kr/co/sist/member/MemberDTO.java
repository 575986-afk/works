package kr.co.sist.member;

import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("memberDTO")
@Getter
@Setter
@ToString
public class MemberDTO {

	private String userNo;
    private String userName;
    private String email;
    private String phone;
    private String workplace;
    private String jobtask;
    private String companyNo;
    
    private String rankNo;
    private String positionNo;

    private String organizationNo;
    private List<String> organizationNos;
}