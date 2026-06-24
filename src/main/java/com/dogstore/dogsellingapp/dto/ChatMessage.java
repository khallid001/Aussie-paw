package com.dogstore.dogsellingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private String sender; // "user" or "bot"
    private String message;
    private LocalDateTime timestamp;
}

