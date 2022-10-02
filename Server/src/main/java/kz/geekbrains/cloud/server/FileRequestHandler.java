package kz.geekbrains.cloud.server;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;
import kz.geekbrains.cloud.common.Constants;
import kz.geekbrains.cloud.common.FileRequest;
import kz.geekbrains.cloud.server.service.FileService;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
public class FileRequestHandler implements ServerRequestHandler {

    @Override
    public void handle(ChannelHandlerContext ctx, Object msg) {
        FileRequest fileRequest = (FileRequest) msg;

        String fileName = fileRequest.getFileName();
        String path = fileRequest.getPath();

        Path absolutePath = Paths.get(Constants.SERVER_REP, path, fileName).toAbsolutePath();
        File file = new File(absolutePath.toString());

        FileService.sendFile(ctx.channel(), file, Constants.CLIENT_REP);
        log.info("Request for " + ctx.name() + " was send with file: " + file);
    }
}
