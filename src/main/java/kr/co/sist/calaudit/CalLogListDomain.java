package kr.co.sist.calaudit;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CalLogListDomain {
    private String logNo;
    private String title;
    private String duty;
    private String userName;
    private String email;
    private String calenderNo;
    private Timestamp inputDate;
}