package kr.co.sist.addraudit;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AddrLogListDomain {
	private String logNo;
	private String duty;
	private String userName;
	private String email;
	private String targetName;
	private String targetEmail;
    private Timestamp inputDate;
}