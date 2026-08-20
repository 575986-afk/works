package kr.co.sist.chat;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {

	@GetMapping("/chatting")
	public String chatting() {
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
