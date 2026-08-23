package kr.co.sist.calaudit;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CalLogSearchDTO {

    private String startDate;
    private String endDate;
    private String title;
    private String duty;
    private String userName;
    private String companyNo;
}