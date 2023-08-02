package com.taemin.chat.service;

import com.taemin.chat.dto.ChatCreateRequestDto;
import com.taemin.chat.dto.ChatRoom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@Slf4j
public class ChatService {

    // 채팅방 목록 저장용 -> DB로 변경 예정
    private Map<String, ChatRoom> chatRoomMap;

    // 채팅방 목록 저장용 -> DB로 변경 예정
    @PostConstruct
    private void init() {
        chatRoomMap = new LinkedHashMap<>();
    }

    // roomID 기준으로 채팅방 찾기
    public ChatRoom findRoomById(String roomId) {
        return chatRoomMap.get(roomId);
    }

    public String createChatRoom(ChatCreateRequestDto chatCreateRequestDto) {

        ChatRoom chatRoom = new ChatRoom().create(chatCreateRequestDto);

        // map에 채팅룸 아이디와 만들어진 채팅룸을 저장 (후에 DB에 저장)
        chatRoomMap.put(chatRoom.getRoomId(), chatRoom);

        return chatRoom.getRoomId();
    }

    // 채팅방 중복 체크


//    // 채팅방 유저 리스트에 유저 추가
//    public String addUser(String roomId, UUID userId, String nickName) {
//        ChatRoom room = chatRoomMap.get(roomId);
//        String userUUID = userId.toString();
//
//        // 아이디 중복 확인 후 userList에 추가
//        room.getUserlist().put(userUUID, nickName);
//
//        return userUUID;
//    }
//
//    // 채팅방 유저 리스트 삭제
//    public void delUser(String roomId, String userUUID) {
//        ChatRoom room = chatRoomMap.get(roomId);
//        room.getUserlist().remove(userUUID);
//    }
//
//    // 채팅방 userName 조회
//    public String getUserName(String roomId, String userUUID) {
//        ChatRoom room = chatRoomMap.get(roomId);
//        return room.getUserlist().get(userUUID);
//    }
//
//    // 채팅방 전체 userlist 조회
//    public ArrayList<String> getUserList(String roomId) {
//        ArrayList<String> list = new ArrayList<>();
//        ChatRoom room = chatRoomMap.get(roomId);
//
//        // hashmap을 for 문을 돌린 후
//        // value값만 뽑아내서 list에 저장 후 return
//        room.getUserlist().forEach((key, value)->list.add(value));
//        return list;
//    }
}
