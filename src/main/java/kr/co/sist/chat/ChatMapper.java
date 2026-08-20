package kr.co.sist.chat;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMapper {

	List<ChatRoomDTO> selectChatRoom(String userNo);
	List<ChattingDTO> selectMessageList(String chatRoomNo);
	List<ChatRoomDTO> selectSearchUser(String userName);
	int selectChatCnt(ChattingDTO cDTO);
	int insertMessage(ChattingDTO cDTO);
	int insertChatRoom(ChatRoomDTO crDTO);
	List<ChatRoomDTO> sortChatRoom(String chatRoomNo);
	int deleteChatRoom(String userNo, String chatRoomNo);
	String insertFile(String content);
	
}
