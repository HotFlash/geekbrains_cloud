package kz.geekbrains.cloud.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.extern.log4j.Log4j2;
import kz.geekbrains.cloud.client.gui.Controller;
import kz.geekbrains.cloud.common.FileMessage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

@Log4j2
public class FileService {

    public static void sendFile(Channel channel, File file, String path, Controller controller) {
        new Thread(() -> {
            try {
                int bufSize = 1024 * 1024 * 5;
                int partsCount = new Long(file.length() / bufSize).intValue();
                if (file.length() % bufSize != 0) {
                    partsCount++;
                }

                FileMessage fileMessage = new FileMessage(file.getName(), path, -1, partsCount, new byte[bufSize]);
                FileInputStream in = new FileInputStream(file);

                for (int i = 0; i < partsCount; i++) {
                    int readBytes = in.read(fileMessage.data);
                    fileMessage.partNumber = i + 1;
                    if (readBytes < bufSize) {
                        fileMessage.data = Arrays.copyOfRange(fileMessage.data, 0, readBytes);
                    }
                    ChannelFuture f = channel.writeAndFlush(fileMessage);
                    f.sync();

                    log.info("File " + fileMessage.filename + " part " + (i + 1) + "/" + partsCount + " was sent");
                }
                in.close();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
