package kz.geekbrains.cloud.client.services;

import io.netty.channel.ChannelHandlerContext;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import kz.geekbrains.cloud.client.network.ClientRequestHandler;
import lombok.extern.log4j.Log4j2;
import kz.geekbrains.cloud.client.gui.Controller;
import kz.geekbrains.cloud.common.auth.AuthErrorResponse;

import java.util.Objects;

@Log4j2
public class AuthErrorResponseHandler implements ClientRequestHandler {

    @Override
    public void handle(ChannelHandlerContext ctx, Object msg, Controller controller) {
        String reason = ((AuthErrorResponse) msg).getReason();
        if (Objects.equals(reason, "Invalid operation while chane password")) {
            log.error("password change error failed: " + reason);
            Platform.runLater(() -> controller.showAuthMessage(reason, Color.RED));
        } else if (Objects.equals(reason, "Invalid old password")) {
            log.error("invalid old password while change the password: " + reason);
            Platform.runLater(() -> controller.showAuthMessage(reason, Color.RED));
        } else {
            log.error("Authorization failed: " + reason);

            Platform.runLater(() -> controller.showAuthMessage(reason, Color.RED));
        }
    }
}
