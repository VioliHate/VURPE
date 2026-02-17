package it.study.bruvio.vurpe.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public record PayloadResponse<T> (
    Status status,
    Object payload,
    String message,
    String errorCode
){
    public static <T> PayloadResponse<T> success(T data, String message) {
        return new PayloadResponse<>(Status.OK, data, message, null);
    }

    public static <T> PayloadResponse<T> error(String message, String errorCode) {
        return new PayloadResponse<>(Status.ERROR, null, message, errorCode);
    }

    public ResponseEntity<PayloadResponse<T>> toResponseEntity(HttpStatus status) {
        return ResponseEntity.status(status).body(this);
    }
}


enum Status {
    OK,
    ERROR
}
