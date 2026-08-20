package kr.co.sist.group;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GroupSaveDTO {
    private String groupNo;          // 그룹 번호 (필수)
    private String groupName;        // 그룹명 (선택)
    private String groupDescription; // 그룹 설명 (선택)
    private String userNo;           // 마스터 사용자 번호 (선택)
    private String companyNo;        // 회사 번호 (필요 시)
}