package com.example.newproject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    public void start(){
        System.out.println("hello there");
    }
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}