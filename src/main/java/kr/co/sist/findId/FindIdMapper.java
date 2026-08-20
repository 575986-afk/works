package kr.co.sist.findId;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface FindIdMapper {
	

	String selectFindId(@Param("name") String name, @Param("email")String email) ;
		
	
}
