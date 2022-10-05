package kz.geekbrains.cloud.client.network;

import io.netty.channel.ChannelHandlerContext;
import kz.geekbrains.cloud.client.gui.Controller;

public interface ClientRequestHandler {

    void handle(ChannelHandlerContext ctx, Object msg, Controller controller);

}
