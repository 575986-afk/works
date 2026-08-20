package kr.co.sist.setting;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AlarmSettingDTO {

	private int isAlarmOn, allowEventInvitation,allowAttendanceResponse
	,allowTaskAssignment,allowTaskCompletion;
	private String userNo;
}
