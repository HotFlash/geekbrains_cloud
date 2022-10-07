package kz.geekbrains.cloud.common.auth;

import kz.geekbrains.cloud.common.ServerErrorResponse;

public class AuthErrorResponse extends ServerErrorResponse {

    public AuthErrorResponse(String reason) {
        super(reason);
    }
}
