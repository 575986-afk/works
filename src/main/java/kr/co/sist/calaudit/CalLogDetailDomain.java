package kr.co.sist.calaudit;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CalLogDetailDomain {
    private String logNo;
    private String title;          // 제목
    private String startDate;      // 기한 (시작일)
    private String endDate;        // 기한 (종료일)
    
}