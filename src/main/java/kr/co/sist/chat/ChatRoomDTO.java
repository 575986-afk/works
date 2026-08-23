package kr.co.sist.chat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChatRoomDTO {
    private String chatRoomNo;
    private String chatRoomName;
    private String lastMessage;
    private String lastTime; 

    private String chattingNo;
    private String content;
    private String sendUser;
    private String sendUserName;
    private String sendDate; 
    private String userNo; 
    
    private String companyName; 
    private String companyNo; 
    
}