package kr.co.sist.group;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("rangeDTO")
@Setter
@Getter
@ToString
public class RangeDTO {

    private String keyword;

}
