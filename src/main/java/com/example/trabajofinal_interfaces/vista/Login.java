package com.example.trabajofinal_interfaces.vista;

import com.example.trabajofinal_interfaces.HelloApplication;
import com.example.trabajofinal_interfaces.controlador.controladorGestorPersonas;
import com.example.trabajofinal_interfaces.controlador.controladorLoginView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Login extends Application {
    @Override
    public void start(Stage stage) throws Exception {
//        FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("LoginView.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
//        stage.setTitle("ExtreVentos");
//        stage.setScene(scene);
//        controladorLoginView controller=fxmlLoader.getController();
//        controller.setStage(stage);
//        stage.show();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
        Parent root=loader.load();
        Scene scene=new Scene(root);
        stage.setScene(scene);
        //controladorGestorPersonas controller=loader.getController();
        controladorLoginView controller=loader.getController();
        controller.setStage(stage);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}
