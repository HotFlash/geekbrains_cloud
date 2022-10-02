package kz.geekbrains.cloud.client.network;

import io.netty.channel.ChannelHandlerContext;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import kz.geekbrains.cloud.client.gui.Controller;
import kz.geekbrains.cloud.common.FileErrorResponse;

public class FileErrorResponseHandler implements ClientRequestHandler {

    @Override
    public void handle(ChannelHandlerContext ctx, Object msg, Controller controller) {
        FileErrorResponse fileErrorResponse = (FileErrorResponse) msg;

        String reason = fileErrorResponse.getReason();

        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.WARNING, reason, ButtonType.OK);
            alert.showAndWait();
        });
    }
}
