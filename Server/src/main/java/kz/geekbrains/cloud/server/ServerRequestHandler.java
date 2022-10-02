package kz.geekbrains.cloud.server;

import io.netty.channel.ChannelHandlerContext;

public interface ServerRequestHandler {

    void handle(ChannelHandlerContext ctx, Object msg);

}
