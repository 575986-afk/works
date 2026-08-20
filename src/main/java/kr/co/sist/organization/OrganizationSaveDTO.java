package kr.co.sist.organization;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrganizationSaveDTO {
    private String organizationNo;          // 조직 번호 (필수)
    private String organizationName;        // 조직명 (선택)
    private String organizationDescription; // 조직 설명 (선택)
    private String userNo;           // 마스터 사용자 번호 (선택)
    private String companyNo;        // 회사 번호 (필요 시)
}