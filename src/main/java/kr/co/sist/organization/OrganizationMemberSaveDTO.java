package kr.co.sist.organization;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrganizationMemberSaveDTO {

    private String organizationNo;

    private List<String> userNoList;
}