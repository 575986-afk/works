package kr.co.sist.user.alarm;

import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Alias("alarmDomain")
@Data
public class AlarmDomain {
	private String duty;
	private Timestamp inputDate;
}
