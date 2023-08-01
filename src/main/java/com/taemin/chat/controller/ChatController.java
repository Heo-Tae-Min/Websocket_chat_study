package com.taemin.chat.controller;

import com.taemin.chat.dto.ChatDTO;
import com.taemin.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.ArrayList;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {
    private final SimpMessageSendingOperations template;

    @Autowired
    ChatService service;

    // MessageMapping을 통해 webSocket으로 들어오는 메시지를 발신 처리한다.
    // 이때 클라이언트에서는 /pub/chat/message로 요청하게 되고 이것을 controller가 받아서 처리한다.
    // 처리가 완료되면 /sub/chat/room/roomId로 메시지가 전송된다.
    @MessageMapping("/chat/enterUser")
    public void enterUser(@Payload ChatDTO chat, SimpMessageHeaderAccessor headerAccessor) {
        // 채팅방 유저 + 1
        service.plusUserCnt(chat.getRoomId());

        // 채팅방에 유저 추가 및 UserUUID 반환
        String userUUID = service.addUser(chat.getRoomId(), chat.getSender());

        // 반환 결과를 socket session에 userUUID로 저장
        headerAccessor.getSessionAttributes().put("userUUID", userUUID);
        headerAccessor.getSessionAttributes().put("roomId", chat.getRoomId());

        chat.setMessage(chat.getSender() + " 님 입장!!");
        template.convertAndSend("/sub/chat/room/" + chat.getRoomId(), chat);
    }

    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload ChatDTO chat) {
        log.info("CHAT {}", chat);
        chat.setMessage(chat.getMessage());
        template.convertAndSend("/sub/chat/room/" + chat.getRoomId(), chat);
    }

    // 유저 퇴장 시에는 EventListener를 통해 유저 퇴장을 확인
    @EventListener
    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
        log.info("DisConnEvent {}", event);

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // stomp 세션에 있던 uuid와 roomId를 확인해서 채팅방 유저 리스트와 room에서 해당 유저를 삭제
        String userUUID = (String) headerAccessor.getSessionAttributes().get("userUUID");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");

        log.info("headAccessor {}", headerAccessor);

        // 채팅방 유저 -1
        service.minusUserCnt(roomId);

        // 채팅방 유저 리스트에서 UUID 유저 닉네임 조회 및 리스트에서 유저 삭제
        String username = service.getUserName(roomId, userUUID);
        service.delUser(roomId, userUUID);

        if(username != null) {
            log.info("User Disconnected : " + username);

            // builder 어노테이션 활용
            ChatDTO chat = ChatDTO.builder()
                    .type(ChatDTO.MessageType.LEAVE)
                    .sender(username)
                    .message(username + " 님 퇴장!!")
                    .build();

            template.convertAndSend("/sub/chat/room/" + roomId, chat);
        }
    }

    // 채팅에 참여한 유저 리스트 반환
    @GetMapping("/chat/userlist")
    @ResponseBody
    public ArrayList<String> userList(String roomId) {
        return service.getUserList(roomId);
    }

    // 채팅에 참여한 유저 닉네임 중복 확인
    @GetMapping("/chat/duplicateName")
    @ResponseBody
    public String isDuplicateName(@RequestParam("roomId") String roomId, @RequestParam("username") String username) {
        String userName = service.isDuplicateName(roomId, username);
        log.info("동작 확인 {}", userName);

        return userName;
    }
}
