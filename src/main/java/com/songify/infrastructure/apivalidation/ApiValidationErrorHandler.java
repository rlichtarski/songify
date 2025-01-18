package com.songify.infrastructure.apivalidation;

import com.songify.infrastructure.crud.song.controller.SongRestController;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice(assignableTypes = SongRestController.class)
class ApiValidationErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiValidationErrorResponseDto> handleValidationException(MethodArgumentNotValidException exception) {
        List<String> errorsFromException = getErrorsFromException(exception);
        ApiValidationErrorResponseDto apiValidationErrorResponseDto = new ApiValidationErrorResponseDto(errorsFromException, HttpStatus.BAD_REQUEST);
        return ResponseEntity
                .badRequest()
                .header("nice", "header")
                .body(apiValidationErrorResponseDto);
    }

    private List<String> getErrorsFromException(MethodArgumentNotValidException exception) {
        return exception.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
    }

}
