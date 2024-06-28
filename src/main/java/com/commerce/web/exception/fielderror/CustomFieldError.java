package com.commerce.web.exception.fielderror;

import lombok.*;
import org.springframework.validation.BindingResult;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class CustomFieldError {

    private String field;
    private String message;

    // 필드에러 정보 출력
    public static List<CustomFieldError> getFieldErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .filter(e -> e.getCode() != null && e.getDefaultMessage() != null)
                .map(e -> CustomFieldError.builder()
                        .field(e.getField())
                        .message(e.getDefaultMessage())
                        .build())
                .toList();
    }

    @Override
    public String toString() {
        return this.message;
    }

}
