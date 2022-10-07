package kz.geekbrains.cloud.client.services;

import io.netty.channel.ChannelHandlerContext;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import kz.geekbrains.cloud.client.network.ClientRequestHandler;
import lombok.extern.log4j.Log4j2;
import kz.geekbrains.cloud.client.gui.Controller;
import kz.geekbrains.cloud.common.reg.RegErrorResponse;

@Log4j2
public class RegErrorResponseHandler implements ClientRequestHandler {

    @Override
    public void handle(ChannelHandlerContext ctx, Object msg, Controller controller) {
        String reason = ((RegErrorResponse) msg).getReason();

        log.info("Registrarion failed: " + reason);

        Platform.runLater(() -> controller.showRegMessage(reason, Color.RED));
    }
}
