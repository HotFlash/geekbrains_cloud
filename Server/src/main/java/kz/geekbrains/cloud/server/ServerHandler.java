package kz.geekbrains.cloud.server;

import kz.geekbrains.cloud.common.Message;
import kz.geekbrains.cloud.common.FileInfo;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
public class ServerHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        System.out.println("msg = " + msg);
        if (msg.getCommand().equals("put")) {
            Path root = Paths.get("Server/user-dir");
            Files.createDirectories(root);
            Path file = root.resolve(msg.getFile().getPath());
            Files.createDirectories(file.getParent());
            try {
                Files.createFile(file);
            } catch (FileAlreadyExistsException ignored) {
                log.info("file exist");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Files.write(file, msg.getData());
            ChannelFuture future = ctx.writeAndFlush(String.format("File %s stored!\n", msg.getFile().getName()));
            future.addListener(ChannelFutureListener.CLOSE);
        }

        if (msg.getCommand().equals("fileInfo")) {
            FileInfo fileInfo = new FileInfo(msg.getPath());
            msg.SetFileInfo(fileInfo);
            ChannelFuture future1 = ctx.writeAndFlush(String.format("File Info %s \n", msg.getFileInfo()));
            System.out.println(fileInfo);
            future1.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}