package View;

import View.BookApp;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.regex.Pattern;

public class HostController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TextField ipTextField = new TextField();

    @FXML
    private TextField portTextField = new TextField();

    @FXML
    public TextField nameTextField;

    @FXML
    public TextField portField;

    @FXML
    private Button nextButton;

    @FXML
    private Button submitButton;

    int hostPort;
    public static String name;
    BS_Host_ViewModel host = new BS_Host_ViewModel();

    @FXML
    public void onSubmitButtonPressed() {
        if (!validatePort(portTextField.getText()) || !validateIp(ipTextField.getText())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid IP or Port");
            alert.setHeaderText("Invalid IP or Port Number");
            alert.setContentText("Please enter a valid IP and Port Number.");
            alert.showAndWait();
        } else {
            boolean connected = true;
            host.ip.bindBidirectional(ipTextField.textProperty());
            host.port.bindBidirectional(portTextField.textProperty());
            try {
                host.openSocket();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Connection Error");
                alert.setHeaderText("Connection Error");
                alert.setContentText("The game server is not connected.\nConnect the game server and click Submit again.");
                alert.showAndWait();
                connected = false;
            }
            if (connected) {
                nextButton.setVisible(true);
                submitButton.onActionProperty().set(null);
            }
        }
    }

    @FXML
    public void onNextButtonPressed() throws Exception {
        root = FXMLLoader.load(getClass().getResource("/BookScrabbleApp.View/hostNextWindow.fxml"));
        stage = (Stage) welcomeLabel.getScene().getWindow();
        stage.setOnCloseRequest(e -> Platform.exit());
        scene = new Scene(root, BookApp.screenSize()[0], BookApp.screenSize()[1]);
        stage.setScene(scene);
        stage.show();
    }

    private boolean validatePort(String port) {
        String portRegex = "^([1-9]|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-4]\\d{3}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])$";
        return Pattern.matches(portRegex, port);
    }

    private boolean checkPort(String port) {
        int portNumber = Integer.parseInt(port);
        boolean flag = true;
        try {
            ServerSocket socket = new ServerSocket(portNumber);
            socket.close();
        } catch (IOException e) {
            flag = false;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Port Error");
            alert.setHeaderText("Port number is already in use");
            alert.setContentText("Please enter another port number");
            alert.showAndWait();
        }
        return flag;
    }
}
