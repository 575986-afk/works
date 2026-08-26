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

	public List<ChatRoomDTO> getChatRoomList(String userNo) {
		return cm.selectChatRoom(userNo);
	}

	public List<ChatRoomDTO> selectChatRoom(String userNo) {
		return cm.selectChatRoom(userNo);
	}

	public List<ChatRoomDTO> getMessageList(String chatRoomNo) {
		return cm.selectMessageList(chatRoomNo);
	}

	public int sendMessage(ChatRoomDTO cDTO) {
		return cm.insertMessage(cDTO);
	}

	public List<ChatRoomDTO> searchUser(String companyNo, String userNo, String keyword) {
		Map<String, Object> map = new HashMap<>();
		
		map.put("companyNo", companyNo);
		map.put("userNo", userNo);
		map.put("keyword", "%" + keyword + "%");

		return cm.selectSearchUser(map);
	}

	@Transactional
	public int createChat(String chatRoomName, List<String> userNos, String loginUserNo) {

		ChatRoomDTO crDTO = new ChatRoomDTO();
		crDTO.setChatRoomName(chatRoomName != null && !chatRoomName.isEmpty() ? chatRoomName : "새로운 채팅방");
		crDTO.setUserNo(loginUserNo);

		int result = cm.insertChatRoom(crDTO);

		if (result > 0) {
			String chatRoomNo = crDTO.getChatRoomNo();

			if (userNos == null) {
				userNos = new java.util.ArrayList<>();
			}

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

	@Transactional
	public int leaveChatRoom(String userNo, String chatRoomNo) {
		Map<String, Object> map = new HashMap<>();
		map.put("userNo", userNo);
		map.put("chatRoomNo", chatRoomNo);
		return cm.deleteChatParticipant(map);
	}

	@Transactional
	public int sendFileMessage(String chatRoomNo, String sendUser, String originalFilename) {
		ChatRoomDTO cDTO = new ChatRoomDTO();
		cDTO.setChatRoomNo(chatRoomNo);
		cDTO.setSendUser(sendUser);
		cDTO.setContent("[파일] " + originalFilename);

		return cm.insertMessage(cDTO);
	}

}
