package kr.co.sist.chat;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMapper {

	List<ChatRoomDTO> selectChatRoom(String userNo);
	List<ChatRoomDTO> selectMessageList(String chatRoomNo);
	int insertMessage(ChatRoomDTO cDTO);
	int insertChatRoom(ChatRoomDTO cDTO);
	int insertChatParticipant(Map<String, Object> map);
	List<ChatRoomDTO> selectSearchUser(Map<String, Object> map);
	int deleteChatParticipant(Map<String, Object> map);
	
	List<ChatRoomDTO> sortChatRoom(String chatRoomNo);
	String insertFile(String content);
	
}
