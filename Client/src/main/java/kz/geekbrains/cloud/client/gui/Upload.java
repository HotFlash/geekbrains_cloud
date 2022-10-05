package kz.geekbrains.cloud.client.gui;

import java.io.File;
import java.util.List;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import lombok.extern.log4j.Log4j2;
import kz.geekbrains.cloud.client.ClientApplication;
import kz.geekbrains.cloud.client.network.NetworkClient;
import kz.geekbrains.cloud.client.FileService;
import kz.geekbrains.cloud.common.FileInfo;

@Log4j2
public class Upload {

    public static void action(Controller controller) {
        FileChooser fileChooser = controller.getFileChooser();
        TableView<FileInfo> filesTable = controller.getFilesTable();
        TextField pathField = controller.getPathField();
        NetworkClient networkClient = controller.getNetworkClient();

        File file = fileChooser.showOpenDialog(ClientApplication.getPrimaryStage());

        if (file != null) {
            List<FileInfo> list = filesTable.getItems();
            for (FileInfo fi : list) {
                if (file.getName().equals(fi.getFileName())) {
                    Alert alert = new Alert(AlertType.CONFIRMATION, "File already exists on Remote store. \n" +
                            "Do you want overwrite?");
                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.isPresent()) {
                        if (option.get() == ButtonType.OK) {
                            log.info("File selected: " + file.getPath());
                            FileService.sendFile(networkClient.getChannelFuture().channel(), file, pathField.getText(), controller);
                            return;
                        }
                        if (option.get() == ButtonType.CANCEL) {
                            return;
                        }
                    }
                }
            }

            log.info("File selected: " + file.getPath());
            FileService.sendFile(networkClient.getChannelFuture().channel(), file, pathField.getText(), controller);
        }
    }
}
