package kr.co.sist.addraudit;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AddrLogSearchDTO {

    private String startDate;
    private String endDate;
    private String duty;
    private String userName;
    private String targetName;
    private String companyNo;
}