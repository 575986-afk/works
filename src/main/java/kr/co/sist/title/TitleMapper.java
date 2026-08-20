package kr.co.sist.title;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.exceptions.PersistenceException;

@Mapper
public interface TitleMapper {
	
	//position
	public List<PositionDomain> selectPosition(String companyNo);
    public int updatePositionPriority(@Param("positionNo") String positionNo, @Param("priority") int priority, @Param("companyNo") String companyNo);
    public int updatePositionName(@Param("positionNo") String positionNo, @Param("positionName") String positionName, @Param("companyNo") String companyNo);
    public int deletePosition(@Param("positionNo") String positionNo, @Param("companyNo") String companyNo);
    public int insertPosition(PositionDTO pDTO) throws PersistenceException;
    public int countUserByPosition(@Param("positionNo") String positionNo);
	
	//rank
    public List<RankDomain> selectRank(String companyNo);
    public int updateRankPriority(@Param("rankNo") String rankNo, @Param("priority") int priority, @Param("companyNo") String companyNo);
    public int updateRankName(@Param("rankNo") String rankNo, @Param("rankName") String rankName, @Param("companyNo") String companyNo);
    public int deleteRank(@Param("rankNo") String rankNo, @Param("companyNo") String companyNo);
    public int insertRank(RankDTO rDTO) throws PersistenceException;
    public int countUserByRank(@Param("rankNo") String rankNo);

}
