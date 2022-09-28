package kz.geekbrains.cloud.common.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import kz.geekbrains.cloud.common.Constants;
import kz.geekbrains.cloud.common.Message;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.function.Consumer;

@Log4j2
public class Client {
    private final String host;
    private final int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        File file = new File("client/dir/my-file.txt");
        Message message = new Message("put", file, Files.readAllBytes(file.toPath()));
        new Client(Constants.SERVER, Constants.PORT).send(message, (response) -> {
            System.out.println("response = " + response);
        });
        label:
        while (true) {
            log.info("waiting for user's command");
            log.info("command 'send' send file");
            log.info("command 'info' show file info");
            log.info("command 'stop' stop the Client app");
            Scanner scanner = new Scanner(System.in);

            switch (scanner.nextLine()) {
                case "send":
                    new Client(Constants.SERVER, Constants.PORT).send(message, (response) -> {
                        System.out.println("response = " + response);
                    });
                    break;
                case "info":
                    Message message1 = new Message("fileInfo", file, Files.readAllBytes(file.toPath()));
                    new Client(Constants.SERVER, Constants.PORT).send(message1, (response) -> {
                        System.out.println("Response = " + response);
                    });
                    break;
                case "stop":
                    log.info("we are stopping client app");
                    break label;
            }

        }
    }

    private void send(Message message, Consumer<String> callback) throws InterruptedException {
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