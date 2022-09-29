package kz.geekbrains.cloud.common.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import kz.geekbrains.cloud.common.Message;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
@Log4j2
public class Service {
    private final String host;
    private final int port;

    public Service(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void send(Message message, Consumer<String> callback) throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap client = new Bootstrap();
            client.group(workerGroup);
            client.channel(NioSocketChannel.class);
            client.option(ChannelOption.SO_KEEPALIVE, true);
            client.handler(new ChannelInitializer() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ch.pipeline().addLast(
                            new ObjectEncoder(),
                            new LineBasedFrameDecoder(80),
                            new StringDecoder(StandardCharsets.UTF_8),
                            new ClientHandler(message, callback)
                    );
                }
            });
            ChannelFuture future = client.connect(host, port).sync();
            log.info("message are send");
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
