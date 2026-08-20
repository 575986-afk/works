package kr.co.sist.organization;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("organizationRangeDTO")
@Setter
@Getter
@ToString
public class RangeDTO {

    private String keyword;

}
