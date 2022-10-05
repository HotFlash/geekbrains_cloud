package kz.geekbrains.cloud.server;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;
import kz.geekbrains.cloud.common.ListRequest;
import kz.geekbrains.cloud.server.service.FileService;

@Log4j2
public class ListRequestHandler implements ServerRequestHandler {

    @Override
    public void handle(ChannelHandlerContext ctx, Object msg) {
        ListRequest listRequest = (ListRequest) msg;

        String path = listRequest.getPath();

        FileService.sendList(ctx, path);
    }
}
