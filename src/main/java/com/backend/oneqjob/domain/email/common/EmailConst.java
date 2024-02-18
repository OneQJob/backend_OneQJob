package com.backend.oneqjob.domain.email.common;

public interface EmailConst {

    String TRANSMISSION_SESSION = "email-verification-code";
    String VERIFICATION_SESSION = "is-verification-success";

    String SUBJECT = "[One Q Job] 이메일 인증 코드 발송";
    String TEXT_TEMPLATE = "인증코드를 확인하시고 이메일 인증을 완료해 주세요.\n"
                    + "아래의 인증코드를 정확히 입력해 주시기 바랍니다.\n\n"
                    + "이메일 인증 코드 : ";
}
