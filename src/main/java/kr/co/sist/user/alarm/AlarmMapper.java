package kr.co.sist.user.alarm;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlarmMapper {
	public List<AlarmDomain> selectAlarmList(String userNo);
}
