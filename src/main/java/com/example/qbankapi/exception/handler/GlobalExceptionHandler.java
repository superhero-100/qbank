package com.example.qbankapi.exception.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<Map<String, Object>> handleValidationErrors(IllegalArgumentException ex) {
//        return getMapResponseEntity(ex.getMessage(), ex);
//    }
//
//    @ExceptionHandler(EntityNotFoundException.class)
//    public ResponseEntity<Map<String, Object>> handleValidationErrors(EntityNotFoundException ex) {
//        return getMapResponseEntity(ex.getMessage(), ex);
//    }
//
//    private ResponseEntity<Map<String, Object>> getMapResponseEntity(String message, Exception ex) {
//        Map<String, Object> errorResponse = new LinkedHashMap<>();
//        errorResponse.put("timestamp", LocalDateTime.now());
//        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
//        errorResponse.put("error", "Bad Request");
//        errorResponse.put("message", message);
//        return ResponseEntity.badRequest().body(errorResponse);
//    }

}
