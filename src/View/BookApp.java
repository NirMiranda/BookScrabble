package View;


import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.stage.*;

import java.io.IOException;

public class BookApp  extends Application {
    static final double MIN_WIDTH = 800;
    static final double MIN_HEIGHT = 600;

    /**
     The start method serves as the program's entry point, initiating its execution.
     It handles the loading of essential files and the setup of the scene for display.
     @param stage - the primary stage to be presented
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BookApp.class.getResource("/BookApp.View/welcomeWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), screenSize()[0], screenSize()[1]);
        scene.getStylesheets().addAll(
                getClass().getResource("/background.css").toExternalForm(),
                getClass().getResource("/buttonStyleSheets.css").toExternalForm()
        );
        stage.setOnCloseRequest(e -> Platform.exit());
        stage.setScene(scene);
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    /**
     * Retrieves the screen width and height and returns them as a double array.
     *
     * @return A double array containing the screen width and height
     */

    public static double[] screenSize() {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getBounds();

        double screenWidth = bounds.getWidth() - 300;
        double screenHeight = bounds.getHeight() - 100;

        double[] widthHeight = {screenWidth, screenHeight};
        return widthHeight;
    }
}
//t


