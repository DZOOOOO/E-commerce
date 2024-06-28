package com.commerce.web.exception.member;

import lombok.Getter;

@Getter
public enum MemberExceptionCode {
    NOT_FOUND(Message.NOT_FOUND),
    NOT_VALID(Message.NOT_VALID),
    MEMBER_ALREADY(Message.MEMBER_ALREADY),
    PASSWORD_ERROR(Message.PASSWORD_ERROR);

    private final String message;

    MemberExceptionCode(String message) {
        this.message = message;
    }

    static class Message {
        private final static String NOT_FOUND = "없는 회원입니다.";
        private final static String NOT_VALID = "유효한 요청이 아닙니다.";
        private final static String MEMBER_ALREADY = "이미 존재하는 회원";
        private final static String PASSWORD_ERROR = "비밀번호 오류;";

    }
}
