package com.taemin.chat.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.UUID;

// STOMP를 통해 pub/sub를 사용하면 구독자 관리가 알아서 된다.
// 따라서 따로 세션 관리를 하는 코드를 작성할 필요도 없고,
// 메시지를 다른 세션의 클라이언트에게 발송하는 것도 구현할 필요 X
@Data
public class ChatRoom {
    private String roomId; // 채팅방 아이디
    private String grantorId; // 양도자 아이디
    private String assigneeId; // 양수자 아이디
    private Integer roomDealId; // 매물글 아이디

    public ChatRoom create(ChatCreateRequestDto chatCreateRequestDto) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.assigneeId = chatCreateRequestDto.getAssigneeId();
        chatRoom.roomDealId = roomDealId;

        return chatRoom;
    }
}
