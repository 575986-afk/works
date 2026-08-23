package kr.co.sist.aop;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AopMapper {
	
	public void insertTodoLog(@Param("duty") String duty, 
            @Param("representativeNo") String representativeNo, 
            @Param("todoNo") String todoNo, 
            @Param("userNo") String userNo);
	
	void insertLoginLog(
	        @Param("description") String description, 
	        @Param("loginIp") String loginIp, 
	        @Param("connectionStatus") String connectionStatus, 
	        @Param("userNo") String userNo,
	        @Param("attemptId") String attemptId
	    );
}
