package kr.co.sist.findPw;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Map;

@Mapper
public interface FindPwMapper {

    String selectUserPw(@Param("userId") String userId, @Param("email") String email);

    int insertCode(Map<String, Object> map);

    int selectVerification(@Param("verificationNo") String verificationNo, @Param("userNo") String userNo);

    int updatePw(@Param("userNo") String userNo, @Param("newPw") String newPw);
}