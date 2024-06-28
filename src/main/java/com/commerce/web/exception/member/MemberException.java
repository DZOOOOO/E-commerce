package com.commerce.web.exception.member;

public class MemberException extends RuntimeException {
    public MemberException(MemberExceptionCode message) {
        super(message.getMessage());
    }
}
