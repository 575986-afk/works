package kr.co.sist.audit;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuditMapper {
	
	public AuditEmailDomain selectEmail(@Param("userNo") String userNo);

    public int insertCode(Map<String, Object> map);

    public int selectNumber(
        @Param("userNo") String userNo,
        @Param("verificationNo") String verificationNo
    );
}