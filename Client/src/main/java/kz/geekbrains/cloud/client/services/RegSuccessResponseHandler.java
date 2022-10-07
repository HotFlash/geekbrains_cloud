package kz.geekbrains.cloud.client.services;

import io.netty.channel.ChannelHandlerContext;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import kz.geekbrains.cloud.client.network.ClientRequestHandler;
import kz.geekbrains.cloud.common.reg.RegSuccessResponse;
import lombok.extern.log4j.Log4j2;
import kz.geekbrains.cloud.client.gui.Controller;

@Log4j2
public class RegSuccessResponseHandler implements ClientRequestHandler {

    @Override
    public void handle(ChannelHandlerContext ctx, Object msg, Controller controller) {
        Boolean result = ((RegSuccessResponse) msg).getResult();
        log.info("Registration completed successfully");


        if (result) {
            Platform.runLater(() -> controller.changeStageToAuth());
            Platform.runLater(() -> controller.showAuthMessage("Registration completed \nPlease use yor Username \nand Password", Color.ROYALBLUE));

        }

    }
}
