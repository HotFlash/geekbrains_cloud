package kz.geekbrains.cloud.client.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import kz.geekbrains.cloud.client.gui.Controller;

public class NetworkClientHandler extends ChannelInboundHandlerAdapter {

    private final Controller controller;

    public NetworkClientHandler(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ClientRequestHandler handler = ClientHandlerRegistry.getHandler(msg.getClass());
        handler.handle(ctx, msg, controller);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
