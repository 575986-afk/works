package kr.co.sist.group;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GroupMemberSaveDTO {

    private String groupNo;

    private List<String> userNoList;
}