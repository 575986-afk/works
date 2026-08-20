package kr.co.sist.chat;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {
	
	private final ChatService cs;

	@GetMapping("/chatting")
	public String chatting() {
		
		cs.getChatRoomList(null);
		
		return "works/chat/chatting";
	}
	
	public String chatRoom(String chatRoomNo, Model model) {
		return "";
	}
	
	public String searchUser(String userName) {
		return "";
	}
	
	public String chatCnt(String userNo) {
		return "";
	}
	
	public String createChatRoom() {
		return "";
	}
	
	public String sortChatRoom() {
		return "";
	}
	
	public String leaveChatRoom(String chatRoomNo) {
		return "";
	}
	
	public String attachFile() {
		return "";
	}
}
