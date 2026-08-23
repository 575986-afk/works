package kr.co.sist.calaudit;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CalLogMapper {
	public List<CalLogListDomain> selectAllCalLog(CalLogSearchDTO search);
	public CalLogDetailDomain selectCalLogDetail(@Param("logNo") String logNo);
}
