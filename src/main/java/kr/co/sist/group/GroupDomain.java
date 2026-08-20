package kr.co.sist.group;

import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("groupDomain")
@Setter
@Getter
@ToString
public class GroupDomain {

    private String groupNo, groupName, groupDesc, companyNo, userNo, leaderName;
    private Timestamp inputDate;
    private int memberCount;
}
