package View;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Screen;
import javafx.stage.Stage;

class mainController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label welcomeLabel;
    @FXML
    public void onHostGameButtonClicked() throws Exception {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getBounds();
        double screenWidth = bounds.getWidth();
        double screenHeight = bounds.getHeight();

        root = FXMLLoader.load(getClass().getResource("/BookApp.View/hostServerWindow.fxml"));
        stage = (Stage) welcomeLabel.getScene().getWindow();
        scene = new Scene(root, BookApp.screenSize()[0], BookApp.screenSize()[1]);
        stage.setOnCloseRequest(e -> Platform.exit());
        stage.setScene(scene);
        stage.setMinWidth(BookApp.MIN_WIDTH);
        stage.setMinHeight(BookApp.MIN_HEIGHT);
        stage.show();
    }

    @FXML
    public void onGuestButtonClicked() throws Exception {
        root = FXMLLoader.load(getClass().getResource("/BookScrabbleApp.View/guestWindow.fxml"));
        stage = (Stage) welcomeLabel.getScene().getWindow();
        scene = new Scene(root, BookApp.screenSize()[0], BookApp.screenSize()[1]);
        stage.setOnCloseRequest(e -> Platform.exit());
        stage.setScene(scene);
        stage.show();
    }
}
//t