package kr.co.sist.user.main;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MainMapper {
	public UserDomain selectUserInfo(String userNo);

}
