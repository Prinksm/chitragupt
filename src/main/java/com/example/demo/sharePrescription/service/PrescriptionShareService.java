package com.example.demo.sharePrescription.service;

import com.example.demo.sharePrescription.repository.ShareTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrescriptionShareService {
    private final ShareTokenRepository shareTokenRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

}
