package kr.co.sist.todoaudit;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ToDoLogDetailDomain {
    private String logNo;
    private String companyNo;
    private String title;          // 제목
    private String status;         // 상태
    private String startDate;      // 기한 (시작일)
    private String endDate;        // 기한 (종료일)
    
    // 요청자 (작성자)
    private String requesterName;  
    private String requesterEmail; 
    
    // 담당자
    private String assigneeName;   
    private String assigneeEmail;  
}