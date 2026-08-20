package kr.co.sist.inquiry;

import org.apache.ibatis.type.Alias;

import lombok.Builder;
import lombok.Data;

@Alias("InquiryDTO")
@Data
public class InquiryDTO {
	private String title, content, files, userNo;

}
