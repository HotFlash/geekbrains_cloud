package kz.geekbrains.cloud.client.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import lombok.extern.log4j.Log4j2;
import kz.geekbrains.cloud.client.network.NetworkClient;
import kz.geekbrains.cloud.common.DeleteRequest;
import kz.geekbrains.cloud.common.FileInfo;

import java.nio.file.Paths;

@Log4j2
public class Delete {
    public static void action(Controller controller) {
        TableView<FileInfo> filesTable = controller.getFilesTable();
        TextField pathField = controller.getPathField();
        NetworkClient networkClient = controller.getNetworkClient();

        if (filesTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(AlertType.WARNING, "Please, select the File", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        String fileName = filesTable.getSelectionModel().getSelectedItem().getFileName();
        String path = pathField.getText();

        networkClient.send(new DeleteRequest(fileName, path));
        log.info("DeleteRequest action: " + Paths.get(path, fileName));
    }

}
