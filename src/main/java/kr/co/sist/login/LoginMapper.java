package kr.co.sist.login;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.sist.signup.UserDTO;

@Mapper
public interface LoginMapper {


	UserDTO selectLogin(@Param("userId") String userId);

}
