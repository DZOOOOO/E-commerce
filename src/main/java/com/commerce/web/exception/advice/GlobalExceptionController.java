package com.commerce.web.exception.advice;

import com.commerce.web.exception.fielderror.CustomFieldError;
import com.commerce.web.exception.member.MemberException;
import com.commerce.web.exception.order.OrderException;
import com.commerce.web.exception.product.ProductException;
import com.commerce.web.exception.response.ExceptionResponse;
import com.commerce.web.exception.wishlist.WishlistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionController {

    // Member Exception
    @ExceptionHandler(MemberException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse memberErrorResponse(MemberException e) {
        log.error("[Member Exception] ", e);
        return new ExceptionResponse(e.getMessage());
    }

    // Order Exception
    @ExceptionHandler(OrderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse orderErrorResponse(OrderException e) {
        log.error("[Order Exception] ", e);
        return new ExceptionResponse(e.getMessage());
    }

    // Product Exception
    @ExceptionHandler(ProductException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse productErrorResponse(ProductException e) {
        log.error("[Product Exception] ", e);
        return new ExceptionResponse(e.getMessage());
    }

    // wishlist Exception
    @ExceptionHandler(WishlistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse wishlistErrorResponse(WishlistException e) {
        log.error("[WishList Exception] ", e);
        return new ExceptionResponse(e.getMessage());
    }

    // Field Exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse fieldErrorResponse(MethodArgumentNotValidException e) {
        List<CustomFieldError> fieldErrors = CustomFieldError.getFieldErrors(e.getBindingResult());
        log.error("[Field Exception] = {}", fieldErrors);
        return new ExceptionResponse(fieldErrors.toString());
    }
}
