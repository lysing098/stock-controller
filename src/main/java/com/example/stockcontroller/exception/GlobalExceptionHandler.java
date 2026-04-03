package com.example.stockcontroller.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.transaction.TransactionSystemException;
//import org.hibernate.exception.ConstraintViolationException as HibernateConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(MaxUploadSizeExceededException.class)
        public ResponseEntity<?> handleMaxSize(MaxUploadSizeExceededException e) {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "message", "File too large",
                                "size", e.getMaxUploadSize()
                        ));
        }

        // --- Resource not found ---
        @ExceptionHandler(MyResourceNotFoundException.class)
        public ResponseEntity<ErrorMessage> resourceNotFoundException(
                MyResourceNotFoundException ex,
                WebRequest request) {

                ErrorMessage message = new ErrorMessage(
                        HttpStatus.NOT_FOUND.value(),
                        new Date(),
                        ex.getMessage(),
                        request.getDescription(false)
                );

                return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        // --- General exceptions ---
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorMessage> globalExceptionHandler(
                Exception ex,
                WebRequest request) {

                ErrorMessage message = new ErrorMessage(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        new Date(),
                        ex.getMessage(),
                        request.getDescription(false)
                );

                return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // --- DTO validation errors ---
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, String>> handleValidationExceptions(
                MethodArgumentNotValidException ex) {

                Map<String, String> errors = new HashMap<>();
                ex.getBindingResult().getFieldErrors()
                        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

                return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        // --- Entity validation errors (Hibernate / JPA) ---
        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<Map<String, String>> handleEntityValidation(ConstraintViolationException ex) {
                Map<String, String> errors = new HashMap<>();
                ex.getConstraintViolations().forEach(cv ->
                        errors.put(cv.getPropertyPath().toString(), cv.getMessage())
                );
                return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        // --- Database unique / constraint violations ---
        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<Map<String, String>> handleUniqueConstraint(
                DataIntegrityViolationException ex) {

                Map<String, String> errors = new HashMap<>();
                String message = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();

                if (message != null && message.toLowerCase().contains("duplicate")) {
                        String fieldName = extractFieldFromMessage(message);
                        errors.put(fieldName, fieldName + " must be unique!");
                } else {
                        errors.put("error", "Database error: " + message);
                }

                return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        // --- Helper methods ---
        private String extractFieldFromMessage(String message) {
                message = message.toLowerCase();

                try {
                        if (message.contains("for key")) { // MySQL
                                String keyPart = message.substring(message.indexOf("for key") + 7).trim();
                                keyPart = keyPart.replaceAll("[`'\"]", "");
                                return parseFieldFromConstraint(keyPart);
                        }

                        if (message.contains("violates unique constraint")) { // PostgreSQL
                                String[] parts = message.split("\"");
                                if (parts.length >= 2) {
                                        return parseFieldFromConstraint(parts[1]);
                                }
                        }
                } catch (Exception ignored) {}

                return "error";
        }

        @ExceptionHandler(TransactionSystemException.class)
        public ResponseEntity<Map<String, String>> handleTransactionSystemException(TransactionSystemException ex) {
                Throwable cause = ex.getRootCause();
                Map<String, String> errors = new HashMap<>();

                if (cause instanceof ConstraintViolationException cve) {
                        // extract validation errors from Hibernate/JPA
                        cve.getConstraintViolations().forEach(cv ->
                                errors.put(cv.getPropertyPath().toString(), cv.getMessage())
                        );
                } else {
                        errors.put("error", cause != null ? cause.getMessage() : ex.getMessage());
                }

                return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        private String parseFieldFromConstraint(String constraintName) {
                if (constraintName.contains(".")) {
                        String[] parts = constraintName.split("\\.");
                        constraintName = parts[parts.length - 1];
                }
                if (constraintName.contains("_")) {
                        String[] parts = constraintName.split("_");
                        return parts[parts.length - 1];
                }
                return constraintName;
        }
}