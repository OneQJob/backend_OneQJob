package com.backend.oneqjob.domain.user.common;

import com.backend.oneqjob.entity.dto.SignUpRequestDto;

import java.util.ArrayList;
import java.util.List;

public class Helper {
    public static String checkRequired(SignUpRequestDto dto) {
        List<String> missingFields = new ArrayList<>();

        if (dto.getUserName() == null || dto.getUserName().trim().isEmpty()) missingFields.add("userName");
        if (dto.getPhoneNumber() == null || dto.getPhoneNumber().trim().isEmpty()) missingFields.add("phoneNumber");
        if (dto.getDateOfBirth() == null || dto.getDateOfBirth().trim().isEmpty()) missingFields.add("dateOfBirth");
        if (dto.getUserId() == null || dto.getUserId().trim().isEmpty()) missingFields.add("userId");
        if (dto.getUserPw() == null || dto.getUserPw().trim().isEmpty()) missingFields.add("userPw");
        if (dto.getAddress() == null) {
            missingFields.add("address");
        } else {
            if (dto.getAddress().getRoadAddress() == null || dto.getAddress().getRoadAddress().trim().isEmpty())
                missingFields.add("address.roadAddress");
            if (dto.getAddress().getSido() == null || dto.getAddress().getSido().trim().isEmpty())
                missingFields.add("address.sido");
            if (dto.getAddress().getSigungu() == null || dto.getAddress().getSigungu().trim().isEmpty())
                missingFields.add("address.sigungu");
            if (dto.getAddress().getBname() == null || dto.getAddress().getBname().trim().isEmpty())
                missingFields.add("address.bname");
        }

        return String.join(", ", missingFields);
    }

}

