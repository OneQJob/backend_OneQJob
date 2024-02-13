package com.backend.oneqjob.domain.shared.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PhoneValidationService {
    public String getValidationCode() {
        String ran_str = "";
        for(int i=0; i<6; i++) {
            ran_str += (int)(Math.random()*10);
        }
        return ran_str;
    }

}