package com.taemin.chat.dto;

import com.taemin.chat.domain.ChatDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ChatSendRequestDto {

    @NotNull
    private ChatDTO chat;
}
