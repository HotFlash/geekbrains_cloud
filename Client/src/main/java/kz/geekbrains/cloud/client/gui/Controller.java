package kz.geekbrains.cloud.client.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import kz.geekbrains.cloud.client.network.NetworkClient;
import kz.geekbrains.cloud.common.Constants;
import kz.geekbrains.cloud.common.FileInfo;
import kz.geekbrains.cloud.common.FileInfo.FileType;
import kz.geekbrains.cloud.common.ListRequest;
import kz.geekbrains.cloud.common.auth.AuthRequest;
import kz.geekbrains.cloud.common.reg.RegRequest;
import lombok.Getter;
import lombok.Setter;
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
    @FXML
    TextField authLogin;
    @FXML
    PasswordField authPassword;
    @FXML
    Label authMessage;

    @FXML
    Label cloudSizeMessage;

    @FXML
    Label cloudSizeCapacity;
    @FXML
    GridPane regView;
    @FXML
    TextField regLogin;
    @FXML
    PasswordField regPassword;
    @FXML
    PasswordField regPasswordRep;
    @FXML
    Label regMessage;

    @Getter
    @Setter
    String login;
    @Getter
    @Setter
    Double currentUsage;
    @Getter
    @Setter
    Double currentCapacity;
    @FXML
    GridPane authView;


    @Getter
    private final FileChooser fileChooser = new FileChooser();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createRepositoryFolder();
        changeStageToAuth();
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
    }


    public void changeStageToAuth() {
        authLogin.clear();
        authPassword.clear();
        authMessage.setVisible(false);

        authView.setVisible(true);
        regView.setVisible(false);
        mainView.setVisible(false);
    }

    public void register() throws InterruptedException {
        connection();

        if (regLogin.getText().isEmpty() || regPassword.getText().isEmpty() || regPasswordRep.getText().isEmpty()) {
            regMessage.setTextFill(Color.RED);
            regMessage.setText("Enter login, password and name");
            regMessage.setVisible(true);
        } else if (!regPassword.getText().equals(regPasswordRep.getText())) {
            regMessage.setTextFill(Color.RED);
            regMessage.setText("Passwords do not match");
            regMessage.setVisible(true);
        } else {
            log.info("Trying to register a new user: " + regLogin.getText());
            networkClient.send(new RegRequest(regLogin.getText(), regPassword.getText(), 2));

        }
    }

    public void updateList(List<FileInfo> list) {
        filesTable.getItems().clear();
        filesTable.getItems().addAll(list);
        filesTable.sort();
        currentUsage = 0.0;
        for (FileInfo fileInfo : list) {
            currentUsage += fileInfo.getSize();
        }
        showCloudUsage("Cloud size is: " + String.format("%,.2f Mb", currentUsage), Color.DARKBLUE);
        showCapacity();
    }



    public void pathUpAction() {
        String path = pathField.getText();
        if (!path.equals(login)) {
            networkClient.send(new ListRequest(Paths.get(path).getParent().toString()));
        }
    }

    public void showAuthMessage(String reason, Color color) {
        authMessage.setTextFill(color);
        authMessage.setText(reason);
        authMessage.setVisible(true);
    }

    public void showCloudUsage(String capacity, Color color) {
        cloudSizeMessage.setTextFill(color);
        cloudSizeMessage.setText(capacity);
        cloudSizeMessage.setVisible(true);
    }

    public String GetCloudUsage(){
        return cloudSizeMessage.getText();
    }
    public void showCloudSize(String capacity, Color color) {
        cloudSizeCapacity.setTextFill(color);
        cloudSizeCapacity.setText(capacity);
        cloudSizeCapacity.setVisible(true);
    }

    public void showRegMessage(String reason, Color color) {
        regMessage.setTextFill(color);
        regMessage.setText(reason);
        regMessage.setVisible(true);
    }

    public void enterCloud() throws InterruptedException {
        connection();

        if (authLogin.getText().isEmpty() || authPassword.getText().isEmpty()) {
            authMessage.setText("Enter login and password");
            authMessage.setVisible(true);
        } else {
            log.info("Trying to log in: " + authLogin.getText());
            networkClient.send(new AuthRequest(authLogin.getText(), authPassword.getText()));
        }
    }

    public void changeStageToReg() {
        regLogin.clear();
        regPassword.clear();
        regPasswordRep.clear();
        regMessage.setVisible(false);

        authView.setVisible(false);
        mainView.setVisible(false);
        regView.setVisible(true);

    }

    public void changeStageToCloud() {
        authView.setVisible(false);
        regView.setVisible(false);
        mainView.setVisible(true);

        networkClient.send(new ListRequest(login));
    }

    public void deleteAction() {
        Delete.action(this);
    }

    public void showCapacity() {
        Capacity.action(this, 0);
    }

    public void changeCapacity() {
        Capacity.action(this, 1);
    }

    public void changeUserDetails() {
        ChangeUsersDetails.action(this);
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

    public void exitAction() {
        Platform.exit();
    }

    private void connection() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        networkClient = new NetworkClient(this, countDownLatch);
        new Thread(networkClient).start();
        countDownLatch.await();
    }
}