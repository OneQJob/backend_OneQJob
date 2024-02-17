package com.backend.oneqjob;

import com.backend.oneqjob.companyUser.dto.CompanySignUpRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CompanySignUpTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("사업자회원가입")
    public void testSignup() throws Exception {

        CompanySignUpRequest request = CompanySignUpRequest.builder()
                .companyName("Test Company")
                .businessNumber("1234567890")
                .companyEmail("test@example.com")
                .companyPw1("password123*")
                .companyPw2("password123*")
                .build();

        String json = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/company/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())  // 201 Created 상태 코드를 기대합니다
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("successfully singUp"));
    }


    @Test
    @DisplayName("중복사업자회원가입")
    public void testDuplicateBusinessNumber() throws Exception {
        // 첫 번째 회원가입 요청
        CompanySignUpRequest request = CompanySignUpRequest.builder()
                .companyName("Test Company")
                .businessNumber("1234567890")
                .companyEmail("test@example.com")
                .companyPw1("password123*")
                .companyPw2("password123*")
                .build();

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/company/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        // 중복된 사업자 등록 번호로 회원가입을 시도하는 두 번째 요청
        CompanySignUpRequest duplicateRequest = CompanySignUpRequest.builder()
                .companyName("Duplicate Test Company")
                .businessNumber("1234567890")  // 중복되는 사업자 등록 번호
                .companyEmail("test2@example.com")
                .companyPw1("password123*")
                .companyPw2("password123*")
                .build();

        String duplicateJson = objectMapper.writeValueAsString(duplicateRequest);

        MvcResult result = mockMvc.perform(post("/company/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(duplicateJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("Duplicate input information."));

    }


    @Test
    @DisplayName("중복이메일회원가입")
    public void testDuplicateEmail() throws Exception {

        CompanySignUpRequest request = CompanySignUpRequest.builder()
                .companyName("Test Company")
                .businessNumber("1234567890")
                .companyEmail("test@example.com")
                .companyPw1("password123*")
                .companyPw2("password123*")
                .build();

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/company/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        // 중복된 이메일 회원가입 시도
        CompanySignUpRequest duplicateRequest = CompanySignUpRequest.builder()
                .companyName("Duplicate Test Company")
                .businessNumber("3216547854")  // 중복되는 사업자 등록 번호
                .companyEmail("test@example.com")
                .companyPw1("password123*")
                .companyPw2("password123*")
                .build();

        String duplicateJson = objectMapper.writeValueAsString(duplicateRequest);

        MvcResult result = mockMvc.perform(post("/company/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(duplicateJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("Duplicate input information."));
    }

    @Test
    @DisplayName("비밀번호 패턴")
    public void testPasswordPattern() throws Exception {

        CompanySignUpRequest request = CompanySignUpRequest.builder()
                .companyName("Test Company")
                .businessNumber("1234567890")
                .companyEmail("test@example.com")
                .companyPw1("password12")
                .companyPw2("password12")
                .build();

        String json = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/company/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("Invalid input information"));
    }

    @Test
    @DisplayName("비밀번호 일치")
    public void testPasswordMatch() throws Exception {

        CompanySignUpRequest request = CompanySignUpRequest.builder()
                .companyName("Test Company")
                .businessNumber("1234567890")
                .companyEmail("test@example.com")
                .companyPw1("password123*")
                .companyPw2("password1233*")
                .build();

        String json = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/company/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("Passwords do not match"));
    }


}
