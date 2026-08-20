package kr.co.sist.inquiry;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class InquiryService {
	
	@Value("${user.upload-dir}")
	private String uploadDir;
	
	@Autowired
	private InquiryMapper im;
	
	public String createInquiry(MultipartFile mf,InquiryDTO iDTO) {
		
		String result = "문의 등록에 실패하였습니다. 잠시 후 다시 시도해 주시기 바랍니다.";
		String originalFileName = mf.getOriginalFilename();
		String ext = originalFileName.substring(originalFileName.lastIndexOf('.'));
		String fileName = UUID.randomUUID().toString().replaceAll("-", "") + ext;
		File file = new File(uploadDir + fileName);
		
		try {
			mf.transferTo(file);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		iDTO.setFiles(fileName);
		
		if (im.insertInquiry(iDTO) == 1) {
			result = "문의가 정상적으로 등록되었습니다.";
		}
		
		return result;
	}// createInquiry

}
