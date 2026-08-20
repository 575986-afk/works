package kr.co.sist.title;

import org.apache.ibatis.type.Alias;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Alias("positionDTO")
@Getter
@Builder
@ToString
public class PositionDTO {
	private String positionNo, positionName, companyNo;
	private int priority;
}
