package com.taemin.chat.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ChatEnterResponseDto {

    @NotNull
    private String roomId;

    @NotNull
    private String userId;
}
