package kz.geekbrains.cloud.server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.string.StringEncoder;
import kz.geekbrains.cloud.common.Constants;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;

@Log4j2
public class Server {

    private final int port;

    public Server(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        new Server(Constants.PORT).run();
    }

    private void run() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup, workerGroup);
            server.channel(NioServerSocketChannel.class);
            server.option(ChannelOption.SO_BACKLOG, 128);
            server.childOption(ChannelOption.SO_KEEPALIVE, true);
            server.childHandler(new ChannelInitializer() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ch.pipeline().addLast(
                            new StringEncoder(StandardCharsets.UTF_8),
                            new ObjectDecoder(Constants.MAXIMUM_OBJECT_SIZE, ClassResolvers.cacheDisabled(null)),
                            new ServerHandler()
                    );
                }
            });
            ChannelFuture future = server.bind(port).sync();
            log.info("server is running");
            future.channel().closeFuture().sync();

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}