package com.taemin.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ChatCreateRequestDto {
    @NotNull
    private String assigneeId;

    @NotNull
    private Integer roomDealId;
}
