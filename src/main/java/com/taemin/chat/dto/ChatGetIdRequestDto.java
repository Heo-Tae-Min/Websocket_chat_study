package com.taemin.chat.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ChatGetIdRequestDto {
    @NotNull
    private UUID assigneeId;

    @NotNull
    private UUID grantorId;
}