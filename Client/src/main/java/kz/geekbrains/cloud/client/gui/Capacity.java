package kz.geekbrains.cloud.client.gui;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import kz.geekbrains.cloud.client.ClientApplication;
import kz.geekbrains.cloud.client.network.NetworkClient;
import kz.geekbrains.cloud.common.UserConfigRequest;
import lombok.extern.log4j.Log4j2;


@Log4j2
public class Capacity {

    public static void action(Controller controller, Integer option) {
        NetworkClient networkClient = controller.getNetworkClient();
        if (option == 0) {
            networkClient.send(new UserConfigRequest(controller.getLogin(), 0));
        } else {
            Label secondLabel = new Label("Set new capacity:");

            TextField textField = new TextField();
            textField.setPrefWidth(50);

            Button btnCreate = new Button("Set");
            btnCreate.setPrefSize(50, 30);
            Button btnCancel = new Button("Cancel");

            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setSpacing(10);
            hBox.getChildren().addAll(btnCreate, btnCancel);

            VBox vBox = new VBox();
            vBox.setSpacing(10);
            vBox.setPadding(new Insets(20));
            vBox.getChildren().addAll(secondLabel, textField, hBox);

            Scene secondScene = new Scene(vBox, 200, 125);

            Stage newWindow = new Stage();
            newWindow.setResizable(false);
            newWindow.setTitle("Change Capacity");
            newWindow.setScene(secondScene);
            newWindow.setX(ClientApplication.getPrimaryStage().getX() + 200);
            newWindow.setY(ClientApplication.getPrimaryStage().getY() + 200);

            newWindow.show();

            btnCreate.setOnAction(event -> {
                Integer capacity = Integer.valueOf(textField.getText());

                networkClient.send(new UserConfigRequest(controller.getLogin(), capacity));
                log.info("Capacity is set to: " + capacity);

                newWindow.close();
            });

            btnCancel.setOnAction(event -> newWindow.close());
        }
    }
}
