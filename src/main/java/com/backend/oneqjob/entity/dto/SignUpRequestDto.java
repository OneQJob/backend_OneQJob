package com.backend.oneqjob.entity.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {
    private String userName;
    private String phoneNumber;
    private String imgFileName;
    private String imgUrl;
    private String dateOfBirth;
    private String userId;
    private String userPw;
    private AddressDto address;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressDto {
        private String roadAddress;
        private String sido;
        private String sigungu;
        private String bname;
        private String bname1;
        private String detailAddress;

    }
}
