package kz.geekbrains.cloud.server;

import kz.geekbrains.cloud.common.*;
import kz.geekbrains.cloud.common.auth.AuthRequest;
import kz.geekbrains.cloud.common.reg.RegRequest;
import kz.geekbrains.cloud.server.service.ChangeUsersDetailsHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ServerHandlerRegistry {

    private static final Map<Class<? extends AbstractMessage>, ServerRequestHandler> REQUEST_HANDLER_MAP;

    static {
        Map<Class<? extends AbstractMessage>, ServerRequestHandler> requestHandlerMap = new HashMap<>();
        requestHandlerMap.put(AuthRequest.class, new AuthHandler(Server.getAuthService()));
        requestHandlerMap.put(RegRequest.class, new RegHandler(Server.getAuthService()));
        requestHandlerMap.put(UserConfigRequest.class, new UserConfigHandler(Server.getAuthService()));
        requestHandlerMap.put(ListRequest.class, new ListRequestHandler());
        requestHandlerMap.put(FileMessage.class, new FileUploadHandler());
        requestHandlerMap.put(FileRequest.class, new FileRequestHandler());
        requestHandlerMap.put(DeleteRequest.class, new DeleteRequestHandler());
        requestHandlerMap.put(MakeDirRequest.class, new MakeDirRequestHandler());
        requestHandlerMap.put(ChangeUsersDetailsRequest.class, new ChangeUsersDetailsHandler(Server.getAuthService()));

        REQUEST_HANDLER_MAP = Collections.unmodifiableMap(requestHandlerMap);
    }

    public static ServerRequestHandler getHandler(Class<?> messageClass) {
        return REQUEST_HANDLER_MAP.get(messageClass);
    }
}
