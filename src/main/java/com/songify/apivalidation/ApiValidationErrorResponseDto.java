package com.songify.apivalidation;

public record ApiValidationErrorResponseDto(java.util.List<String> errors,
                                            org.springframework.http.HttpStatus badRequest) {
}
