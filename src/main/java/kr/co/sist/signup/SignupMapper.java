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
	 
	 int insertRank1( UserDTO uDTO);
	 int insertRank2( UserDTO uDTO);
	 int insertRank3( UserDTO uDTO);
	 int insertRank4( UserDTO uDTO);
	 
	 int insertPosition1( UserDTO uDTO);
	 int insertPosition2( UserDTO uDTO);
	 int insertPosition3( UserDTO uDTO);
	 int insertPosition4( UserDTO uDTO);
	
	 int insertManagerJoin(UserDTO uDTO);
	
	 int insertRole(UserDTO uDTO);
	 
	 int insertManagerChat(UserDTO uDTO);
}
