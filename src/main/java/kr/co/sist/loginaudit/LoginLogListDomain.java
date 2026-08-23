package kr.co.sist.loginaudit;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginLogListDomain {
	private String logNo;
	private String description;
	private String loginIp;
	private String status;
	private String userName;
	private String email;
    private Timestamp inputDate;
}