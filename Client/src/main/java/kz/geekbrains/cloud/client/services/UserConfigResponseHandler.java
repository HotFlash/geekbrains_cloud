package kz.geekbrains.cloud.client.services;


import io.netty.channel.ChannelHandlerContext;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import kz.geekbrains.cloud.client.gui.Controller;
import kz.geekbrains.cloud.client.network.ClientRequestHandler;
import kz.geekbrains.cloud.common.UserConfigResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class UserConfigResponseHandler implements ClientRequestHandler {
    public void handle(ChannelHandlerContext ctx, Object msg, Controller controller) {
        Double result = Double.valueOf(((UserConfigResponse) msg).getCapacity());
        controller.setCurrentCapacity(result);
        if (result != null) {
            if ((controller.getCurrentUsage() * 1.1) >= result) {
                Platform.runLater(() -> {

                    controller.showCloudSize(String.format("Current Capacity %,.2f Mb: ", result), Color.RED);

                });
            } else {
                Platform.runLater(() -> {
                    controller.showCloudSize(String.format("Current Capacity %,.2f Mb: ", result), Color.GREEN);
                });
            }
        }
    }

}
