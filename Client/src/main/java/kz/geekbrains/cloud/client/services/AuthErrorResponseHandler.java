package kz.geekbrains.cloud.client.services;

import io.netty.channel.ChannelHandlerContext;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import kz.geekbrains.cloud.client.network.ClientRequestHandler;
import lombok.extern.log4j.Log4j2;
import kz.geekbrains.cloud.client.gui.Controller;
import kz.geekbrains.cloud.common.auth.AuthErrorResponse;

@Log4j2
public class AuthErrorResponseHandler implements ClientRequestHandler {

    @Override
    public void handle(ChannelHandlerContext ctx, Object msg, Controller controller) {
        String reason = ((AuthErrorResponse) msg).getReason();

        log.info("Authorization failed: " + reason);

        Platform.runLater(() -> controller.showAuthMessage(reason, Color.RED));
    }
}
