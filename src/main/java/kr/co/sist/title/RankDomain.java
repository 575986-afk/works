package kr.co.sist.title;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("rankDomain")
@Setter
@Getter
@ToString
public class RankDomain {
	private String rankName, rankNo;
	private int priority;
}
