package kr.co.sist.group;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GroupMapper {

	// 그룹 검색
	public List<GroupDomain> selectGroup(@Param("companyNo") String companyNo, @Param("keyword") String keyword);

	// 그룹 전체 조회
	public List<GroupDomain> selectAllGroup(@Param("companyNo") String companyNo);

	// 그룹 추가
	public int insertGroup(GroupDTO groupDTO);
	//그룹 추가시 그룹멤버추가
	public int insertGroupMember(@Param("groupNo") String groupNo, @Param("userNo") String userNo);

	// 기존 그룹에 멤버 추가
    // 그룹 멤버 수정에서 사용
    public int insertGroupMemberForEdit(@Param("groupNo") String groupNo,@Param("userNo") String userNo);
	
	// 그룹 상세 정보 조회
	public GroupDomain selectGroupDetail(@Param("groupNo") String groupNo, @Param("companyNo") String companyNo);

	// 그룹 구성원 조회
	public List<GroupMemberDomain> selectGroupMember(@Param("groupNo") String groupNo);

	// 그룹 구성원 삭제
	public int deleteGroupMember(@Param("groupNo") String groupNo,@Param("userNo") String userNo);

	// 그룹장 변경 - GROUPSMEMBER
	public int updateGroupMemberLeader(@Param("groupNo") String groupNo, @Param("userNo") String userNo);

	// 그룹장 변경 - GROUPS
	public int updateGroupLeader(@Param("groupNo") String groupNo, @Param("userNo") String userNo);

	// 그룹 삭제 전 구성원 전체 삭제
	public int deleteGroupMemberAll(@Param("groupNo") String groupNo);

	// 그룹 삭제
	public int deleteGroup(@Param("groupNo") String groupNo);
	
	//그룹 수정
	public int updateGroup(GroupDTO gDTO);

	// 그룹/그룹마스터 수정용
    // GROUPS의 user_no 변경
    public int updateGroupLeaderForEdit(@Param("groupNo") String groupNo,@Param("companyNo") String companyNo,@Param("userNo") String userNo);

    // 그룹/그룹마스터 수정용
    // GROUPSMEMBER의 is_leader 변경
    public int updateGroupMemberLeaderForEdit(@Param("groupNo") String groupNo,@Param("userNo") String userNo);

	
}