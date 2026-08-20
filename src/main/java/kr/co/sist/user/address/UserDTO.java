package kr.co.sist.user.address;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("addressUserDTO")
public class UserDTO {
	private String userNo, targetNo;
}
