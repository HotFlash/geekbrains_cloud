package kz.geekbrains.cloud.client.gui;

import java.io.File;
import java.nio.file.Paths;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import lombok.extern.log4j.Log4j2;
import kz.geekbrains.cloud.client.network.NetworkClient;
import kz.geekbrains.cloud.common.Constants;
import kz.geekbrains.cloud.common.FileRequest;
import kz.geekbrains.cloud.common.FileInfo;
import kz.geekbrains.cloud.common.FileInfo.FileType;

@Log4j2
public class Download {

    public static void action(Controller controller) {
        TableView<FileInfo> filesTable = controller.getFilesTable();
        TextField pathField = controller.getPathField();
        NetworkClient networkClient = controller.getNetworkClient();
        FileInfo fileInfo = filesTable.getSelectionModel().getSelectedItem();

        if (fileInfo == null) {
            Alert alert = new Alert(AlertType.WARNING, "Please, select the FIle", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if (fileInfo.getType() == FileType.DIRECTORY) {
            Alert alert = new Alert(AlertType.WARNING, "Directory selected. \n Please, select the file", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        File file = new File(Paths.get(Constants.CLIENT_REP, fileInfo.getFileName()).toString());

        if (file.exists()) {
            Alert alert = new Alert(AlertType.CONFIRMATION, "File already exists at the local store. \n" +
                    "Do you want to overwrite?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.isPresent()) {
                if (option.get() == ButtonType.OK) {
                    String fileName = filesTable.getSelectionModel().getSelectedItem().getFileName();
                    String path = pathField.getText();

                    networkClient.send(new FileRequest(fileName, path));
                    log.info("FileRequest action: " + Paths.get(path, fileName));
                }
            }
        } else {
            String fileName = filesTable.getSelectionModel().getSelectedItem().getFileName();
            String path = pathField.getText();
            networkClient.send(new FileRequest(fileName, path));
            log.info("FileRequest action: " + Paths.get(path, fileName));
        }
    }
}
