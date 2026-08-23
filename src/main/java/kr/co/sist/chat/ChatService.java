package kr.co.sist.chat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	public List<ChatRoomDTO> searchUser(String companyNo, String userNo, String keyword) {
	    Map<String, Object> map = new HashMap<>();
	    map.put("companyNo", companyNo);
	    map.put("myUserNo", userNo);
	    map.put("keyword", keyword);
	    
	    return cm.selectSearchUser(map);
	}
	
	
	@Transactional
	public int createChat(String chatRoomName, List<String> userNos, String loginUserNo) {
		ChatRoomDTO crDTO = new ChatRoomDTO();
		crDTO.setChatRoomName(chatRoomName != null && !chatRoomName.isEmpty() ? chatRoomName : "새로운 채팅방");
		
		int result = cm.insertChatRoom(crDTO);
		
		if (result > 0) {
			String chatRoomNo = crDTO.getChatRoomNo();
			
			if (!userNos.contains(loginUserNo)) {
				userNos.add(loginUserNo);
			}
			
			for (String userNo : userNos) {
				Map<String, Object> map = new HashMap<>();
				map.put("chatRoomNo", chatRoomNo);
				map.put("userNo", userNo);
				cm.insertChatParticipant(map);
			}
		}
		
		return result;
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
