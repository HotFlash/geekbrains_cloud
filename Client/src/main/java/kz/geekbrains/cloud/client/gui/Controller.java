package kz.geekbrains.cloud.client.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import kz.geekbrains.cloud.client.network.NetworkClient;
import kz.geekbrains.cloud.common.Constants;
import kz.geekbrains.cloud.common.FileInfo;
import kz.geekbrains.cloud.common.FileInfo.FileType;
import kz.geekbrains.cloud.common.ListRequest;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;


@Log4j2
public class Controller implements Initializable {

    @FXML
    VBox mainView;
    @Getter
    private NetworkClient networkClient;
    @FXML
    @Getter
    TableView<FileInfo> filesTable;
    @FXML
    @Getter
    TextField pathField;

    @Getter
    private final FileChooser fileChooser = new FileChooser();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            connection();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        createRepositoryFolder();
        CreateTableView.action(this);

        filesTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String fileName = filesTable.getSelectionModel().getSelectedItem().getFileName();
                String path = pathField.getText();
                if (filesTable.getSelectionModel().getSelectedItem().getType() == FileType.DIRECTORY) {
                    networkClient.send(new ListRequest(Paths.get(path, fileName).toString()));
                }
            }
        });

        changeStageToMainView();
    }


    public void changeStageToMainView() {

        mainView.setVisible(true);
        networkClient.send(new ListRequest("user"));
    }

    public void updateList(List<FileInfo> list) {
        filesTable.getItems().clear();
        filesTable.getItems().addAll(list);
        filesTable.sort();
    }

    public void pathUpAction() {
        String path = pathField.getText();
        if (!path.equals("user")) {
            networkClient.send(new ListRequest(Paths.get(path).getParent().toString()));
        }
    }

    public void deleteAction() {
        Delete.action(this);
    }

    public void downloadAction() {
        Download.action(this);
    }

    public void updatePathField(String path) {
        pathField.setText(path);
    }

    public void uploadAction() {
        Upload.action(this);
    }

    public void makeDirectory() {
        MakeDirectory.action(this);
    }

    private void createRepositoryFolder() {
        File folder = new File(Constants.CLIENT_REP);
        if (!folder.exists()) {
            if (folder.mkdir()) {
                System.out.println("Folder " + folder.getName() + " created");
            } else {
                log.error("Fatal error while user's folder creation. check write folder permission");
            }

        }
    }

    private void connection() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        networkClient = new NetworkClient(this, countDownLatch);
        new Thread(networkClient).start();
        countDownLatch.await();
    }
}