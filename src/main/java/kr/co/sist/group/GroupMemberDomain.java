package kr.co.sist.group;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("groupMemberDomain")
@Getter
@Setter
@ToString
public class GroupMemberDomain {

    private String groupNo;
    private String userNo;
    private String userName;
    private String email;

    private String rankName;
    private String positionName;

    private int isLeader;
}
