package BookScrabbleApp.View;

import BookScrabbleApp.ViewModel.BS_Guest_ViewModel;
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

import java.util.regex.Pattern;

public class GuestController {
    @FXML
    private Label invalidIpOrPortLabel;

    @FXML
    private TextField ipTextField;

    @FXML
    private TextField portTextField;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button submitButton;

    @FXML
    private Button switchToGameButton;

    @FXML
    private TextField nameTextField;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public static String name;

    private BS_Guest_ViewModel guest = new BS_Guest_ViewModel();

    @FXML
    public void onSubmitButtonClicked() {
        if (nameTextField.getText().isEmpty()) {
            name = "Guest";
        } else {
            name = nameTextField.getText();
        }

        guest.setPlayerProperties(name);

        if (!validatePort(portTextField.getText()) || !validateIp(ipTextField.getText())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Problem with IP or Port");
            alert.setHeaderText("Invalid IP or Port number");
            alert.setContentText("Please enter a valid IP and Port number");
            alert.showAndWait();
        } else {
            boolean connected = true;
            guest.hostIp.bindBidirectional(ipTextField.textProperty());
            guest.hostPort.bindBidirectional(portTextField.textProperty());

            try {
                guest.openSocket();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Connect Error");
                alert.setHeaderText("Connect Error");
                alert.setContentText("The game server is not connected\n" +
                        "Connect the game server and click submit again");
                alert.showAndWait();
                connected = false;
            }

            if (connected) {
                switchToGameButton.setVisible(true);
                submitButton.onActionProperty().set(null);
            }
        }
    }

    @FXML
    public void onSwitchToGameButtonClicked() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/BookScrabbleApp.View/gameMainWindow.fxml"));
        root = loader.load();
        mainController controller = loader.getController();
        controller.setViewModel(guest);
        stage = (Stage) welcomeLabel.getScene().getWindow();
        stage.setOnCloseRequest(e -> Platform.exit());
        scene = new Scene(root);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.show();
    }

    private boolean validatePort(String port) {
        String portRegex = "^([1-9]|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-4]\\d{3}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])$";
        return Pattern.matches(portRegex, port);
    }

    private boolean validateIp(String ip) {
        String ipv4Regex = "^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$";
        return Pattern.matches(ipv4Regex, ip);
    }
}
