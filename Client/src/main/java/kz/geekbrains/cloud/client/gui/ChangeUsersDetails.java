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
import kz.geekbrains.cloud.common.ChangeUsersDetailsRequest;
import lombok.extern.log4j.Log4j2;

import java.util.Objects;

@Log4j2
public class ChangeUsersDetails {

    public static void action(Controller controller) {
        NetworkClient networkClient = controller.getNetworkClient();

        Label HeadLabel = new Label("please enter new password twice.");

        Label oldPasswordLabel = new Label("Enter your old password");
        Label newPasswordLabel = new Label("Enter your new password");
        Label confirmPasswordLabel = new Label("Confirm your new password");


        TextField oldPasswordField = new TextField();
        oldPasswordField.setPrefWidth(50);
        TextField newPasswordField = new TextField();
        newPasswordField.setPrefWidth(50);
        TextField confirmPasswordField = new TextField();
        confirmPasswordField.setPrefWidth(50);


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
        vBox.getChildren().addAll(HeadLabel, oldPasswordLabel, oldPasswordField, newPasswordLabel, newPasswordField, confirmPasswordLabel, confirmPasswordField, hBox);

        Scene secondScene = new Scene(vBox, 400, 325);

        Stage newWindow = new Stage();
        newWindow.setResizable(false);
        newWindow.setTitle("Update account");
        newWindow.setScene(secondScene);
        newWindow.setX(ClientApplication.getPrimaryStage().getX() + 200);
        newWindow.setY(ClientApplication.getPrimaryStage().getY() + 200);

        newWindow.show();

        btnCreate.setOnAction(event -> {
            String oldPassword = oldPasswordField.getText();
            String newPassword = newPasswordField.getText();
            String confirmNewPassword = confirmPasswordField.getText();

            if (!oldPassword.isEmpty() && Objects.equals(newPassword, confirmNewPassword) && !newPassword.isEmpty()) {
                networkClient.send(new ChangeUsersDetailsRequest(controller.getLogin(), oldPassword, newPassword));
            } else {
                log.info("insert correct passwords!");
            }

            newWindow.close();
        });

        btnCancel.setOnAction(event -> newWindow.close());
    }

}
