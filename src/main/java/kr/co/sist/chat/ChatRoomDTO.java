package kr.co.sist.chat;

import java.security.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChatRoomDTO {

	private String chatRoomNo, chatRoomName, userNo, lastMessage;
	private int chatType;
	private Timestamp inputDate, lastTime;
	
}