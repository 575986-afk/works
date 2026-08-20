package kr.co.sist.inquiry;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InquiryMapper {
	public int insertInquiry(InquiryDTO iDTO);
}
