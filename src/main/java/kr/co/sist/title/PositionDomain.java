package kr.co.sist.title;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("positionDomain")
@Setter
@Getter
@ToString
public class PositionDomain {
	private String positionName, positionNo;
	private int priority;
}
