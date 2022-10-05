package kz.geekbrains.cloud.server;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;
import kz.geekbrains.cloud.common.Constants;
import kz.geekbrains.cloud.common.DeleteRequest;
import kz.geekbrains.cloud.common.FileErrorResponse;
import kz.geekbrains.cloud.server.service.FileService;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
public class DeleteRequestHandler implements ServerRequestHandler {

    @Override
    public void handle(ChannelHandlerContext ctx, Object msg) {
        DeleteRequest deleteRequest = (DeleteRequest) msg;

        String fileName = deleteRequest.getFileName();
        String path = deleteRequest.getPath();

        Path absolutePath = Paths.get(Constants.SERVER_REP, path, fileName).toAbsolutePath();
        File file = new File(absolutePath.toString());

        if (file.delete()) {
            log.info(ctx.name() + "- File " + file.getPath() + " deleted");
            FileService.sendList(ctx, path);
        } else {
            String reason;

            if (isDirectoryEmpty(file)) {
                reason = "Delete file error";
            } else {
                reason = "Directory is not empty";
            }

            log.warn(ctx.name() + "- Not deleted: " + reason);
            ctx.writeAndFlush(new FileErrorResponse(reason));
        }
    }

    public boolean isDirectoryEmpty(File directory) {
        String[] files = directory.list();
        assert files != null;
        return files.length == 0;
    }
}
