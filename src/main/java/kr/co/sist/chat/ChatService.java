package kr.co.sist.chat;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
	
	private final ChatMapper cm;

	public List<ChatRoomDTO> getChatRoomList(String userNo){
		return cm.selectChatRoom(userNo);
	}
	
	public List<ChatRoomDTO> getMessageList(String chatRoomNo){
		return cm.selectMessageList(chatRoomNo);
	}
	
	public int sendMessage(ChatRoomDTO cDTO) {
		return cm.insertMessage(cDTO);
	}
	
	public List<ChatRoomDTO> searchUser(String userName) {
		List<ChatRoomDTO> list=null;
		return list;
	}
	
	public int getChatCnt() {
		int cnt=0;
		return cnt;
	}
	
	
	public int createChat(ChatRoomDTO crDTO) {
		int cnt=0;
		return cnt;
	}
	
	public List<ChatRoomDTO> sortChatRoom(String chatRoomNo){
		List<ChatRoomDTO> list=null;
		return list;
	}
	
	public int leaveChat(String userNo, String chatRoomNo) {
		int cnt=0;
		return cnt;
	}
	
	public String insertFile(String content) {
		return "";
	}
	
}
