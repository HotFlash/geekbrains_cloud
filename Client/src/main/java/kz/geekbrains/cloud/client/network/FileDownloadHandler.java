package kz.geekbrains.cloud.client.network;

import io.netty.channel.ChannelHandlerContext;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import lombok.extern.log4j.Log4j2;
import kz.geekbrains.cloud.client.gui.Controller;
import kz.geekbrains.cloud.common.Constants;
import kz.geekbrains.cloud.common.FileMessage;

@Log4j2
public class FileDownloadHandler implements ClientRequestHandler {

    private FileOutputStream fos;
    private Boolean append;

    @Override
    public void handle(ChannelHandlerContext ctx, Object msg, Controller controller) {
        FileMessage fileMessage = (FileMessage) msg;

        try {
            if (fileMessage.partNumber == 1) {
                append = false;
                fos = new FileOutputStream(Paths.get(Constants.CLIENT_REP, fileMessage.filename).toString(), append);
            } else {
                append = true;
            }

            log.info(ctx.name() + ": File " + fileMessage.filename + " part " + fileMessage.partNumber + " / " + fileMessage.partsCount + " received");
            fos.write(fileMessage.data);

            if (fileMessage.partNumber == fileMessage.partsCount) {
                fos.close();
                append = false;
                log.info(ctx.name() + ": File " + fileMessage.filename + " is completely downloaded");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
