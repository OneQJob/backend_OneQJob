package com.backend.oneqjob.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
// 단순히 builder 를 사용하지 않으므로써 얻는것 ?
public class UserDTO {
    private String userName;
    private String phoneNumber;
    private String roadAddress;
    private String imgFileName;
    private String imgUrl;
    private String dateOfBirth;
    private String userId;
    private String userPw;


    private UserDTO() {
    }
    //private 생성자는 외부에서 직접 인스턴스를 생성하는 것을 방지하기 위한 것

    public static UserDTO forRegistrationRequest(String userName, String phoneNumber,
                                                 String imgFileName, String imgUrl,
                                                 String dateOfBirth, String userId, String userPw) {
        UserDTO userDTO = new UserDTO();
        userDTO.userName = userName;
        userDTO.phoneNumber = phoneNumber;
        userDTO.imgFileName = imgFileName;
        userDTO.imgUrl = imgUrl;
        userDTO.dateOfBirth = dateOfBirth;
        userDTO.userId = userId;
        userDTO.userPw = userPw;

        return userDTO;
    }
}
