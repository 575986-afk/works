package kr.co.sist.group;

import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Alias("groupDTO")
@Getter
@Builder
@ToString
public class GroupDTO {
	private String groupNo, groupName, groupDescription, companyNo, userNo;
	private Timestamp inputDate;
}
