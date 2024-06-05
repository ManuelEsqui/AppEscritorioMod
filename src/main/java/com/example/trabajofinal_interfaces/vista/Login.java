package com.example.trabajofinal_interfaces.vista;

import com.example.trabajofinal_interfaces.HelloApplication;
import com.example.trabajofinal_interfaces.controlador.controladorGestorPersonas;
import com.example.trabajofinal_interfaces.controlador.controladorLoginView;
import com.example.trabajofinal_interfaces.utiles.utiles;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Login extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
        Parent root=loader.load();
        Scene scene=new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        Image imagen = new Image(Objects.requireNonNull(getClass().getResource("/com/example/trabajofinal_interfaces/images/logo.png")).toExternalForm());
        stage.getIcons().add(imagen);
        controladorLoginView controller=loader.getController();
        controller.setStage(stage);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}
