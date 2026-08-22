package kr.co.sist.chat;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMapper {

	List<ChatRoomDTO> selectChatRoom(String userNo);
	List<ChatRoomDTO> selectMessageList(String chatRoomNo);
	int insertMessage(ChatRoomDTO cDTO);
	
	List<ChatRoomDTO> selectSearchUser(String userName);
	int selectChatCnt(ChatRoomDTO cDTO);
	int insertChatRoom(ChatRoomDTO cDTO);
	List<ChatRoomDTO> sortChatRoom(String chatRoomNo);
	int deleteChatRoom(String userNo, String chatRoomNo);
	String insertFile(String content);
	
}
