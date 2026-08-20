package kr.co.sist.title;

import org.apache.ibatis.type.Alias;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Alias("rankDTO")
@Getter
@Builder
@ToString
public class RankDTO {
	private String rankNo, rankName, companyNo;
	private int priority;
}
