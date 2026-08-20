package kr.co.sist.title;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TitleService {
	
	@Autowired(required = false)
	private TitleMapper tm;
	
	//position
	public List<PositionDomain> getPosition(String companyNo){
		List<PositionDomain> list=tm.selectPosition(companyNo);
		return list;
	}
	
	@Transactional
    public void changePositionPriority(List<String> positionNoList, String companyNo) {
        for (int i = 0; i < positionNoList.size(); i++) {
            String positionNo = positionNoList.get(i);
            int priority = i + 1; // 1, 2, 3... 순서대로 부여
            tm.updatePositionPriority(positionNo, priority, companyNo);
        }
    }
	
	public boolean changePositionName(String positionNo, String positionName, String companyNo) {
        return tm.updatePositionName(positionNo, positionName, companyNo) > 0;
    }
	
	public String deletePosition(String positionNo, String companyNo) {
	    int userCnt = tm.countUserByPosition(positionNo);
	    if (userCnt > 0) {
	        return "사용자가 있어 직책을 삭제할 수 없습니다.";
	    }
	    return tm.deletePosition(positionNo, companyNo) == 1
	            ? "success"
	            : "fail";
	}
	
	public boolean createPosition(PositionDTO pDTO) {
		return tm.insertPosition(pDTO)>0;
	}
	
	@Transactional
	public String savePosition(List<TitleSaveDTO> saveList, String companyNo) {
	    List<PositionDomain> originalList = tm.selectPosition(companyNo);

	    boolean deleteBlocked = false;
	    // 삭제
	    for (PositionDomain original : originalList) {
	        boolean exists = false;
	        for (TitleSaveDTO save : saveList) {
	            if (save.getNo() != null
	                    && save.getNo().equals(original.getPositionNo())) {
	                exists = true;
	                break;
	            }
	        }

	        if (!exists) {
	            int userCnt = tm.countUserByPosition(original.getPositionNo());
	            if (userCnt == 0) {
	                tm.deletePosition(
	                    original.getPositionNo(),
	                    companyNo
	                );
	            } else {
	                deleteBlocked = true;
	            }
	        }
	    }

	    // 기존 수정 + 신규 추가
	    for (TitleSaveDTO save : saveList) {
	        if (save.getNo() == null || save.getNo().isEmpty()) {
	            PositionDTO pDTO = PositionDTO.builder()
	                    .positionName(save.getName())
	                    .companyNo(companyNo)
	                    .priority(save.getPriority())
	                    .build();
	            tm.insertPosition(pDTO);
	        } else {
	            tm.updatePositionName(
	                save.getNo(),
	                save.getName(),
	                companyNo
	            );
	        }
	    }

	    // DB 재조회
	    List<PositionDomain> currentList = tm.selectPosition(companyNo);

	    // priority 재설정
	    for (TitleSaveDTO save : saveList) {
	        String positionNo = save.getNo();
	        if (positionNo == null || positionNo.isEmpty()) {
	            for (PositionDomain current : currentList) {
	                if (current.getPositionName().equals(save.getName())) {
	                    positionNo = current.getPositionNo();
	                    break;
	                }
	            }
	        }

	        if (positionNo != null) {
	            tm.updatePositionPriority(
	                positionNo,
	                save.getPriority(),
	                companyNo
	            );
	        }
	    }

	    return deleteBlocked
	            ? "사용자가 사용 중인 직책은 삭제할 수 없습니다."
	            : "success";
	}
	
	//rank
	public List<RankDomain> getRank(String companyNo){
		List<RankDomain> list=tm.selectRank(companyNo);
		return list;
	}
	
	@Transactional
    public void changeRankPriority(List<String> rankNoList, String companyNo) {
        for (int i = 0; i < rankNoList.size(); i++) {
            String rankNo = rankNoList.get(i);
            int priority = i + 1;
            tm.updateRankPriority(rankNo, priority, companyNo);
        }
    }
	
	public boolean changeRankName(String rankNo, String rankName, String companyNo) {
        return tm.updateRankName(rankNo, rankName, companyNo) > 0;
    }
	
	public String deleteRank(String rankNo, String companyNo) {
	    int userCnt = tm.countUserByRank(rankNo);
	    if (userCnt > 0) {
	        return "사용자가 있어 직급을 삭제할 수 없습니다.";
	    }
	    return tm.deleteRank(rankNo, companyNo) == 1
	            ? "success"
	            : "fail";
	}
	
	public boolean createRank(RankDTO rDTO) {
		return tm.insertRank(rDTO)>0;
	}
	
	@Transactional
	public String saveRank(List<TitleSaveDTO> saveList, String companyNo) {
	    List<RankDomain> originalList = tm.selectRank(companyNo);
	    boolean deleteBlocked = false;

	    // 삭제
	    for (RankDomain original : originalList) {
	        boolean exists = false;
	        for (TitleSaveDTO save : saveList) {
	            if (save.getNo() != null
	                    && save.getNo().equals(original.getRankNo())) {
	                exists = true;
	                break;
	            }
	        }

	        if (!exists) {
	            int userCnt = tm.countUserByRank(original.getRankNo());
	            if (userCnt == 0) {
	                tm.deleteRank(
	                    original.getRankNo(),
	                    companyNo
	                );
	            } else {
	                deleteBlocked = true;
	            }
	        }
	    }

	    // 기존 수정 + 신규 추가
	    for (TitleSaveDTO save : saveList) {
	        if (save.getNo() == null || save.getNo().isEmpty()) {
	            RankDTO rDTO = RankDTO.builder()
	                    .rankName(save.getName())
	                    .companyNo(companyNo)
	                    .priority(save.getPriority())
	                    .build();
	            tm.insertRank(rDTO);

	        } else {
	            tm.updateRankName(
	                save.getNo(),
	                save.getName(),
	                companyNo
	            );
	        }
	    }

	    // DB 재조회
	    List<RankDomain> currentList = tm.selectRank(companyNo);

	    // priority 재설정
	    for (TitleSaveDTO save : saveList) {
	        String rankNo = save.getNo();
	        if (rankNo == null || rankNo.isEmpty()) {
	            for (RankDomain current : currentList) {
	                if (current.getRankName().equals(save.getName())) {
	                    rankNo = current.getRankNo();
	                    break;
	                }
	            }
	        }

	        if (rankNo != null) {
	            tm.updateRankPriority(
	                rankNo,
	                save.getPriority(),
	                companyNo
	            );
	        }
	    }

	    return deleteBlocked
	            ? "사용자가 사용 중인 직급은 삭제할 수 없습니다."
	            : "success";
	}
}
