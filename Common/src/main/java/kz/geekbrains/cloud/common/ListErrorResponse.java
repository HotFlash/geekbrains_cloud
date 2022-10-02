package kz.geekbrains.cloud.common;

import kz.geekbrains.cloud.common.ServerErrorResponse;

public class ListErrorResponse extends ServerErrorResponse {

    public ListErrorResponse(String reason) {
        super(reason);
    }
}
