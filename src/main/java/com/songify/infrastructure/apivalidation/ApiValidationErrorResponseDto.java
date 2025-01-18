package com.songify.infrastructure.apivalidation;

record ApiValidationErrorResponseDto(java.util.List<String> errors,
                                     org.springframework.http.HttpStatus badRequest) {
}
