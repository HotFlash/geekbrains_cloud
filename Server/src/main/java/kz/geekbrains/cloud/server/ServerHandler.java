package kz.geekbrains.cloud.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import kz.geekbrains.cloud.common.Constants;
import lombok.extern.log4j.Log4j2;


import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Path absolutePath = Paths.get(Constants.SERVER_REP, "user").toAbsolutePath();
        File folder = new File(absolutePath.toString());
        if (!folder.exists()) {
            boolean result = folder.mkdir();
            log.info("server per has been run " + folder.getPath() + " with result:" + result);
        } else {
            log.error("can't create server repository : " + folder.getPath());
        }
        log.info("Client connected: " + ctx.name());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ServerRequestHandler handler = ServerHandlerRegistry.getHandler(msg.getClass());
        handler.handle(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
