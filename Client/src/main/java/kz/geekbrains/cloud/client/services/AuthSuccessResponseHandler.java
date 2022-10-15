package kz.geekbrains.cloud.client.services;

import io.netty.channel.ChannelHandlerContext;
import javafx.application.Platform;
import kz.geekbrains.cloud.client.network.ClientRequestHandler;
import lombok.extern.log4j.Log4j2;
import kz.geekbrains.cloud.client.gui.Controller;
import kz.geekbrains.cloud.common.auth.AuthSuccessResponse;

import java.util.Objects;

@Log4j2
public class AuthSuccessResponseHandler implements ClientRequestHandler {

    @Override
    public void handle(ChannelHandlerContext ctx, Object msg, Controller controller) {
        AuthSuccessResponse authSuccessResponse = (AuthSuccessResponse) msg;

        String login = authSuccessResponse.getLogin();

        if (Objects.equals(login, "true")) {
            log.info("password changed");

        } else {
            log.info("Authorization completed successfully");

            Platform.runLater(() -> {
                controller.setLogin(login);
                controller.changeStageToCloud();
            });
        }
    }
}
