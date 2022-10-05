package kz.geekbrains.cloud.server;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;
import kz.geekbrains.cloud.common.Constants;
import kz.geekbrains.cloud.common.MakeDirRequest;
import kz.geekbrains.cloud.server.service.FileService;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
public class MakeDirRequestHandler implements ServerRequestHandler {

    @Override
    public void handle(ChannelHandlerContext ctx, Object msg) {
        MakeDirRequest makeDirRequest = (MakeDirRequest) msg;

        String fileName = makeDirRequest.getFileName();
        String path = makeDirRequest.getPath();

        Path absolutePath = Paths.get(Constants.SERVER_REP, path, fileName).toAbsolutePath();
        File file = new File(absolutePath.toString());

        boolean result = file.mkdir();
        log.info(ctx.name() + "- Make directory " + absolutePath + " result " + result);

        FileService.sendList(ctx, path);
    }
}
