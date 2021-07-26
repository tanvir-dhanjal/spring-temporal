package com.codejam.error;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ServiceErrorsEnum {
    REQUEST_TIME_OUT(1000, 408, "Request Timed Out"),
    BAD_REQUEST(1001, 400, "Bad Request"),
    INTERNAL_SERVER_ERROR(1002, 500, "Internal Server Error"),
    PLAYER_NOT_FOUND(2000, 404, "Player not found"),
    FIXTURE_NOT_FOUND(2001, 404, "Fixture not found"),
    INVALID_FIXTURE_STATUS(2002, 403, "Invalid fixture status");

    private int code;
    private int statusCode;
    public String message;

    ServiceErrorsEnum(final int code, final int statusCode, final String message) {
        this.code = code;
        this.statusCode = statusCode;
        this.message = message;
    }
}
