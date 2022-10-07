package kz.geekbrains.cloud.client.network;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import kz.geekbrains.cloud.client.services.AuthErrorResponseHandler;
import kz.geekbrains.cloud.client.services.AuthSuccessResponseHandler;
import kz.geekbrains.cloud.client.services.RegErrorResponseHandler;
import kz.geekbrains.cloud.client.services.RegSuccessResponseHandler;
import kz.geekbrains.cloud.common.AbstractMessage;
import kz.geekbrains.cloud.common.ListResponse;
import kz.geekbrains.cloud.common.FileMessage;
import kz.geekbrains.cloud.common.FileErrorResponse;
import kz.geekbrains.cloud.common.auth.AuthErrorResponse;
import kz.geekbrains.cloud.common.auth.AuthSuccessResponse;
import kz.geekbrains.cloud.common.reg.RegErrorResponse;
import kz.geekbrains.cloud.common.reg.RegSuccessResponse;

public class ClientHandlerRegistry {

    private static final Map<Class<? extends AbstractMessage>, ClientRequestHandler> REQUEST_HANDLER_MAP;

    static {
        Map<Class<? extends AbstractMessage>, ClientRequestHandler> requestHandlerMap = new HashMap<>();
        requestHandlerMap.put(ListResponse.class, new ListResponseHandler());
        requestHandlerMap.put(FileMessage.class, new FileDownloadHandler());
        requestHandlerMap.put(FileErrorResponse.class, new FileErrorResponseHandler());
        requestHandlerMap.put(AuthErrorResponse.class, new AuthErrorResponseHandler());
        requestHandlerMap.put(AuthSuccessResponse.class, new AuthSuccessResponseHandler());
        requestHandlerMap.put(RegErrorResponse.class, new RegErrorResponseHandler());
        requestHandlerMap.put(RegSuccessResponse.class, new RegSuccessResponseHandler());

        REQUEST_HANDLER_MAP = Collections.unmodifiableMap(requestHandlerMap);
    }

    public static ClientRequestHandler getHandler(Class<?> messageClass) {
        return REQUEST_HANDLER_MAP.get(messageClass);
    }
}
