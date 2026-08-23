package kr.co.sist.loginaudit;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginLogSearchDTO {

    private String startDate;
    private String endDate;
    private String loginIp;
    private String userName;
    private String companyNo;
}