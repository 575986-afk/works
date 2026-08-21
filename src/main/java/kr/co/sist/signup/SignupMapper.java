package kr.co.sist.signup;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Mapper
public interface SignupMapper {
	

	 int selectIdDup(String id) ;
	
	 int insertUserJoin(UserDTO uDTO);
	
	 int insertUserRole(UserDTO uDTO) ;
	 
	 int insertUserChat(UserDTO uDTO);

	 int insertCompanyInfo(UserDTO uDTO);
	
	 int insertManagerJoin(UserDTO uDTO);
	
	 int insertRole(UserDTO uDTO);
	 
	 int insertManagerChat(UserDTO uDTO);
}
