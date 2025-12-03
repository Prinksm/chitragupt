package com.example.demo.sharePrescription.dto;

import lombok.Data;

@Data
public class ShareResponseDto {
    private String message;
    private String shareLink;
    private String waUrl;    // present when frontend should open WhatsApp
    private String sentTo;   // email or phone
    private String method;   // "email" or "whatsapp"
}
