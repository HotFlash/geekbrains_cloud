package kz.geekbrains.cloud.client.network;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import kz.geekbrains.cloud.common.AbstractMessage;
import kz.geekbrains.cloud.common.ListResponse;
import kz.geekbrains.cloud.common.FileMessage;
import kz.geekbrains.cloud.common.FileErrorResponse;

public class ClientHandlerRegistry {

    private static final Map<Class<? extends AbstractMessage>, ClientRequestHandler> REQUEST_HANDLER_MAP;

    static {
        Map<Class<? extends AbstractMessage>, ClientRequestHandler> requestHandlerMap = new HashMap<>();
        requestHandlerMap.put(ListResponse.class, new ListResponseHandler());
        requestHandlerMap.put(FileMessage.class, new FileDownloadHandler());
        requestHandlerMap.put(FileErrorResponse.class, new FileErrorResponseHandler());

        REQUEST_HANDLER_MAP = Collections.unmodifiableMap(requestHandlerMap);
    }

    public static ClientRequestHandler getHandler(Class<?> messageClass) {
        return REQUEST_HANDLER_MAP.get(messageClass);
    }
}
