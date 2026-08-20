package kr.co.sist.signup;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Mapper
public interface SignupMapper {
	

	 int selectIdDup(String id) ;
	
	 int insertUserJoin(UserDTO uDTO);
	
	 int insertUserRole(UserDTO uDTO) ;
	 
//	 int insertRankPosition(UserDTO uDTO);

	 int insertCompanyInfo(UserDTO uDTO);
	
	 int insertManagerJoin(UserDTO uDTO);
	
	 int insertRole(UserDTO uDTO);
	 
//	 int insertManagerRankPosition(UserDTO uDTO);
}
