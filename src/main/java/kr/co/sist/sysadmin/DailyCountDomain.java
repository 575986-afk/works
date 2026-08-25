package kr.co.sist.sysadmin;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** 대시보드 주간/월간 그래프용 (예: "08/06" -> 3건, "3월" -> 12건) */
@Alias("dailyCountDomain")
@Getter
@Setter
@ToString
public class DailyCountDomain {
	private String label;
	private int cnt;
}
