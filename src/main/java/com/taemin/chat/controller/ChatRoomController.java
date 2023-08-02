package com.taemin.chat.controller;

import com.taemin.chat.dto.ChatCreateRequestDto;
import com.taemin.chat.dto.ChatCreateResponseDto;
import com.taemin.chat.dto.ChatRoom;
import com.taemin.chat.global.common.response.BaseResponse;
import com.taemin.chat.global.common.response.ResponseService;
import com.taemin.chat.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@Slf4j
public class ChatRoomController {
    // ChatService Bean 가져오기
    @Autowired
    private ChatService chatService;

    @Autowired
    private ResponseService responseService;

    // 채팅방 생성
    // 채팅을 보내면 상대를 채팅방으로 초대
    // 메시지 보내기 -> 방 생성 -> 방으로 이동
    // 초대를 수락하면 -> 방 이동
    @PostMapping("/chat/createroom")
    public BaseResponse<?> createRoom(@RequestBody ChatCreateRequestDto chatCreateRequestDto) {
        // 방 존재 여부 체크
        
        // 방 생성
        String roomId = chatService.createChatRoom(chatCreateRequestDto);
        log.info("Create chat roomID {}", roomId);

        ChatCreateResponseDto response = new ChatCreateResponseDto();
        response.setRoomId(roomId);

        return responseService.getSuccessResponse("create success", response);
    }

    // 채팅방 입장 화면
    // 재입장시 과거 메세지 가져오기?
    // NoSQL 적용하면 그때 만들자
//    @GetMapping("/chat/room/{roomId}")
//    public BaseResponse<?> roomDetail(@PathVariable("roomId") String roomId) {
//        log.info("roomId {}", roomId);
//        ChatRoom room = chatService.findRoomById(roomId);
//
//        return responseService.getSuccessResponse("enter success", room);
//    }
}
