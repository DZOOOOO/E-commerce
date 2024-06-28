package com.commerce.web.member.dto.response.message;

import lombok.Getter;

@Getter
public enum MemberMessage {
    MAIL_SEND_OK(Message.MAIL_SEND_OK),
    MAIL_OK_AUTH(Message.MAIL_OK_AUTH),
    MAIL_ALREADY_AUTH(Message.MAIL_ALREADY_AUTH),
    MEMBER_JOIN_OK(Message.MEMBER_JOIN_OK),
    MAIL_NOT_AUTH(Message.MAIL_NOT_AUTH),
    MEMBER_UPDATE_OK(Message.MEMBER_UPDATE_OK);

    private final String message;

    MemberMessage(String message) {
        this.message = message;
    }

    static class Message {
        private final static String MAIL_SEND_OK = "메일 방송완료.";
        private final static String MAIL_OK_AUTH = "메일이 인증되었습니다.";
        private final static String MAIL_ALREADY_AUTH = "이미 인증된 메일입니다.";
        private final static String MEMBER_JOIN_OK = "회원가입 성공.";
        private final static String MAIL_NOT_AUTH = "이메일 인증 후 다시 시도해주세요.";
        private final static String MEMBER_UPDATE_OK = "수정완료.";
    }
}
